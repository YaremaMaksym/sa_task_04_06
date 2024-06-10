package yaremax.com.sa_task_04_06.exception.custom;

/**
 * Exception thrown when trying to create a resource that already exists.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
public class DuplicateResourceException extends RuntimeException {
    /**
     * Constructs a new {@code DuplicateResourceException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code DuplicateResourceException} with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     * @param cause The cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}

