package yaremax.com.sa_task_04_06.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import yaremax.com.sa_task_04_06.entity.ReportDetails;

import java.util.UUID;

//@Repository works without annotation?
public interface ReportDetailsRepository extends MongoRepository<ReportDetails, UUID> {

    /*
    Optional<Student> findStudentByEmail(String email);

    @Query("db.student.find()")
    List<Student> findAllUsingQuery();

    @Query("db.student.find({'email': ?0})")
    Student findByEmailUsingQuery(String email);
     */
}
