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

@Data
@Entity
@Table(name = "reports")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private LocalDate reportDate;
    private BigDecimal totalRevenue;
    private BigDecimal netProfit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id) && Objects.equals(company, report.company) && Objects.equals(reportDate, report.reportDate) && Objects.equals(totalRevenue, report.totalRevenue) && Objects.equals(netProfit, report.netProfit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, company, reportDate, totalRevenue, netProfit);
    }
}
