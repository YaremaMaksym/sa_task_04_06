package yaremax.com.pb_task_24_04.exception;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Builder
public record ApiException (
        String message,
        HttpStatus httpStatus,
        ZonedDateTime timeStamp) {
}
