package yaremax.com.sa_task_04_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yaremax.com.sa_task_04_06.dto.ReportDetailsDto;
import yaremax.com.sa_task_04_06.entity.Report;
import yaremax.com.sa_task_04_06.entity.ReportDetails;
import yaremax.com.sa_task_04_06.exception.custom.DuplicateResourceException;
import yaremax.com.sa_task_04_06.exception.custom.InvalidDataException;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.ReportDetailsRepository;
import yaremax.com.sa_task_04_06.repository.ReportRepository;
import yaremax.com.sa_task_04_06.service.mapper.ReportDetailsMapper;

import java.util.List;
import java.util.UUID;


/**
 * Service class for {@link ReportDetails}.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Service
@RequiredArgsConstructor
public class ReportDetailsService {
    private final ReportDetailsRepository reportDetailsRepository;
    private final ReportDetailsMapper reportDetailsMapper = ReportDetailsMapper.INSTANCE;
    private final ReportRepository reportRepository;

    /**
     * Creates a new {@link ReportDetails}.
     *
     * @param  createReportDetailsRequestDto    the {@link ReportDetailsDto} with {@link ReportDetailsDto} data
     * @return                                  the created {@link ReportDetailsDto}
     * @throws InvalidDataException             if report id is {@code null}
     * @throws ResourceNotFoundException        if the {@link Report} with the given ID is not found
     * @throws DuplicateResourceException       if the Report details with the given ID already exists
     */
    @Transactional
    public ReportDetailsDto createReportDetails(ReportDetailsDto createReportDetailsRequestDto) {
        UUID reportId = createReportDetailsRequestDto.getReportId();
        if (reportId == null) {
            throw new InvalidDataException("Report details id is null");
        }
        reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report with id " + reportId + " not found"));
        if (reportDetailsRepository.existsById(reportId)) {
            throw new DuplicateResourceException("Report details with id " + reportId + " already exists");
        }
        ReportDetails reportDetails = reportDetailsMapper.toEntity(createReportDetailsRequestDto);
        ReportDetails savedReportDetails = reportDetailsRepository.save(reportDetails);
        return reportDetailsMapper.toDto(savedReportDetails);
    }

    /**
     * Retrieves all {@link ReportDetails}.
     *
     * @return  the list of {@link ReportDetailsDto} objects
     */
    public List<ReportDetailsDto> getAllReportDetails() {
        return reportDetailsMapper.toDtoList(reportDetailsRepository.findAll());
    }

    /**
     * Retrieves a {@link ReportDetails} by its ID.
     *
     * @param  id   the ID of the {@link ReportDetails} to retrieve
     * @return      the {@link ReportDetailsDto} object corresponding to the given ID
     * @throws ResourceNotFoundException if the {@link ReportDetails} with the given ID is not found
     */
    public ReportDetailsDto getReportDetailsById(UUID id) {
        ReportDetails foundReportDetails = reportDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report details with id " + id + " not found"));
        return reportDetailsMapper.toDto(foundReportDetails);
    }

    /**
     * Updates a {@link ReportDetails}.
     *
     * @param  id                       the ID of the {@link ReportDetails} to update
     * @param  updateReportDetailsRequestDto   the {@link ReportDetailsDto} with updated {@link ReportDetails} data
     * @return                          the updated {@link ReportDetailsDto}
     * @throws InvalidDataException if report id's don't match
     * @throws ResourceNotFoundException if the {@link ReportDetails} with the given ID is not found
     */
    @Transactional
    public ReportDetailsDto updateReportDetails(UUID id, ReportDetailsDto updateReportDetailsRequestDto) {
        if (!updateReportDetailsRequestDto.getReportId().equals(id)) {
            throw new InvalidDataException("Id's don't match: " + id + ", " + updateReportDetailsRequestDto.getReportId());
        }
        if (!reportDetailsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report details with id " + id + " not found");
        }
        ReportDetails reportDetails = reportDetailsMapper.toEntity(updateReportDetailsRequestDto);
        ReportDetails savedReportDetails = reportDetailsRepository.save(reportDetails);
        return reportDetailsMapper.toDto(savedReportDetails);
    }

    /**
     * Deletes a {@link ReportDetails}.
     *
     * @param  id   the ID of the {@link ReportDetails} to delete
     * @throws ResourceNotFoundException if the {@link ReportDetails} with the given ID is not found
     */
    @Transactional
    public void deleteReportDetails(UUID id) {
        ReportDetails reportDetailsToDelete = reportDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report details with id " + id + " not found"));
        reportDetailsRepository.delete(reportDetailsToDelete);
    }
}