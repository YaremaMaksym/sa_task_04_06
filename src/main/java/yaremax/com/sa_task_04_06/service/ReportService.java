package yaremax.com.sa_task_04_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yaremax.com.sa_task_04_06.dto.ReportDto;
import yaremax.com.sa_task_04_06.entity.Company;
import yaremax.com.sa_task_04_06.entity.Report;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.ReportRepository;
import yaremax.com.sa_task_04_06.service.mapper.ReportMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for {@link Report}.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReferenceService referenceService;
    private final ReportMapper reportMapper = ReportMapper.INSTANCE;

    /**
     * Creates a new {@link Report} with the given {@link ReportDto}.
     *
     * @param  createReportRequestDto   the {@link ReportDto} with Report data
     * @return                          the created {@link Report}
     * @throws ResourceNotFoundException if the {@link Company} with the given ID does not exist
     */
    @Transactional
    public ReportDto createReport(ReportDto createReportRequestDto) {
        Report report = reportMapper.toEntity(createReportRequestDto);
        report.setCompany(referenceService.getExistingCompanyReference(createReportRequestDto.getCompanyId()));
        Report savedReport = reportRepository.save(report);
        return reportMapper.toDto(savedReport);
    }

    /**
     * Returns all {@link Report}s.
     *
     * @return              a list of {@link ReportDto} objects
     */
    public List<ReportDto> getAllReports() {
        return reportMapper.toDtoList(reportRepository.findAll());
    }

    /**
     * Returns all {@link Report}s for the given company.
     *
     * @param  companyId    the ID of the {@link Company} to retrieve reports for
     * @return              a list of {@link ReportDto} objects
     */
    public List<ReportDto> getAllReportByCompanyId(UUID companyId) {
        return reportRepository.findAllByCompanyId(companyId).stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a {@link Report} by its ID.
     *
     * @param  id   the ID of the {@link Report} to retrieve
     * @return      the {@link ReportDto} object corresponding to the given ID
     * @throws ResourceNotFoundException if the {@link Report} with the given ID is not found
     */
    public ReportDto getReportById(UUID id) {
        Report foundReport = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report with id " + id + " not found"));
        return reportMapper.toDto(foundReport);
    }

    /**
     * Updates a {@link Report} with the given {@link ReportDto}.
     *
     * @param  id                       the ID of the {@link Report} to update
     * @param  updateReportRequestDto   the {@link ReportDto} with updated Report data
     * @return                          the updated {@link Report}
     * @throws ResourceNotFoundException if the {@link Report} with the given ID is not found
     */
    @Transactional
    public ReportDto updateReport(UUID id,
                                   ReportDto updateReportRequestDto) {
        reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report with id " + id + " not found"));
        Company companyReference = referenceService.getExistingCompanyReference(updateReportRequestDto.getCompanyId());

        Report report = reportMapper.toEntity(updateReportRequestDto);
        report.setCompany(companyReference);
        Report savedReport = reportRepository.save(report);
        return reportMapper.toDto(savedReport);
    }

    /**
     * Deletes a {@link Report} by its ID.
     *
     * @param  id  the ID of the {@link Report} to delete
     * @throws ResourceNotFoundException if the {@link Report} with the given ID is not found
     */
    @Transactional
    public void deleteReport(UUID id) {
        Report reportToDelete = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report with id " + id + " not found"));
        reportRepository.delete(reportToDelete);
    }
}
