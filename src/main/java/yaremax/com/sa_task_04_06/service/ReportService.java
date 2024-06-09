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

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReferenceService referenceService;
    private final ReportMapper reportMapper = ReportMapper.INSTANCE;

    @Transactional
    public ReportDto createReport(ReportDto createReportRequestDto) {
        Report report = reportMapper.toEntity(createReportRequestDto);
        report.setCompany(referenceService.getExistingCompanyReference(createReportRequestDto.getCompanyId()));
        Report savedReport = reportRepository.save(report);
        return reportMapper.toDto(savedReport);
    }

    public List<ReportDto> getAllReports() {
        return reportMapper.toDtoList(reportRepository.findAll());
    }

    public List<ReportDto> getAllReportByCompanyId(UUID companyId) {
        return reportRepository.findAllByCompanyId(companyId).stream()
                .map(reportMapper::toDto)
                .collect(Collectors.toList());
    }

    public ReportDto getReportById(UUID id) {
        Report foundReport = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report with id " + id + " not found"));
        return reportMapper.toDto(foundReport);
    }

    @Transactional
    public ReportDto updateReport(UUID id, ReportDto updateReportRequestDto) {
        reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report with id " + id + " not found"));
        Company companyReference = referenceService.getExistingCompanyReference(updateReportRequestDto.getCompanyId());

        Report report = reportMapper.toEntity(updateReportRequestDto);
        report.setCompany(companyReference);
        Report savedReport = reportRepository.save(report);
        return reportMapper.toDto(savedReport);
    }

    @Transactional
    public void deleteReport(UUID id) {
        Report reportToDelete = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report with id " + id + " not found"));
        reportRepository.delete(reportToDelete);
    }
}
