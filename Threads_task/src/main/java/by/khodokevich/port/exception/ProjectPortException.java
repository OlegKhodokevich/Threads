package by.khodokevich.port.exception;

public class ProjectPortException extends Exception {
    public ProjectPortException() {
    }

    public ProjectPortException(String message) {
        super(message);
    }

    public ProjectPortException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectPortException(Throwable cause) {
        super(cause);
    }
}
