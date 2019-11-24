package pl.techdra.engine.exceptions;

public class InvalidDatabaseConnectionParametersException extends RuntimeException {
    public InvalidDatabaseConnectionParametersException(String message) {
        super(message);
    }
}
