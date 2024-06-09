package yaremax.com.sa_task_04_06.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yaremax.com.sa_task_04_06.dto.ReportDto;
import yaremax.com.sa_task_04_06.service.ReportService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportDto> createReport(@RequestBody ReportDto createReportRequestDto) {
        return ResponseEntity.ok(reportService.createReport(createReportRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReports(@RequestParam(required = false) UUID companyId) {
        if (companyId != null) {
            return ResponseEntity.ok(reportService.getAllReportByCompanyId(companyId));
        }
        else {
            return ResponseEntity.ok(reportService.getAllReports());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDto> getReportById(@PathVariable UUID id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportDto> updateReport(@PathVariable UUID id,
                               @RequestBody ReportDto updateReportRequestDto) {
        return ResponseEntity.ok(reportService.updateReport(id, updateReportRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReport(@PathVariable UUID id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok("Deleted report with id " + id);
    }
}
