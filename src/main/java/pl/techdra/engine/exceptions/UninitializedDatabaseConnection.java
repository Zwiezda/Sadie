package pl.techdra.engine.exceptions;

public class UninitializedDatabaseConnection extends RuntimeException {
    public UninitializedDatabaseConnection(String message) {
        super(message);
    }
}
