package yaremax.com.sa_task_04_06.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import yaremax.com.sa_task_04_06.entity.Company;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link Company} entity.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Builder
public class CompanyDto {
    /**
     * The unique identifier of the company.
     */
    @Schema(description = "The unique identifier of the company",
            example = "123e4567-e89b-12d3-a456-556642440000")
    private UUID id;

    /**
     * The name of the company.
     */
    @Schema(description = "The name of the company",
            example = "Acme Inc.")
    private String name;

    /**
     * The registration number of the company.
     */
    @Schema(description = "The registration number of the company",
            example = "ABC12345")
    private String registrationNumber;

    /**
     * The address of the company.
     */
    @Schema(description = "The address of the company",
            example = "123 Main St, Anytown USA")
    private String address;

    /**
     * The date and time when the company was created.
     */
    @Schema(description = "The date and time when the company was created",
            example = "2023-05-01")
    private LocalDate created_at;
}