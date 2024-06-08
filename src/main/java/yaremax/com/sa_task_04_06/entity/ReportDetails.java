package yaremax.com.sa_task_04_06.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data // todo: remove @Data and add getter&setter + safe hashcode and equals
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetails {
    @Id
    private UUID reportId;

    private MultipartFile financialData; // JSON data
    private String comments;
}
