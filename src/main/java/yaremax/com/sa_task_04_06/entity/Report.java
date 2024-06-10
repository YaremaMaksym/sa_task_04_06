package yaremax.com.sa_task_04_06.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Report entity class
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Entity
@Table(name = "reports")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    /**
     * Report id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Company related to the report
     */
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    /**
     * Report date
     */
    private LocalDate reportDate;

    /**
     * Total revenue for the report
     */
    private BigDecimal totalRevenue;

    /**
     * Net profit for the report
     */
    private BigDecimal netProfit;

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
        Report report = (Report) o;
        return Objects.equals(id, report.id) && Objects.equals(company, report.company) && Objects.equals(reportDate, report.reportDate) && Objects.equals(totalRevenue, report.totalRevenue) && Objects.equals(netProfit, report.netProfit);
    }

    /**
     * Calculates hash code for the report
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, company, reportDate, totalRevenue, netProfit);
    }
}
