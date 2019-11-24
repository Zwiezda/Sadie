package pl.techdra.engine.exceptions;

public class InterruptedTaskAlreadyExistException extends RuntimeException {
    public InterruptedTaskAlreadyExistException(String message) {
        super(message);
    }
}
