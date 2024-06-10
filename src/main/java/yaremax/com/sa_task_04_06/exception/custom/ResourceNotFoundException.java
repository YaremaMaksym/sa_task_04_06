package yaremax.com.sa_task_04_06.exception.custom;

/**
 * Exception thrown when a resource is not found.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code ResourceNotFoundException} with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     * @param cause The cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
