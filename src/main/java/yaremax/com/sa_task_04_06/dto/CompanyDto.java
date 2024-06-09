package yaremax.com.sa_task_04_06.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class CompanyDto {
    private UUID id;
    private String name;
    private String registrationNumber;
    private String address;
    private LocalDate created_at;
}
