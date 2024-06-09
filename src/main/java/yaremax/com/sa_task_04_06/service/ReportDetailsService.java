package yaremax.com.sa_task_04_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yaremax.com.sa_task_04_06.dto.ReportDetailsDto;
import yaremax.com.sa_task_04_06.entity.Report;
import yaremax.com.sa_task_04_06.entity.ReportDetails;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.ReportDetailsRepository;
import yaremax.com.sa_task_04_06.service.mapper.ReportDetailsMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportDetailsService {
    private final ReportDetailsRepository reportDetailsRepository;
    private final ReportDetailsMapper reportDetailsMapper = ReportDetailsMapper.INSTANCE;

    @Transactional
    public ReportDetailsDto createReportDetails(ReportDetailsDto createReportDetailsRequestDto) {
        ReportDetails reportDetails = reportDetailsMapper.toEntity(createReportDetailsRequestDto);
        ReportDetails savedReportDetails = reportDetailsRepository.save(reportDetails);
        return reportDetailsMapper.toDto(savedReportDetails);
    }

    public List<ReportDetailsDto> getAllReportDetails() {
        return reportDetailsMapper.toDtoList(reportDetailsRepository.findAll());
    }

    public ReportDetailsDto getReportDetailsById(UUID id) {
        ReportDetails foundReportDetails = reportDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report details with id " + id + " not found"));
        return reportDetailsMapper.toDto(foundReportDetails);
    }

    @Transactional
    public ReportDetailsDto updateReportDetails(UUID id, ReportDetailsDto updateReportDetailsRequestDto) {
        if (!reportDetailsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Report details with id " + id + " not found");
        }
        ReportDetails reportDetails = reportDetailsMapper.toEntity(updateReportDetailsRequestDto);
        ReportDetails savedReportDetails = reportDetailsRepository.save(reportDetails);
        return reportDetailsMapper.toDto(savedReportDetails);
    }

    @Transactional
    public void deleteReportDetails(UUID id) {
        ReportDetails reportDetailsToDelete = reportDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report details with id " + id + " not found"));
        reportDetailsRepository.delete(reportDetailsToDelete);
    }
}