package yaremax.com.sa_task_04_06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yaremax.com.sa_task_04_06.entity.Company;

import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsById(UUID id);
    boolean existsByRegistrationNumber(String registrationNumber);
}
