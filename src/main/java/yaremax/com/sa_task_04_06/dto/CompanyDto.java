package yaremax.com.sa_task_04_06.dto;

import lombok.Builder;
import lombok.Data;
import yaremax.com.sa_task_04_06.entity.Company;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO for {@link Company} entity.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Builder
public class CompanyDto {
    /**
     * The unique identifier of the company.
     */
    private UUID id;

    /**
     * The name of the company.
     */
    private String name;

    /**
     * The registration number of the company.
     */
    private String registrationNumber;

    /**
     * The address of the company.
     */
    private String address;

    /**
     * The date and time when the company was created.
     */
    private LocalDate created_at;
}
