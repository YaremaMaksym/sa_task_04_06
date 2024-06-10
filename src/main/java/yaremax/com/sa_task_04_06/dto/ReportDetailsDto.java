package yaremax.com.sa_task_04_06.dto;

import lombok.Builder;
import lombok.Data;
import yaremax.com.sa_task_04_06.entity.ReportDetails;

import java.util.UUID;

/**
 * DTO for {@link ReportDetails} entity.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Builder
public class ReportDetailsDto {
    /**
     * The unique identifier of the report.
     */
    private UUID reportId;

    /**
     * The financial data of the report.
     */
    private org.bson.Document financialData;

    /**
     * The comments of the report.
     */
    private String comments;
}
