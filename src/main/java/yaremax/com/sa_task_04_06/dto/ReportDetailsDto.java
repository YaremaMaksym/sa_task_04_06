package yaremax.com.sa_task_04_06.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "The unique identifier of the report",
            example = "123e4567-e89b-12d3-a456-556642440000")
    private UUID reportId;

    /**
     * The financial data of the report.
     */
    @Schema(description = "The financial data of the report",
            example = "{\"revenue\": 100000, \"expenses\": 80000}")
    private org.bson.Document financialData;

    /**
     * The comments of the report.
     */
    @Schema(description = "The comments of the report",
         example = "This is a comment about the report.")
    private String comments;
}
