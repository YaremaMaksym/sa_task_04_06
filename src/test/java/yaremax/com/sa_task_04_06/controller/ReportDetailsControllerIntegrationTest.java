package yaremax.com.sa_task_04_06.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import yaremax.com.sa_task_04_06.SaTask0406Application;
import yaremax.com.sa_task_04_06.dto.CompanyDto;
import yaremax.com.sa_task_04_06.dto.ReportDetailsDto;
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
class ReportDetailsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate; // to clean up db after each test

    private UUID extractField(String jsonResponse, String fieldName) throws Exception {
        return UUID.fromString(objectMapper.readTree(jsonResponse).get(fieldName).asText());
    }

    private UUID sendCreateCompanyRequest(String name, String registrationNumber, String address) throws Exception {
        CompanyDto companyDto = CompanyDto.builder()
                .name(name)
                .registrationNumber(registrationNumber)
                .address(address)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(companyDto);

        String responseBody = mockMvc.perform(post("/api/v1/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return extractField(responseBody, "id");
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

        return extractField(responseBody, "id");
    }

    private UUID sendCreateReportDetailsRequest(UUID reportId, org.bson.Document financialData, String comments) throws Exception {
        ReportDetailsDto createReportDetailsRequestDto = ReportDetailsDto.builder()
                .reportId(reportId)
                .financialData(financialData)
                .comments(comments)
                .build();

        String jsonRequest = objectMapper.writeValueAsString(createReportDetailsRequestDto);

        String responseBody = mockMvc.perform(post("/api/v1/reports/details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return extractField(responseBody, "reportId");
    }

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @AfterEach
    void cleanUp() {
        mongoTemplate.getCollectionNames()
                .forEach(collectionName -> mongoTemplate.dropCollection(collectionName));
    }

    @Nested
    class CreateReportDetailsTests {
        @Test
        void createReportDetails_ShouldCreateAndReturnReportDetails() throws Exception {
            UUID companyId = sendCreateCompanyRequest("Test Company", "1234567890", "Test Address");
            UUID reportId = sendCreateReportRequest(companyId, LocalDate.now(), BigDecimal.valueOf(10000000), BigDecimal.valueOf(2000000));

            org.bson.Document financialData = new org.bson.Document()
                    .append("operatingExpenses", 1500000)
                    .append("researchAndDevelopment", 1000000)
                    .append("marketingExpenses", 500000);
            String comments = "Increased R&D spending for new tech.";

            ReportDetailsDto createReportDetailsRequestDto = ReportDetailsDto.builder()
                    .reportId(reportId)
                    .financialData(financialData)
                    .comments(comments)
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(createReportDetailsRequestDto);

            mockMvc.perform(post("/api/v1/reports/details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reportId").value(reportId.toString()))
                    .andExpect(jsonPath("$.financialData.operatingExpenses").value(1500000))
                    .andExpect(jsonPath("$.financialData.researchAndDevelopment").value(1000000))
                    .andExpect(jsonPath("$.financialData.marketingExpenses").value(500000))
                    .andExpect(jsonPath("$.comments").value(comments));
        }

        @Test
        void createReportDetails_ShouldCreateWithMinimalData() throws Exception {
            UUID companyId = sendCreateCompanyRequest("Minimal Data Company", "0987654321", "Minimal Address");
            UUID reportId = sendCreateReportRequest(companyId, LocalDate.now(), BigDecimal.valueOf(5000000), BigDecimal.valueOf(1000000));

            org.bson.Document financialData = new org.bson.Document()
                    .append("operatingExpenses", 1000000);

            ReportDetailsDto createReportDetailsRequestDto = ReportDetailsDto.builder()
                    .reportId(reportId)
                    .financialData(financialData)
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(createReportDetailsRequestDto);

            mockMvc.perform(post("/api/v1/reports/details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reportId").value(reportId.toString()))
                    .andExpect(jsonPath("$.financialData.operatingExpenses").value(1000000))
                    .andExpect(jsonPath("$.comments").doesNotExist());
        }

        @Test
        void createReportDetails_ShouldReturnBadRequest_WhenReportIdIsMissing() throws Exception {
            org.bson.Document financialData = new org.bson.Document()
                    .append("operatingExpenses", 1000000);

            ReportDetailsDto createReportDetailsRequestDto = ReportDetailsDto.builder()
                    .financialData(financialData)
                    .comments("Test comments")
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(createReportDetailsRequestDto);

            mockMvc.perform(post("/api/v1/reports/details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createReportDetails_ShouldReturnNotFound_WhenReportDoesNotExist() throws Exception {
            UUID nonExistentReportId = UUID.randomUUID();
            org.bson.Document financialData = new org.bson.Document()
                    .append("operatingExpenses", 1000000);

            ReportDetailsDto createReportDetailsRequestDto = ReportDetailsDto.builder()
                    .reportId(nonExistentReportId)
                    .financialData(financialData)
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(createReportDetailsRequestDto);

            mockMvc.perform(post("/api/v1/reports/details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isNotFound());
        }

        @Test
        void createReportDetails_ShouldReturnConflict_WhenReportDetailsAlreadyExist() throws Exception {
            UUID companyId = sendCreateCompanyRequest("Duplicate Company", "1122334455", "Duplicate Address");
            UUID reportId = sendCreateReportRequest(companyId, LocalDate.now(), BigDecimal.valueOf(7000000), BigDecimal.valueOf(1500000));

            org.bson.Document financialData = new org.bson.Document()
                    .append("operatingExpenses", 1000000);

            ReportDetailsDto createReportDetailsRequestDto = ReportDetailsDto.builder()
                    .reportId(reportId)
                    .financialData(financialData)
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(createReportDetailsRequestDto);

            // First request - should be successful
            mockMvc.perform(post("/api/v1/reports/details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk());

            // Second request with the same reportId - should return conflict
            mockMvc.perform(post("/api/v1/reports/details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class GetReportDetailsTests {
        @Test
        void getReportDetails_ShouldReturnAllReportDetails() throws Exception {
            UUID companyId1 = sendCreateCompanyRequest("Company 1", "1111111111", "Address 1");
            UUID reportId1 = sendCreateReportRequest(companyId1, LocalDate.now(), BigDecimal.valueOf(10000000), BigDecimal.valueOf(2000000));
            sendCreateReportDetailsRequest(reportId1, new org.bson.Document().append("operatingExpenses", 1500000), "Comments 1");

            UUID companyId2 = sendCreateCompanyRequest("Company 2", "2222222222", "Address 2");
            UUID reportId2 = sendCreateReportRequest(companyId2, LocalDate.now(), BigDecimal.valueOf(15000000), BigDecimal.valueOf(3000000));
            sendCreateReportDetailsRequest(reportId2, new org.bson.Document().append("researchAndDevelopment", 2000000), "Comments 2");

            mockMvc.perform(get("/api/v1/reports/details")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].reportId").value(reportId1.toString()))
                    .andExpect(jsonPath("$[0].financialData.operatingExpenses").value(1500000))
                    .andExpect(jsonPath("$[0].comments").value("Comments 1"))
                    .andExpect(jsonPath("$[1].reportId").value(reportId2.toString()))
                    .andExpect(jsonPath("$[1].financialData.researchAndDevelopment").value(2000000))
                    .andExpect(jsonPath("$[1].comments").value("Comments 2"));
        }

        @Test
        void getReportDetails_ShouldReturnEmptyList_WhenNoReportDetailsExist() throws Exception {
            mockMvc.perform(get("/api/v1/reports/details")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    class GetReportDetailsByIdTests {
        @Test
        void getReportDetailsById_ShouldReturnReportDetails_WhenExists() throws Exception {
            UUID companyId = sendCreateCompanyRequest("Test Company", "9876543210", "Test Address");
            UUID reportId = sendCreateReportRequest(companyId, LocalDate.now(), BigDecimal.valueOf(8000000), BigDecimal.valueOf(1800000));
            sendCreateReportDetailsRequest(reportId, new org.bson.Document().append("marketingExpenses", 1200000), "Marketing focus");

            mockMvc.perform(get("/api/v1/reports/details/{id}", reportId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reportId").value(reportId.toString()))
                    .andExpect(jsonPath("$.financialData.marketingExpenses").value(1200000))
                    .andExpect(jsonPath("$.comments").value("Marketing focus"));
        }

        @Test
        void getReportDetailsById_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
            UUID nonExistentId = UUID.randomUUID();

            mockMvc.perform(get("/api/v1/reports/details/{id}", nonExistentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class UpdateReportDetailsTests {
        @Test
        void updateReportDetails_ShouldUpdateAndReturnReportDetails() throws Exception {
            UUID companyId = sendCreateCompanyRequest("Update Company", "5555555555", "Update Address");
            UUID reportId = sendCreateReportRequest(companyId, LocalDate.now(), BigDecimal.valueOf(12000000), BigDecimal.valueOf(2500000));
            sendCreateReportDetailsRequest(reportId, new org.bson.Document().append("operatingExpenses", 1800000), "Initial comments");

            org.bson.Document updatedFinancialData = new org.bson.Document()
                    .append("operatingExpenses", 2000000)
                    .append("researchAndDevelopment", 1500000);
            String updatedComments = "Increased R&D and operating expenses";

            ReportDetailsDto updateReportDetailsRequestDto = ReportDetailsDto.builder()
                    .reportId(reportId)
                    .financialData(updatedFinancialData)
                    .comments(updatedComments)
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(updateReportDetailsRequestDto);

            mockMvc.perform(put("/api/v1/reports/details/{id}", reportId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reportId").value(reportId.toString()))
                    .andExpect(jsonPath("$.financialData.operatingExpenses").value(2000000))
                    .andExpect(jsonPath("$.financialData.researchAndDevelopment").value(1500000))
                    .andExpect(jsonPath("$.comments").value(updatedComments));
        }

        @Test
        void updateReportDetails_ShouldReturnNotFound_WhenReportDetailsDoNotExist() throws Exception {
            UUID nonExistentId = UUID.randomUUID();
            org.bson.Document financialData = new org.bson.Document()
                    .append("operatingExpenses", 1000000);

            ReportDetailsDto updateReportDetailsRequestDto = ReportDetailsDto.builder()
                    .reportId(nonExistentId)
                    .financialData(financialData)
                    .comments("Test comments")
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(updateReportDetailsRequestDto);

            mockMvc.perform(put("/api/v1/reports/details/{id}", nonExistentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isNotFound());
        }

        @Test
        void updateReportDetails_ShouldReturnBadRequest_WhenIdsDoNotMatch() throws Exception {
            UUID companyId = sendCreateCompanyRequest("Mismatch Company", "6666666666", "Mismatch Address");
            UUID reportId = sendCreateReportRequest(companyId, LocalDate.now(), BigDecimal.valueOf(9000000), BigDecimal.valueOf(1800000));
            sendCreateReportDetailsRequest(reportId, new org.bson.Document().append("operatingExpenses", 1600000), "Original comments");

            UUID mismatchedId = UUID.randomUUID();
            org.bson.Document financialData = new org.bson.Document()
                    .append("operatingExpenses", 1700000);

            ReportDetailsDto updateReportDetailsRequestDto = ReportDetailsDto.builder()
                    .reportId(mismatchedId)
                    .financialData(financialData)
                    .comments("Mismatched comments")
                    .build();

            String jsonRequest = objectMapper.writeValueAsString(updateReportDetailsRequestDto);

            mockMvc.perform(put("/api/v1/reports/details/{id}", reportId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteReportDetailsTests {
        @Test
        void deleteReportDetails_ShouldDeleteAndReturnOk() throws Exception {
            UUID companyId = sendCreateCompanyRequest("Delete Test Company", "9999999999", "Delete Test Address");
            UUID reportId = sendCreateReportRequest(companyId, LocalDate.now(), BigDecimal.valueOf(13000000), BigDecimal.valueOf(2600000));
            sendCreateReportDetailsRequest(reportId, new org.bson.Document()
                            .append("operatingExpenses", 2200000)
                            .append("marketingExpenses", 800000),
                    "Details to be deleted");

            mockMvc.perform(get("/api/v1/reports/details/{id}", reportId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reportId").value(reportId.toString()));

            mockMvc.perform(delete("/api/v1/reports/details/{id}", reportId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Deleted report details with id " + reportId));

            mockMvc.perform(get("/api/v1/reports/details/{id}", reportId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void deleteReportDetails_ShouldReturnNotFound_WhenReportDetailsDoNotExist() throws Exception {
            UUID nonExistentId = UUID.randomUUID();

            mockMvc.perform(delete("/api/v1/reports/details/{id}", nonExistentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void deleteReportDetails_ShouldNotAffectOtherReportDetails() throws Exception {
            UUID companyId1 = sendCreateCompanyRequest("Company A", "1111222233", "Address A");
            UUID reportId1 = sendCreateReportRequest(companyId1, LocalDate.now(), BigDecimal.valueOf(10000000), BigDecimal.valueOf(2000000));
            sendCreateReportDetailsRequest(reportId1, new org.bson.Document().append("operatingExpenses", 1800000), "Report A details");

            UUID companyId2 = sendCreateCompanyRequest("Company B", "4444555566", "Address B");
            UUID reportId2 = sendCreateReportRequest(companyId2, LocalDate.now(), BigDecimal.valueOf(15000000), BigDecimal.valueOf(3000000));
            sendCreateReportDetailsRequest(reportId2, new org.bson.Document().append("researchAndDevelopment", 2500000), "Report B details");

            mockMvc.perform(delete("/api/v1/reports/details/{id}", reportId1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/reports/details/{id}", reportId2)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.reportId").value(reportId2.toString()))
                    .andExpect(jsonPath("$.financialData.researchAndDevelopment").value(2500000))
                    .andExpect(jsonPath("$.comments").value("Report B details"));
        }
    }
}