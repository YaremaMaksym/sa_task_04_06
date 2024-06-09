package yaremax.com.sa_task_04_06.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;
import java.util.UUID;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetails {
    @Id
    private UUID reportId;

    private org.bson.Document financialData; // JSON data
    private String comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportDetails that = (ReportDetails) o;
        return Objects.equals(reportId, that.reportId) && Objects.equals(financialData, that.financialData) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId, financialData, comments);
    }
}
