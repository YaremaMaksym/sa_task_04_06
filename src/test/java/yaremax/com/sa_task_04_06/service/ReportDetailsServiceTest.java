package yaremax.com.sa_task_04_06.service;


import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yaremax.com.sa_task_04_06.dto.ReportDetailsDto;
import yaremax.com.sa_task_04_06.entity.Report;
import yaremax.com.sa_task_04_06.entity.ReportDetails;
import yaremax.com.sa_task_04_06.exception.custom.DuplicateResourceException;
import yaremax.com.sa_task_04_06.exception.custom.InvalidDataException;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.ReportDetailsRepository;
import yaremax.com.sa_task_04_06.repository.ReportRepository;
import yaremax.com.sa_task_04_06.service.mapper.ReportDetailsMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportDetailsServiceTest {

    @Mock
    private ReportDetailsRepository reportDetailsRepository;
    @Mock
    private ReportRepository reportRepository;

    private ReportDetailsService reportDetailsService;

    @BeforeEach
    void setUp() {
        reportDetailsService = new ReportDetailsService(reportDetailsRepository, reportRepository);
    }

    @Nested
    class CreateReportDetailsTests {
        @Test
        void createReportDetails_shouldCreateReportDetails_whenValidDataProvided() {
            // Arrange
            UUID reportId = UUID.randomUUID();
            ReportDetailsDto createReportDetailsRequestDto = ReportDetailsDto.builder()
                    .reportId(reportId)
                    .financialData(new Document("revenue", 100000))
                    .comments("Test comments")
                    .build();

            ReportDetails reportDetails = ReportDetails.builder()
                    .reportId(reportId)
                    .financialData(new Document("revenue", 100000))
                    .comments("Test comments")
                    .build();

            when(reportRepository.findById(reportId)).thenReturn(Optional.of(new Report()));
            when(reportDetailsRepository.existsById(reportId)).thenReturn(false);
            when(reportDetailsRepository.save(any(ReportDetails.class))).thenReturn(reportDetails);

            // Act
            ReportDetailsDto savedReportDetailsDto = reportDetailsService.createReportDetails(createReportDetailsRequestDto);

            // Assert
            assertThat(savedReportDetailsDto)
                    .usingRecursiveComparison()
                    .isEqualTo(createReportDetailsRequestDto);
            verify(reportRepository).findById(reportId);
            verify(reportDetailsRepository).existsById(reportId);
            verify(reportDetailsRepository).save(any(ReportDetails.class));
        }

        @Test
        void createReportDetails_shouldThrowInvalidDataException_whenReportIdIsNull() {
            // Arrange
            ReportDetailsDto createReportDetailsRequestDto = ReportDetailsDto.builder()
                    .financialData(new Document("revenue", 100000))
                    .comments("Test comments")
                    .build();

            // Act & Assert
            assertThatThrownBy(() -> reportDetailsService.createReportDetails(createReportDetailsRequestDto))
                    .isInstanceOf(InvalidDataException.class);

            verify(reportRepository, never()).findById(any());
            verify(reportDetailsRepository, never()).existsById(any());
            verify(reportDetailsRepository, never()).save(any());
        }

        @Test
        void createReportDetails_shouldThrowResourceNotFoundException_whenReportDoesNotExist() {
            // Arrange
            UUID nonExistentReportId = UUID.randomUUID();
            ReportDetailsDto createReportDetailsRequestDto = ReportDetailsDto.builder()
                    .reportId(nonExistentReportId)
                    .financialData(new Document("revenue", 100000))
                    .build();

            when(reportRepository.findById(nonExistentReportId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> reportDetailsService.createReportDetails(createReportDetailsRequestDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining(nonExistentReportId.toString());

            verify(reportRepository).findById(nonExistentReportId);
            verify(reportDetailsRepository, never()).existsById(any());
            verify(reportDetailsRepository, never()).save(any());
        }

        @Test
        void createReportDetails_shouldThrowDuplicateResourceException_whenReportDetailsAlreadyExist() {
            // Arrange
            UUID existingReportId = UUID.randomUUID();
            ReportDetailsDto createReportDetailsRequestDto = ReportDetailsDto.builder()
                    .reportId(existingReportId)
                    .financialData(new Document("revenue", 100000))
                    .build();

            when(reportRepository.findById(existingReportId)).thenReturn(Optional.of(new Report()));
            when(reportDetailsRepository.existsById(existingReportId)).thenReturn(true);

            // Act & Assert
            assertThatThrownBy(() -> reportDetailsService.createReportDetails(createReportDetailsRequestDto))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining(existingReportId.toString());

            verify(reportRepository).findById(existingReportId);
            verify(reportDetailsRepository).existsById(existingReportId);
            verify(reportDetailsRepository, never()).save(any());
        }
    }

    @Nested
    class GetAllReportDetailsTests {
        @Test
        void getAllReportDetails_shouldReturnListOfReportDetailsDto_whenReportDetailsExist() {
            // Arrange
            List<ReportDetails> reportDetailsList = Arrays.asList(
                    ReportDetails.builder().reportId(UUID.randomUUID()).financialData(new Document("revenue", 100000)).comments("Test1").build(),
                    ReportDetails.builder().reportId(UUID.randomUUID()).financialData(new Document("revenue", 200000)).comments("Test2").build()
            );

            when(reportDetailsRepository.findAll()).thenReturn(reportDetailsList);

            // Act
            List<ReportDetailsDto> result = reportDetailsService.getAllReportDetails();

            // Assert
            assertThat(result)
                    .hasSize(2)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactlyElementsOf(ReportDetailsMapper.INSTANCE.toDtoList(reportDetailsList));
            verify(reportDetailsRepository).findAll();
        }

        @Test
        void getAllReportDetails_shouldReturnEmptyList_whenNoReportDetailsExist() {
            // Arrange
            when(reportDetailsRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<ReportDetailsDto> result = reportDetailsService.getAllReportDetails();

            // Assert
            assertThat(result).isEmpty();
            verify(reportDetailsRepository).findAll();
        }
    }

    @Nested
    class GetReportDetailsByIdTests {
        @Test
        void getReportDetailsById_shouldReturnReportDetailsDto_whenReportDetailsExists() {
            // Arrange
            UUID id = UUID.randomUUID();
            ReportDetails reportDetails = ReportDetails.builder()
                    .reportId(id)
                    .financialData(new Document("revenue", 150000))
                    .comments("Existing report details")
                    .build();

            when(reportDetailsRepository.findById(id)).thenReturn(Optional.of(reportDetails));

            // Act
            ReportDetailsDto result = reportDetailsService.getReportDetailsById(id);

            // Assert
            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(ReportDetailsMapper.INSTANCE.toDto(reportDetails));
            verify(reportDetailsRepository).findById(id);
        }

        @Test
        void getReportDetailsById_shouldThrowResourceNotFoundException_whenReportDetailsNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(reportDetailsRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> reportDetailsService.getReportDetailsById(id))
                    .withMessageContaining(id.toString());

            verify(reportDetailsRepository).findById(id);
        }
    }

    @Nested
    class UpdateReportDetailsTests {
        @Test
        void updateReportDetails_shouldUpdateAndReturnReportDetails_whenReportDetailsExist() {
            // Arrange
            UUID id = UUID.randomUUID();
            ReportDetailsDto updatedDto = ReportDetailsDto.builder()
                    .reportId(id)
                    .financialData(new Document("revenue", 200000))
                    .comments("Updated comments")
                    .build();

            ReportDetails updatedReportDetails = ReportDetails.builder()
                    .reportId(id)
                    .financialData(new Document("revenue", 200000))
                    .comments("Updated comments")
                    .build();

            when(reportDetailsRepository.existsById(id)).thenReturn(true);
            when(reportDetailsRepository.save(any(ReportDetails.class))).thenReturn(updatedReportDetails);

            // Act
            ReportDetailsDto result = reportDetailsService.updateReportDetails(id, updatedDto);

            // Assert
            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(updatedDto);
            verify(reportDetailsRepository).existsById(id);
            verify(reportDetailsRepository).save(any(ReportDetails.class));
        }

        @Test
        void updateReportDetails_shouldThrowResourceNotFoundException_whenReportDetailsNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            ReportDetailsDto updatedDto = ReportDetailsDto.builder()
                    .reportId(id)
                    .financialData(new Document("revenue", 200000))
                    .comments("Updated comments")
                    .build();

            when(reportDetailsRepository.existsById(id)).thenReturn(false);

            // Act & Assert
            assertThatThrownBy(() -> reportDetailsService.updateReportDetails(id, updatedDto))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Report details with id " + id + " not found");

            verify(reportDetailsRepository).existsById(id);
            verify(reportDetailsRepository, never()).save(any(ReportDetails.class));
        }

        @Test
        void updateReportDetails_shouldThrowInvalidDataException_whenIdsDoNotMatch() {
            // Arrange
            UUID pathId = UUID.randomUUID();
            UUID dtoId = UUID.randomUUID();
            ReportDetailsDto updatedDto = ReportDetailsDto.builder()
                    .reportId(dtoId)  // This ID doesn't match the path ID
                    .financialData(new Document("revenue", 200000))
                    .comments("Updated comments")
                    .build();

            // Act & Assert
            assertThatThrownBy(() -> reportDetailsService.updateReportDetails(pathId, updatedDto))
                    .isInstanceOf(InvalidDataException.class)
                    .hasMessageContaining(pathId.toString(), dtoId.toString());

            verify(reportDetailsRepository, never()).existsById(any());
            verify(reportDetailsRepository, never()).save(any(ReportDetails.class));
        }

        @Test
        void updateReportDetails_shouldUpdatePartialData() {
            // Arrange
            UUID id = UUID.randomUUID();
            ReportDetailsDto originalDto = ReportDetailsDto.builder()
                    .reportId(id)
                    .financialData(new Document()
                            .append("revenue", 100000)
                            .append("operatingExpenses", 50000))
                    .comments("Original comments")
                    .build();

            ReportDetails originalReportDetails = ReportDetails.builder()
                    .reportId(id)
                    .financialData(new Document()
                            .append("revenue", 100000)
                            .append("operatingExpenses", 50000))
                    .comments("Original comments")
                    .build();

            ReportDetailsDto updatedDto = ReportDetailsDto.builder()
                    .reportId(id)
                    .financialData(new Document()
                            .append("revenue", 150000))  // Only updating revenue
                    .build();

            ReportDetails updatedReportDetails = ReportDetails.builder()
                    .reportId(id)
                    .financialData(new Document()
                            .append("revenue", 150000))  // Only revenue is updated
                    .build();

            when(reportDetailsRepository.existsById(id)).thenReturn(true);
            when(reportDetailsRepository.save(any(ReportDetails.class))).thenReturn(updatedReportDetails);

            // Act
            ReportDetailsDto result = reportDetailsService.updateReportDetails(id, updatedDto);

            // Assert
            assertThat(result)
                    .usingRecursiveComparison()
                    .isEqualTo(updatedDto);
            assertThat(result.getFinancialData().keySet()).containsOnly("revenue");
            assertThat(result.getComments()).isNull();
            verify(reportDetailsRepository).existsById(id);
            verify(reportDetailsRepository).save(any(ReportDetails.class));
        }
    }

    @Nested
    class DeleteReportDetailsTests {
        @Test
        void deleteReportDetails_shouldDeleteReportDetails_whenReportDetailsExist() {
            // Arrange
            UUID id = UUID.randomUUID();
            ReportDetails foundReportDetails = new ReportDetails();
            when(reportDetailsRepository.findById(id)).thenReturn(Optional.of(foundReportDetails));

            // Act
            reportDetailsService.deleteReportDetails(id);

            // Assert
            verify(reportDetailsRepository).findById(id);
            verify(reportDetailsRepository).delete(foundReportDetails);
        }

        @Test
        void deleteReportDetails_shouldThrowResourceNotFoundException_whenReportDetailsNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(reportDetailsRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> reportDetailsService.deleteReportDetails(id))
                    .withMessageContaining(id.toString());

            verify(reportDetailsRepository).findById(id);
            verify(reportDetailsRepository, never()).delete(any(ReportDetails.class));
        }
    }
}