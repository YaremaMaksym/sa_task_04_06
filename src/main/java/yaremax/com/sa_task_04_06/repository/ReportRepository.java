package yaremax.com.sa_task_04_06.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yaremax.com.sa_task_04_06.entity.Report;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findAllByCompanyId(UUID companyId);
}
