package yaremax.com.sa_task_04_06.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yaremax.com.sa_task_04_06.SaTask0406Application;
import yaremax.com.sa_task_04_06.dto.CompanyDto;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@SpringBootTest(classes = SaTask0406Application.class)
class CompanyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String sendCreateCompanyRequest(String name, String registrationNumber, String address) throws Exception {
        CompanyDto companyDto = CompanyDto.builder()
                .name(name)
                .registrationNumber(registrationNumber)
                .address(address)
                .build();

        String jsonRequest = new ObjectMapper().writeValueAsString(companyDto);

        return mockMvc.perform(post("/api/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    private UUID extractCompanyId(String jsonResponse) throws Exception {
        return UUID.fromString(new ObjectMapper().readTree(jsonResponse).get("id").asText());
    }

    @Nested
    class CreateCompanyTests {
        @Test
        void createCompany_ShouldCreateAndReturnCompany() throws Exception {
            CompanyDto companyDto = CompanyDto.builder()
                    .name("Stark Industries")
                    .registrationNumber("STARK456")
                    .address("10880 Malibu Point, Malibu, CA")
                    .build();

            String jsonRequest = new ObjectMapper().writeValueAsString(companyDto);

            mockMvc.perform(post("/api/v1/companies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Stark Industries"))
                    .andExpect(jsonPath("$.registrationNumber").value("STARK456"))
                    .andExpect(jsonPath("$.address").value("10880 Malibu Point, Malibu, CA"));
        }

        @Test
        void createCompany_DuplicateRegistrationNumber_ShouldReturnConflict() throws Exception {
            CompanyDto firstCompany = CompanyDto.builder()
                    .name("Stark Industries")
                    .registrationNumber("DUP123")
                    .address("10880 Malibu Point, Malibu, CA")
                    .build();

            CompanyDto secondCompany = CompanyDto.builder()
                    .name("Stark Enterprises")
                    .registrationNumber("DUP123")
                    .address("200 Park Avenue, New York City")
                    .build();

            String firstJsonRequest = new ObjectMapper().writeValueAsString(firstCompany);
            String secondJsonRequest = new ObjectMapper().writeValueAsString(secondCompany);

            mockMvc.perform(post("/api/v1/companies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(firstJsonRequest))
                    .andExpect(status().isOk());

            mockMvc.perform(post("/api/v1/companies")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(secondJsonRequest))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class GetAllCompaniesTests {
        @Test
        void getAllCompanies_ShouldReturnListOfCompanies() throws Exception {
            // Створення двох компаній
            sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
            sendCreateCompanyRequest("Wayne Enterprises", "WAYNE789", "1007 Mountain Drive, Gotham City");

            // Запит на отримання всіх компаній
            mockMvc.perform(get("/api/v1/companies")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].name").value("Stark Industries"))
                    .andExpect(jsonPath("$[1].name").value("Wayne Enterprises"));
        }
    }

    @Nested
    class GetCompanyByIdTests {
        @Test
        void getCompanyById_ExistingId_ShouldReturnCompany() throws Exception {
            String responseBody = sendCreateCompanyRequest("Acme Corporation", "ACME101", "42 Wile E. Coyote Way, Desert City");
            UUID companyId = extractCompanyId(responseBody);

            mockMvc.perform(get("/api/v1/companies/{id}", companyId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(companyId.toString()))
                    .andExpect(jsonPath("$.name").value("Acme Corporation"))
                    .andExpect(jsonPath("$.registrationNumber").value("ACME101"))
                    .andExpect(jsonPath("$.address").value("42 Wile E. Coyote Way, Desert City"));
        }

        @Test
        void getCompanyById_NonExistingId_ShouldReturnNotFound() throws Exception {
            UUID nonExistingId = UUID.randomUUID();

            mockMvc.perform(get("/api/v1/companies/{id}", nonExistingId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class UpdateCompanyTests {
        @Test
        void updateCompany_shouldUpdateCompany_whenCompanyExistsAndRegistrationNumberUnchanged() throws Exception {
            String responseBody = sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
            UUID companyId = extractCompanyId(responseBody);

            CompanyDto updatedCompanyDto = CompanyDto.builder()
                    .name("Stark Enterprises")
                    .registrationNumber("STARK456")
                    .address("200 Park Avenue, New York City")
                    .build();

            String jsonRequest = new ObjectMapper().writeValueAsString(updatedCompanyDto);

            mockMvc.perform(put("/api/v1/companies/{id}", companyId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Stark Enterprises"))
                    .andExpect(jsonPath("$.registrationNumber").value("STARK456"))
                    .andExpect(jsonPath("$.address").value("200 Park Avenue, New York City"));
        }

        @Test
        void updateCompany_shouldUpdateCompany_whenCompanyExistsAndNewRegistrationNumberNotExists() throws Exception {
            String responseBody = sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
            UUID companyId = extractCompanyId(responseBody);

            CompanyDto updatedCompanyDto = CompanyDto.builder()
                    .name("Stark Enterprises")
                    .registrationNumber("STARK789")
                    .address("200 Park Avenue, New York City")
                    .build();

            String jsonRequest = new ObjectMapper().writeValueAsString(updatedCompanyDto);

            mockMvc.perform(put("/api/v1/companies/{id}", companyId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Stark Enterprises"))
                    .andExpect(jsonPath("$.registrationNumber").value("STARK789"))
                    .andExpect(jsonPath("$.address").value("200 Park Avenue, New York City"));
        }

        @Test
        void updateCompany_shouldThrowResourceNotFoundException_whenCompanyNotFound() throws Exception {
            UUID nonExistentCompanyId = UUID.randomUUID();

            CompanyDto updatedCompanyDto = CompanyDto.builder()
                    .name("Stark Enterprises")
                    .registrationNumber("STARK789")
                    .address("200 Park Avenue, New York City")
                    .build();

            String jsonRequest = new ObjectMapper().writeValueAsString(updatedCompanyDto);

            mockMvc.perform(put("/api/v1/companies/{id}", nonExistentCompanyId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isNotFound());
        }

        @Test
        void updateCompany_shouldThrowDuplicateResourceException_whenNewRegistrationNumberExists() throws Exception {
            String responseBody1 = sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
            UUID companyId1 = extractCompanyId(responseBody1);

            String responseBody2 = sendCreateCompanyRequest("Wayne Enterprises", "WAYNE789", "1007 Mountain Drive, Gotham City");
            UUID companyId2 = extractCompanyId(responseBody2);

            CompanyDto updatedCompanyDto = CompanyDto.builder()
                    .name("Wayne Corp")
                    .registrationNumber("STARK456")
                    .address("1007 Mountain Drive, Gotham City")
                    .build();

            String jsonRequest = new ObjectMapper().writeValueAsString(updatedCompanyDto);

            mockMvc.perform(put("/api/v1/companies/{id}", companyId2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class DeleteCompany {
        @Test
        void deleteCompany_ExistingId_ShouldReturnOk() throws Exception {
            String responseBody = sendCreateCompanyRequest("Aperture Science", "APS789", "1 Infinite Loop, Upper Michigan");

            UUID companyId = extractCompanyId(responseBody);
            mockMvc.perform(delete("/api/v1/companies/{id}", companyId))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/companies/{id}", companyId))
                    .andExpect(status().isNotFound());
        }

        @Test
        void deleteCompany_NonExistingId_ShouldReturnNotFound() throws Exception {
            UUID nonExistingId = UUID.randomUUID();

            mockMvc.perform(delete("/api/v1/companies/{id}", nonExistingId))
                    .andExpect(status().isNotFound());
        }
    }
}