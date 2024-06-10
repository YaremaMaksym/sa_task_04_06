package yaremax.com.sa_task_04_06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yaremax.com.sa_task_04_06.entity.Company;

import java.util.UUID;


/**
 * Jpa repository for {@link Company}.
 * 
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    /**
     * Checks if a {@link Company} with the given registration number exists.
     *
     * @param  registrationNumber  the registration number to check
     * @return                    true if a {@link Company} with the given registration number exists, false otherwise
     */
    boolean existsByRegistrationNumber(String registrationNumber);
}
