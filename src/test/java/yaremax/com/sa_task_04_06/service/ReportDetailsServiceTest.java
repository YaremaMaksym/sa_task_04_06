package yaremax.com.sa_task_04_06.service;


import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yaremax.com.sa_task_04_06.dto.ReportDetailsDto;
import yaremax.com.sa_task_04_06.entity.ReportDetails;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.ReportDetailsRepository;
import yaremax.com.sa_task_04_06.service.mapper.ReportDetailsMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportDetailsServiceTest {

    @Mock
    private ReportDetailsRepository reportDetailsRepository;

    private ReportDetailsService reportDetailsService;

    @BeforeEach
    void setUp() {
        reportDetailsService = new ReportDetailsService(reportDetailsRepository);
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

            when(reportDetailsRepository.save(any(ReportDetails.class))).thenReturn(reportDetails);

            // Act
            ReportDetailsDto savedReportDetailsDto = reportDetailsService.createReportDetails(createReportDetailsRequestDto);

            // Assert
            assertThat(savedReportDetailsDto)
                    .usingRecursiveComparison()
                    .isEqualTo(createReportDetailsRequestDto);
            verify(reportDetailsRepository).save(any(ReportDetails.class));
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
                    .financialData(new Document("revenue", 200000))
                    .comments("Updated comments")
                    .build();

            when(reportDetailsRepository.existsById(id)).thenReturn(false);

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> reportDetailsService.updateReportDetails(id, updatedDto))
                    .withMessageContaining(id.toString());

            verify(reportDetailsRepository).existsById(id);
            verify(reportDetailsRepository, never()).save(any(ReportDetails.class));
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