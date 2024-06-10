package yaremax.com.sa_task_04_06.exception.custom;

/**
 * Exception thrown when the data passed to the method is invalid.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
public class InvalidDataException extends RuntimeException {
    /**
     * Constructs a new {@code InvalidDataException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public InvalidDataException(String message) {
        super(message);
    }
}
