package yaremax.com.sa_task_04_06.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yaremax.com.sa_task_04_06.dto.ReportDto;
import yaremax.com.sa_task_04_06.entity.Company;
import yaremax.com.sa_task_04_06.entity.Report;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.ReportRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;
    @Mock
    private ReferenceService referenceService;

    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportService(reportRepository, referenceService);
    }

    @Nested
    class CreateReportTests {
        @Test
        void createReport_shouldCreateReport_whenCompanyExists() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            ReportDto reportDto = ReportDto.builder()
                    .companyId(companyId)
                    .reportDate(LocalDate.now())
                    .totalRevenue(BigDecimal.valueOf(100000))
                    .netProfit(BigDecimal.valueOf(50000))
                    .build();

            Company company = Company.builder().id(companyId).build();
            Report report = Report.builder()
                    .id(UUID.randomUUID())
                    .company(company)
                    .reportDate(reportDto.getReportDate())
                    .totalRevenue(reportDto.getTotalRevenue())
                    .netProfit(reportDto.getNetProfit())
                    .build();

            when(referenceService.getExistingCompanyReference(companyId)).thenReturn(company);
            when(reportRepository.save(any(Report.class))).thenReturn(report);

            // Act
            ReportDto savedReportDto = reportService.createReport(reportDto);

            // Assert
            assertThat(savedReportDto)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(reportDto);
            verify(referenceService).getExistingCompanyReference(companyId);
            verify(reportRepository).save(any(Report.class));
        }

        @Test
        void createReport_shouldThrowResourceNotFoundException_whenCompanyNotFound() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            ReportDto reportDto = ReportDto.builder()
                    .companyId(companyId)
                    .build();

            when(referenceService.getExistingCompanyReference(companyId))
                    .thenThrow(new ResourceNotFoundException("Company not found"));

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> reportService.createReport(reportDto))
                    .withMessageContaining("Company not found");

            verify(referenceService).getExistingCompanyReference(companyId);
            verify(reportRepository, never()).save(any(Report.class));
        }
    }

    @Nested
    class GetAllReportsTests {
        @Test
        void getAllReports_shouldReturnAllReports() {
            // Arrange
            List<Report> reports = Arrays.asList(
                    Report.builder().id(UUID.randomUUID()).reportDate(LocalDate.now()).build(),
                    Report.builder().id(UUID.randomUUID()).reportDate(LocalDate.now().minusDays(1)).build()
            );
            when(reportRepository.findAll()).thenReturn(reports);

            // Act
            List<ReportDto> reportDtos = reportService.getAllReports();

            // Assert
            assertThat(reportDtos).hasSize(2);
            assertThat(reportDtos.get(0).getId()).isEqualTo(reports.get(0).getId());
            assertThat(reportDtos.get(1).getId()).isEqualTo(reports.get(1).getId());
            verify(reportRepository).findAll();
        }

        @Test
        void getAllReports_shouldReturnEmptyList_whenNoReportsExist() {
            // Arrange
            when(reportRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<ReportDto> reportDtos = reportService.getAllReports();

            // Assert
            assertThat(reportDtos).isEmpty();
            verify(reportRepository).findAll();
        }
    }

    @Nested
    class GetAllReportByCompanyIdTests {
        @Test
        void getAllReportByCompanyId_shouldReturnCompanyReports() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            Company existingCompany = Company.builder().id(companyId).build();
            List<Report> reports = Arrays.asList(
                    Report.builder().id(UUID.randomUUID()).company(existingCompany).totalRevenue(BigDecimal.valueOf(1000)).build(),
                    Report.builder().id(UUID.randomUUID()).company(existingCompany).totalRevenue(BigDecimal.valueOf(2000)).build()
            );
            when(reportRepository.findAllByCompanyId(companyId)).thenReturn(reports);

            // Act
            List<ReportDto> reportDtos = reportService.getAllReportByCompanyId(companyId);

            // Assert
            assertThat(reportDtos).hasSize(2);
            assertThat(reportDtos.get(0).getTotalRevenue()).isEqualByComparingTo("1000");
            assertThat(reportDtos.get(1).getTotalRevenue()).isEqualByComparingTo("2000");
            verify(reportRepository).findAllByCompanyId(companyId);
        }

        @Test
        void getAllReportByCompanyId_shouldReturnEmptyList_whenNoReportsForCompany() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            when(reportRepository.findAllByCompanyId(companyId)).thenReturn(Collections.emptyList());

            // Act
            List<ReportDto> reportDtos = reportService.getAllReportByCompanyId(companyId);

            // Assert
            assertThat(reportDtos).isEmpty();
            verify(reportRepository).findAllByCompanyId(companyId);
        }
    }

    @Nested
    class GetReportByIdTests {
        @Test
        void getReportById_shouldReturnReportDto_whenReportExists() {
            // Arrange
            UUID reportId = UUID.randomUUID();
            Report report = Report.builder()
                    .id(reportId)
                    .reportDate(LocalDate.now())
                    .netProfit(BigDecimal.valueOf(5000))
                    .build();
            when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

            // Act
            ReportDto reportDto = reportService.getReportById(reportId);

            // Assert
            assertThat(reportDto.getId()).isEqualTo(reportId);
            assertThat(reportDto.getReportDate()).isEqualTo(report.getReportDate());
            assertThat(reportDto.getNetProfit()).isEqualByComparingTo("5000");
            verify(reportRepository).findById(reportId);
        }

        @Test
        void getReportById_shouldThrowResourceNotFoundException_whenReportNotFound() {
            // Arrange
            UUID reportId = UUID.randomUUID();
            when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> reportService.getReportById(reportId))
                    .withMessageContaining(reportId.toString())
                    .withMessageContaining("not found");

            verify(reportRepository).findById(reportId);
        }
    }

    @Nested
    class UpdateReportTests {
        @Test
        void updateReport_shouldUpdateReport_whenReportExistsAndCompanyUnchanged() {
            // Arrange
            UUID reportId = UUID.randomUUID();
            UUID companyId = UUID.randomUUID();
            Company company = Company.builder().id(companyId).build();
            Report existingReport = Report.builder()
                    .id(reportId)
                    .company(company)
                    .reportDate(LocalDate.now())
                    .totalRevenue(BigDecimal.valueOf(1000))
                    .netProfit(BigDecimal.valueOf(500))
                    .build();
            ReportDto updateReportDto = ReportDto.builder()
                    .companyId(companyId)
                    .reportDate(LocalDate.now().plusDays(1))
                    .totalRevenue(BigDecimal.valueOf(1500))
                    .netProfit(BigDecimal.valueOf(750))
                    .build();
            Report updatedReport = Report.builder()
                    .id(reportId)
                    .company(company)
                    .reportDate(updateReportDto.getReportDate())
                    .totalRevenue(updateReportDto.getTotalRevenue())
                    .netProfit(updateReportDto.getNetProfit())
                    .build();

            when(reportRepository.findById(reportId)).thenReturn(Optional.of(existingReport));
            when(reportRepository.save(any(Report.class))).thenReturn(updatedReport);

            // Act
            ReportDto resultDto = reportService.updateReport(reportId, updateReportDto);

            // Assert
            assertThat(resultDto)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(updateReportDto);
            verify(reportRepository).findById(reportId);
            verify(reportRepository).save(any(Report.class));
        }

        @Test
        void updateReport_shouldUpdateReportAndCompany_whenReportExistsAndNewCompanyExists() {
            // Arrange
            UUID reportId = UUID.randomUUID();
            UUID oldCompanyId = UUID.randomUUID();
            UUID newCompanyId = UUID.randomUUID();
            Company oldCompany = Company.builder().id(oldCompanyId).build();
            Company newCompany = Company.builder().id(newCompanyId).build();
            Report existingReport = Report.builder()
                    .id(reportId)
                    .company(oldCompany)
                    .build();
            ReportDto updateReportDto = ReportDto.builder()
                    .companyId(newCompanyId)
                    .build();
            Report updatedReport = Report.builder()
                    .id(reportId)
                    .company(newCompany)
                    .build();

            when(reportRepository.findById(reportId)).thenReturn(Optional.of(existingReport));
            when(referenceService.getExistingCompanyReference(newCompanyId)).thenReturn(newCompany);
            when(reportRepository.save(any(Report.class))).thenReturn(updatedReport);

            // Act
            ReportDto resultDto = reportService.updateReport(reportId, updateReportDto);

            // Assert
            assertThat(resultDto)
                    .usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(updateReportDto);
            verify(reportRepository).findById(reportId);
            verify(referenceService).getExistingCompanyReference(newCompanyId);
            verify(reportRepository).save(any(Report.class));
        }

        @Test
        void updateReport_shouldThrowResourceNotFoundException_whenReportNotFound() {
            // Arrange
            UUID reportId = UUID.randomUUID();
            ReportDto updateReportDto = ReportDto.builder().build();

            when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> reportService.updateReport(reportId, updateReportDto))
                    .withMessageContaining(reportId.toString());

            verify(reportRepository).findById(reportId);
            verify(referenceService, never()).getExistingCompanyReference(any());
            verify(reportRepository, never()).save(any(Report.class));
        }

        @Test
        void updateReport_shouldThrowResourceNotFoundException_whenNewCompanyNotFound() {
            // Arrange
            UUID reportId = UUID.randomUUID();
            UUID oldCompanyId = UUID.randomUUID();
            UUID newCompanyId = UUID.randomUUID();
            Company oldCompany = Company.builder().id(oldCompanyId).build();
            Report existingReport = Report.builder()
                    .id(reportId)
                    .company(oldCompany)
                    .build();
            ReportDto updateReportDto = ReportDto.builder()
                    .companyId(newCompanyId)
                    .build();

            when(reportRepository.findById(reportId)).thenReturn(Optional.of(existingReport));
            when(referenceService.getExistingCompanyReference(newCompanyId))
                    .thenThrow(new ResourceNotFoundException("Company with id " + newCompanyId + " not found"));

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> reportService.updateReport(reportId, updateReportDto))
                    .withMessageContaining(newCompanyId.toString());

            verify(reportRepository).findById(reportId);
            verify(referenceService).getExistingCompanyReference(newCompanyId);
            verify(reportRepository, never()).save(any(Report.class));
        }
    }

    @Nested
    class DeleteReportTests {
        @Test
        void deleteReport_shouldDeleteReport_whenReportExists() {
            // Arrange
            UUID id = UUID.randomUUID();
            Report foundReport = new Report();
            when(reportRepository.findById(id)).thenReturn(Optional.of(foundReport));

            // Act
            reportService.deleteReport(id);

            // Assert
            verify(reportRepository).findById(id);
            verify(reportRepository).delete(foundReport);
        }

        @Test
        void deleteReport_shouldThrowResourceNotFoundException_whenReportNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(reportRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> reportService.deleteReport(id))
                    .withMessageContaining(id.toString());

            verify(reportRepository).findById(id);
            verify(reportRepository, never()).delete(any(Report.class));
        }
    }
}