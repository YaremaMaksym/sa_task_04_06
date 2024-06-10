package yaremax.com.sa_task_04_06.exception.custom;

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
    private final String message;

    /**
     * The HTTP status of the exception.
     */
    private final HttpStatus httpStatus;

    /**
     * The timestamp of when the exception was thrown.
     */
    private final ZonedDateTime timeStamp;

}
