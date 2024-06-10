package yaremax.com.sa_task_04_06.dto;

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
    private UUID id;

    /**
     * The unique identifier of the company the report belongs to.
     */
    private UUID companyId;

    /**
     * The date of the report.
     */
    private LocalDate reportDate;

    /**
     * The total revenue for the report.
     */
    private BigDecimal totalRevenue;

    /**
     * The net profit for the report.
     */
    private BigDecimal netProfit;
}
