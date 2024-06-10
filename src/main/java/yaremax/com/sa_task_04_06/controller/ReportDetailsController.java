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
import yaremax.com.sa_task_04_06.dto.ReportDetailsDto;
import yaremax.com.sa_task_04_06.exception.custom.ApiException;
import yaremax.com.sa_task_04_06.service.ReportDetailsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports/details")
@RequiredArgsConstructor
@Tag(name = "ReportDetailsController", description = "Controller for managing report details")
public class ReportDetailsController {
    private final ReportDetailsService reportDetailsService;

    @Operation(summary = "Create a new report details", description = "Create a new report details with the provided data")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportDetailsDto.class))})
    @ApiResponse(responseCode = "400", description = "Invalid data", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @ApiResponse(responseCode = "404", description = "Report not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @ApiResponse(responseCode = "409", description = "Report details already exists", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @PostMapping
    public ResponseEntity<ReportDetailsDto> createReportDetails(@RequestBody ReportDetailsDto createReportDetailsRequestDto) {
        return ResponseEntity.ok(reportDetailsService.createReportDetails(createReportDetailsRequestDto));
    }

    @Operation(summary = "Get all report details", description = "Retrieve all report details")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportDetailsDto.class, type = "array"))})
    @GetMapping
    public ResponseEntity<List<ReportDetailsDto>> getReportDetails() {
        return ResponseEntity.ok(reportDetailsService.getAllReportDetails());
    }

    @Operation(summary = "Get report details by ID", description = "Retrieve report details by its ID")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportDetailsDto.class))})
    @ApiResponse(responseCode = "404", description = "Report details not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @GetMapping("/{id}")
    public ResponseEntity<ReportDetailsDto> getReportDetailsById(@Parameter(description = "Report details ID") @PathVariable UUID id) {
        return ResponseEntity.ok(reportDetailsService.getReportDetailsById(id));
    }

    @Operation(summary = "Update report details", description = "Update an existing report details with the provided data")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReportDetailsDto.class))})
    @ApiResponse(responseCode = "400", description = "Invalid data", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @ApiResponse(responseCode = "404", description = "Report details not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @PutMapping("/{id}")
    public ResponseEntity<ReportDetailsDto> updateReportDetails(@Parameter(description = "Report details ID") @PathVariable UUID id,
                                                                @RequestBody ReportDetailsDto dto) {
        return ResponseEntity.ok(reportDetailsService.updateReportDetails(id, dto));
    }

    @Operation(summary = "Delete report details", description = "Delete report details by its ID")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE)})
    @ApiResponse(responseCode = "404", description = "Report details not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReportDetails(@Parameter(description = "Report details ID") @PathVariable UUID id) {
        reportDetailsService.deleteReportDetails(id);
        return ResponseEntity.ok("Deleted report details with id " + id);
    }
}