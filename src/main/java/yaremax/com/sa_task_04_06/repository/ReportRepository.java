package yaremax.com.sa_task_04_06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yaremax.com.sa_task_04_06.entity.Report;

import java.util.List;
import java.util.UUID;

/**
 * Jpa repository for {@link Report}.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    /**
     * Find all reports by company ID.
     *
     * @param companyId company ID
     * @return list of reports
     */
    List<Report> findAllByCompanyId(UUID companyId);
}
