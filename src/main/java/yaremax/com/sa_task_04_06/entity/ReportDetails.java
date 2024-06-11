package yaremax.com.sa_task_04_06.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;
import java.util.UUID;

/**
 * ReportDetails entity class
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Document(collection = "reportDetails")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetails {
    /**
     * Report id
     */
    @Id
    @Field("reportId")
    private UUID reportId;

    /**
     * JSON data of the financial details
     */
    @Field("financialData")
    private org.bson.Document financialData;

    /**
     * Comments about the report
     */
    @Field("comments")
    private String comments;

    /**
     * Checks if the report is equal to another
     *
     * @param o the object to check equality with
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportDetails that = (ReportDetails) o;
        return Objects.equals(reportId, that.reportId) && Objects.equals(financialData, that.financialData) && Objects.equals(comments, that.comments);
    }

    /**
     * Calculates hash code for the report details
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(reportId, financialData, comments);
    }
}
