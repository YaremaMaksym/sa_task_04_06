package yaremax.com.sa_task_04_06.exception.custom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * This class represents an exception that can be thrown by the API.
 * It contains a message, HTTP status, and a timestamp. Used in {@link yaremax.com.sa_task_04_06.exception.GlobalExceptionHandler}
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Data
@Builder
@AllArgsConstructor
public final class ApiException {

    /**
     * The message of the exception.
     */
    @Schema(description = "The message of the exception",
            example = "Resource not found")
    private final String message;

    /**
     * The HTTP status of the exception.
     */
    @Schema(description = "The HTTP status of the exception",
            example = "404")
    private final HttpStatus httpStatus;

    /**
     * The timestamp of when the exception was thrown.
     */
    @Schema(description = "The timestamp of when the exception was thrown",
            example = "2023-05-01T12:34:56.789Z")
    private final ZonedDateTime timeStamp;

}