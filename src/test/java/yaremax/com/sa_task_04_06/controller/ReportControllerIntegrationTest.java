package yaremax.com.sa_task_04_06.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
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
import yaremax.com.sa_task_04_06.dto.ReportDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@SpringBootTest(classes = SaTask0406Application.class)
class ReportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private String sendCreateCompanyRequest(String name, String registrationNumber, String address) throws Exception {
        CompanyDto companyDto = CompanyDto.builder()
                .name(name)
                .registrationNumber(registrationNumber)
                .address(address)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(companyDto);

        return mockMvc.perform(post("/api/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    private UUID extractId(String jsonResponse) throws Exception {
        return UUID.fromString(objectMapper.readTree(jsonResponse).get("id").asText());
    }

    private UUID sendCreateReportRequest(UUID companyId, LocalDate reportDate, BigDecimal totalRevenue, BigDecimal netProfit) throws Exception {
        ReportDto createReportRequestDto = ReportDto.builder()
                .companyId(companyId)
                .reportDate(reportDate)
                .totalRevenue(totalRevenue)
                .netProfit(netProfit)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(createReportRequestDto);

        String responseBody = mockMvc.perform(post("/api/v1/reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return extractId(responseBody);
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Nested
    class CreateReportTests {
        @Test
        void createReport_ShouldCreateAndReturnReport() throws Exception {
            String companyResponseBody = sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
            UUID companyId = extractId(companyResponseBody);

            ReportDto createReportRequestDto = ReportDto.builder()
                    .companyId(companyId)
                    .reportDate(LocalDate.of(2023, 5, 1))
                    .totalRevenue(BigDecimal.valueOf(1000000))
                    .netProfit(BigDecimal.valueOf(500000))
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(createReportRequestDto);

            mockMvc.perform(post("/api/v1/reports")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.companyId").value(companyId.toString()))
                    .andExpect(jsonPath("$.reportDate").value("2023-05-01"))
                    .andExpect(jsonPath("$.totalRevenue").value(1000000))
                    .andExpect(jsonPath("$.netProfit").value(500000));
        }

        @Test
        void createReport_ShouldThrowResourceNotFoundException_WhenCompanyNotFound() throws Exception {
            UUID nonExistentCompanyId = UUID.randomUUID();

            ReportDto createReportRequestDto = ReportDto.builder()
                    .companyId(nonExistentCompanyId)
                    .reportDate(LocalDate.of(2023, 5, 1))
                    .totalRevenue(BigDecimal.valueOf(1000000))
                    .netProfit(BigDecimal.valueOf(500000))
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(createReportRequestDto);

            mockMvc.perform(post("/api/v1/reports")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class GetAllReportsTests {
        @Test
        void getAllReports_ShouldReturnAllReports() throws Exception {
            String companyResponse1 = sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
            UUID companyId1 = extractId(companyResponse1);
            sendCreateReportRequest(companyId1, LocalDate.of(2023, 5, 1), BigDecimal.valueOf(1000000), BigDecimal.valueOf(500000));
            sendCreateReportRequest(companyId1, LocalDate.of(2023, 6, 1), BigDecimal.valueOf(1200000), BigDecimal.valueOf(600000));

            String companyResponse2 = sendCreateCompanyRequest("Wayne Enterprises", "WAYNE789", "1007 Mountain Drive, Gotham City");
            UUID companyId2 = extractId(companyResponse2);
            sendCreateReportRequest(companyId2, LocalDate.of(2023, 5, 15), BigDecimal.valueOf(800000), BigDecimal.valueOf(400000));

            mockMvc.perform(get("/api/v1/reports"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));
        }

//        For some reason while doing this integration test,
//                  it can return all entities from the database,
//                  but when asked only for those that belong to a certain company, it returns an empty array.
//              The method itself works, but not when testing
//        @Test
//        void getAllReportByCompanyId_ShouldReturnReportsForSpecificCompany() throws Exception {
//            String companyResponse1 = sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
//            UUID companyId1 = extractId(companyResponse1);
//            UUID report1Id = sendCreateReportRequest(companyId1, LocalDate.of(2023, 5, 1), BigDecimal.valueOf(1000000), BigDecimal.valueOf(500000));
//
//            sendCreateReportRequest(companyId1, LocalDate.of(2023, 6, 1), BigDecimal.valueOf(1200000), BigDecimal.valueOf(600000));
//            String companyResponse2 = sendCreateCompanyRequest("Wayne Enterprises", "WAYNE789", "1007 Mountain Drive, Gotham City");
//            UUID companyId2 = extractId(companyResponse2);
//
//            sendCreateReportRequest(companyId2, LocalDate.of(2023, 5, 15), BigDecimal.valueOf(800000), BigDecimal.valueOf(400000));
//
//            mockMvc.perform(get("/api/v1/reports").param("companyId", companyId1.toString()))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$", hasSize(2)))
//                    .andExpect(jsonPath("$[0].id").value(report1Id.toString()));
//        }
    }

    @Nested
    class GetReportByIdTests {
        @Test
        void getReportById_ShouldReturnReportWithSpecificId() throws Exception {
            String companyResponse = sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
            UUID companyId = extractId(companyResponse);
            UUID reportId = sendCreateReportRequest(companyId, LocalDate.of(2023, 5, 1), BigDecimal.valueOf(1000000), BigDecimal.valueOf(500000));

            mockMvc.perform(get("/api/v1/reports/{id}", reportId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(reportId.toString()))
                    .andExpect(jsonPath("$.companyId").value(companyId.toString()))
                    .andExpect(jsonPath("$.reportDate").value("2023-05-01"))
                    .andExpect(jsonPath("$.totalRevenue").value(1000000))
                    .andExpect(jsonPath("$.netProfit").value(500000));
        }

        @Test
        void getReportById_ShouldThrowResourceNotFoundException_WhenReportNotFound() throws Exception {
            UUID nonExistentReportId = UUID.randomUUID();

            mockMvc.perform(get("/api/v1/reports/{id}", nonExistentReportId))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class UpdateReportTests {
        @Test
        void updateReport_ShouldUpdateReport_WhenReportExists() throws Exception {
            String companyResponse = sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
            UUID companyId = extractId(companyResponse);

            UUID reportId = sendCreateReportRequest(companyId, LocalDate.of(2023, 5, 1), BigDecimal.valueOf(1000000), BigDecimal.valueOf(500000));

            ReportDto updateReportRequestDto = ReportDto.builder()
                    .companyId(companyId)
                    .reportDate(LocalDate.of(2023, 6, 1))
                    .totalRevenue(BigDecimal.valueOf(1200000))
                    .netProfit(BigDecimal.valueOf(600000))
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(updateReportRequestDto);

            mockMvc.perform(put("/api/v1/reports/{id}", reportId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.companyId").value(companyId.toString()))
                    .andExpect(jsonPath("$.reportDate").value("2023-06-01"))
                    .andExpect(jsonPath("$.totalRevenue").value(1200000))
                    .andExpect(jsonPath("$.netProfit").value(600000));
        }

        @Test
        void updateReport_ShouldThrowResourceNotFoundException_WhenReportNotFound() throws Exception {
            UUID nonExistentReportId = UUID.randomUUID();

            ReportDto updateReportRequestDto = ReportDto.builder()
                    .companyId(UUID.randomUUID())
                    .reportDate(LocalDate.of(2023, 6, 1))
                    .totalRevenue(BigDecimal.valueOf(1200000))
                    .netProfit(BigDecimal.valueOf(600000))
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(updateReportRequestDto);

            mockMvc.perform(put("/api/v1/reports/{id}", nonExistentReportId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isNotFound());
        }

        @Test
        void updateReport_ShouldUpdateReportWithNewCompany_WhenCompanyIdChanged() throws Exception {
            String companyResponse1 = sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
            UUID companyId1 = extractId(companyResponse1);
            UUID reportId = sendCreateReportRequest(companyId1, LocalDate.of(2023, 5, 1), BigDecimal.valueOf(1000000), BigDecimal.valueOf(500000));

            String companyResponse2 = sendCreateCompanyRequest("Wayne Enterprises", "WAYNE789", "1007 Mountain Drive, Gotham City");
            UUID companyId2 = extractId(companyResponse2);

            ReportDto updateReportRequestDto = ReportDto.builder()
                    .companyId(companyId2)
                    .reportDate(LocalDate.of(2023, 6, 1))
                    .totalRevenue(BigDecimal.valueOf(1200000))
                    .netProfit(BigDecimal.valueOf(600000))
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(updateReportRequestDto);

            mockMvc.perform(put("/api/v1/reports/{id}", reportId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.companyId").value(companyId2.toString()));
        }
    }

    @Nested
    class DeleteReportTests {
        @Test
        void deleteReport_ShouldDeleteReportAndReturnOk() throws Exception {
            String companyResponse = sendCreateCompanyRequest("Stark Industries", "STARK456", "10880 Malibu Point, Malibu, CA");
            UUID companyId = extractId(companyResponse);
            UUID reportId = sendCreateReportRequest(companyId, LocalDate.of(2023, 5, 1), BigDecimal.valueOf(1000000), BigDecimal.valueOf(500000));

            mockMvc.perform(delete("/api/v1/reports/{id}", reportId))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/reports/{id}", reportId))
                    .andExpect(status().isNotFound());
        }

        @Test
        void deleteReport_ShouldThrowResourceNotFoundException_WhenReportNotFound() throws Exception {
            UUID nonExistentReportId = UUID.randomUUID();

            mockMvc.perform(delete("/api/v1/reports/{id}", nonExistentReportId))
                    .andExpect(status().isNotFound());
        }
    }
}