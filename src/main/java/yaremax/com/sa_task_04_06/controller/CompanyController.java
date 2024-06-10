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
import yaremax.com.sa_task_04_06.dto.CompanyDto;
import yaremax.com.sa_task_04_06.exception.custom.ApiException;
import yaremax.com.sa_task_04_06.service.CompanyService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Tag(name = "CompanyController", description = "Controller for managing companies")
public class CompanyController {
    private final CompanyService companyService;

    @Operation(summary = "Create a new company", description = "Create a new company with the provided data")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CompanyDto.class))})
    @ApiResponse(responseCode = "409", description = "Company with the given registration number already exists", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto createCompanyRequestDto) {
        return ResponseEntity.ok(companyService.createCompany(createCompanyRequestDto));
    }

    @Operation(summary = "Get all companies", description = "Retrieve all companies")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CompanyDto.class, type = "array"))})
    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @Operation(summary = "Get a company by ID", description = "Retrieve a company by its ID")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CompanyDto.class))})
    @ApiResponse(responseCode = "404", description = "Company not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getCompanyById(@Parameter(description = "Company ID") @PathVariable UUID id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @Operation(summary = "Update a company", description = "Update an existing company with the provided data")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CompanyDto.class))})
    @ApiResponse(responseCode = "404", description = "Company not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @ApiResponse(responseCode = "409", description = "Company with the given registration number already exists", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> updateCompany(@Parameter(description = "Company ID") @PathVariable UUID id,
                                                    @RequestBody CompanyDto updateCompanyRequestDto) {
        return ResponseEntity.ok(companyService.updateCompany(id, updateCompanyRequestDto));
    }

    @Operation(summary = "Delete a company", description = "Delete a company by its ID")
    @ApiResponse(responseCode = "200", description = "Successful response", content = {@Content(mediaType = MediaType.TEXT_PLAIN_VALUE)})
    @ApiResponse(responseCode = "404", description = "Company not found", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiException.class))})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@Parameter(description = "Company ID") @PathVariable UUID id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok("Deleted company with id " + id);
    }
}