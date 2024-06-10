package yaremax.com.sa_task_04_06.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yaremax.com.sa_task_04_06.entity.ReportDetails;

import java.util.UUID;

/**
 * Mongo repository for {@link ReportDetails}.
 * 
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
public interface ReportDetailsRepository extends MongoRepository<ReportDetails, UUID> {
}
