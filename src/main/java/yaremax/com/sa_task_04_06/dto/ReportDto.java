package yaremax.com.sa_task_04_06.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import yaremax.com.sa_task_04_06.entity.Report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link Report} entity.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Builder
public class ReportDto {
    /**
     * The unique identifier of the report.
     */
    @Schema(description = "The unique identifier of the report",
            example = "123e4567-e89b-12d3-a456-556642440000")
    private UUID id;

    /**
     * The unique identifier of the company the report belongs to.
     */
    @Schema(description = "The unique identifier of the company the report belongs to",
            example = "456e7890-e89b-12d3-a456-556642440000")
    private UUID companyId;

    /**
     * The date of the report.
     */
    @Schema(description = "The date of the report",
            example = "2023-12-29")
    private LocalDate reportDate;

    /**
     * The total revenue for the report.
     */
    @Schema(description = "The total revenue for the report",
            example = "120000.50")
    private BigDecimal totalRevenue;

    /**
     * The net profit for the report.
     */
    @Schema(description = "The net profit for the report",
            example = "40000.25")
    private BigDecimal netProfit;
}
