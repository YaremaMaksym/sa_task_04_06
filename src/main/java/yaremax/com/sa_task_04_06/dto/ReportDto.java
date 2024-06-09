package yaremax.com.sa_task_04_06.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ReportDto {
    private UUID id;
    private UUID companyId;
    private LocalDate reportDate;
    private BigDecimal totalRevenue;
    private BigDecimal netProfit;
}