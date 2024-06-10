package yaremax.com.sa_task_04_06.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yaremax.com.sa_task_04_06.dto.ReportDetailsDto;
import yaremax.com.sa_task_04_06.service.ReportDetailsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports/details")
@RequiredArgsConstructor
public class ReportDetailsController {
    private final ReportDetailsService reportDetailsService;

    @PostMapping
    public ResponseEntity<ReportDetailsDto> createReportDetails(@RequestBody ReportDetailsDto createReportDetailsRequestDto) {
        return ResponseEntity.ok(reportDetailsService.createReportDetails(createReportDetailsRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<ReportDetailsDto>> getReportDetails() {
        return ResponseEntity.ok(reportDetailsService.getAllReportDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDetailsDto> getReportDetailsById(@PathVariable UUID id) {
        return ResponseEntity.ok(reportDetailsService.getReportDetailsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportDetailsDto> updateReportDetails(@PathVariable UUID id,
                                                                @RequestBody ReportDetailsDto dto) {
        return ResponseEntity.ok(reportDetailsService.updateReportDetails(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReportDetails(@PathVariable UUID id) {
        reportDetailsService.deleteReportDetails(id);
        return ResponseEntity.ok("Deleted report details with id " + id);
    }
}
