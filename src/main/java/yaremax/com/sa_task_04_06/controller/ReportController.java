package yaremax.com.sa_task_04_06.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yaremax.com.sa_task_04_06.dto.ReportDto;
import yaremax.com.sa_task_04_06.exception.custom.ApiException;
import yaremax.com.sa_task_04_06.service.ReportService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "ReportController", description = "Controller for managing reports")
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "Create a new report", description = "Create a new report with the provided data")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportDto.class))})
    @ApiResponse(responseCode = "404", description = "Company not exists", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @PostMapping
    public ResponseEntity<ReportDto> createReport(@RequestBody ReportDto createReportRequestDto) {
        return ResponseEntity.ok(reportService.createReport(createReportRequestDto));
    }

    @Operation(summary = "Get all reports", description = "Retrieve all reports, optionally filtered by company ID")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportDto.class, type = "array"))})
    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReports(@Parameter(description = "Company ID to filter reports by") @RequestParam(required = false) UUID companyId) {
        if (companyId != null) {
            return ResponseEntity.ok(reportService.getAllReportByCompanyId(companyId));
        } else {
            return ResponseEntity.ok(reportService.getAllReports());
        }
    }

    @Operation(summary = "Get a report by ID", description = "Retrieve a report by its ID")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportDto.class))})
    @ApiResponse(responseCode = "404", description = "Report not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @GetMapping("/{id}")
    public ResponseEntity<ReportDto> getReportById(@Parameter(description = "Report ID") @PathVariable UUID id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @Operation(summary = "Update a report", description = "Update an existing report with the provided data")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportDto.class))})
    @ApiResponse(responseCode = "404", description = "Report not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @PutMapping("/{id}")
    public ResponseEntity<ReportDto> updateReport(@Parameter(description = "Report ID") @PathVariable UUID id,
                                                  @RequestBody ReportDto updateReportRequestDto) {
        return ResponseEntity.ok(reportService.updateReport(id, updateReportRequestDto));
    }

    @Operation(summary = "Delete a report", description = "Delete a report by its ID")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE)})
    @ApiResponse(responseCode = "404", description = "Report not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReport(@Parameter(description = "Report ID") @PathVariable UUID id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok("Deleted report with id " + id);
    }
}
