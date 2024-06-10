package yaremax.com.sa_task_04_06;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot Application class.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Api for test task", version = "1.0", description = "Api for test task"))
public class SaTask0406Application {

    /**
     * Runs the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SaTask0406Application.class, args);
    }

}
