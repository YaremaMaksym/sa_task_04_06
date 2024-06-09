package yaremax.com.sa_task_04_06.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReportDetailsDto {
    private UUID reportId;
    private org.bson.Document financialData;
    private String comments;
}
