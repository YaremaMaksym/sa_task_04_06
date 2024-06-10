package yaremax.com.sa_task_04_06.exception;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.security.core.AuthenticationException;
import yaremax.com.sa_task_04_06.exception.custom.*;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 * Global exception handler class. This class is responsible for handling all kinds of exceptions that may occur
 * during the execution of the application. It implements {@link ResponseEntityExceptionHandler} and overrides
 * methods to handle specific exceptions.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String LOGGER_MESSAGE_PREFIX = "⚠⚠⚠ Exception was thrown with message: ";

    /**
     * Builds an {@link ApiException} object based on the provided exception and HTTP status code.
     *
     * @param ex          the exception to build the {@link ApiException} object from
     * @param httpStatus  the HTTP status code to set in the {@link ApiException} object
     * @return            the built {@link ApiException} object
     */
    private ApiException buildApiException(RuntimeException ex, HttpStatus httpStatus) {
        return ApiException.builder()
                .httpStatus(httpStatus)
                .message("(" + ex.getClass().getSimpleName() + ") " + ex.getMessage())
                .timeStamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();
    }

    /**
     * Handles the exception by building an {@link ApiException} object and returning it as a response entity.
     * The HTTP status code of the response is taken from the provided exception.
     *
     * @param ex            the exception to handle
     * @param request       the HTTP servlet request
     * @param httpStatus    the HTTP status code to set in the response entity
     * @return              the response entity containing the {@link ApiException} object
     */
    private ResponseEntity<Object> handleException(RuntimeException ex, HttpServletRequest request, HttpStatus httpStatus) {
        ApiException apiException = buildApiException(ex, httpStatus);
        LOGGER.error(LOGGER_MESSAGE_PREFIX + "{}", ex.getMessage());
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }


    /**
     * Handles {@link InvalidDataException} by calling {@link #handleException(RuntimeException, HttpServletRequest, HttpStatus)}
     * with the provided exception, request, and {@link HttpStatus#BAD_REQUEST}.
     *
     * @param ex            the exception to handle
     * @param request       the HTTP servlet request
     * @return              the response entity containing the {@link ApiException} object
     */
    @ExceptionHandler(value = {InvalidDataException.class})
    public ResponseEntity<Object> handleBadRequestExceptions(RuntimeException ex, HttpServletRequest request){
        return handleException(ex, request, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link IllegalStateException}, {@link NullPointerException}, and {@link IllegalArgumentException}
     * by calling {@link #handleException(RuntimeException, HttpServletRequest, HttpStatus)} with the provided
     * exception, request, and {@link HttpStatus#INTERNAL_SERVER_ERROR}.
     *
     * @param ex            the exception to handle
     * @param request       the HTTP servlet request
     * @return              the response entity containing the {@link ApiException} object
     */
    @ExceptionHandler(value = {IllegalStateException.class, NullPointerException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleInternalServerErrorExceptions(RuntimeException ex, HttpServletRequest request){
        return handleException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles {@link DuplicateResourceException} by calling {@link #handleException(RuntimeException, HttpServletRequest, HttpStatus)}
     * with the provided exception, request, and {@link HttpStatus#CONFLICT}.
     *
     * @param ex            the exception to handle
     * @param request       the HTTP servlet request
     * @return              the response entity containing the {@link ApiException} object
     */
    @ExceptionHandler(value = {DuplicateResourceException.class})
    public ResponseEntity<Object> handleConflictExceptions(RuntimeException ex, HttpServletRequest request){
        return handleException(ex, request, HttpStatus.CONFLICT);
    }

    /**
     * Handles {@link ResourceNotFoundException} by calling {@link #handleException(RuntimeException, HttpServletRequest, HttpStatus)}
     * with the provided exception, request, and {@link HttpStatus#NOT_FOUND}.
     *
     * @param ex            the exception to handle
     * @param request       the HTTP servlet request
     * @return              the response entity containing the {@link ApiException} object
     */
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundExceptions(RuntimeException ex, HttpServletRequest request){
        return handleException(ex, request, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link AuthenticationException} by calling {@link #handleException(RuntimeException, HttpServletRequest, HttpStatus)}
     * with the provided exception, request, and {@link HttpStatus#UNAUTHORIZED}.
     *
     * @param ex            the exception to handle
     * @param request       the HTTP servlet request
     * @return              the response entity containing the {@link ApiException} object
     */
    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<Object> handleUnauthorizedExceptions(RuntimeException ex, HttpServletRequest request){
        return handleException(ex, request, HttpStatus.UNAUTHORIZED);
    }
}
