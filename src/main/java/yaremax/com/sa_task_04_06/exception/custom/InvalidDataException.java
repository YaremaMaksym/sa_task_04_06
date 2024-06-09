package yaremax.com.sa_task_04_06.exception.custom;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}
