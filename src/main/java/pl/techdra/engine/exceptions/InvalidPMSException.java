package pl.techdra.engine.exceptions;

import pl.techdra.models.db.PMS;

public class InvalidPMSException extends Exception {
    public InvalidPMSException(PMS pms, String msg) {
        super(String.format("Invalid PMS! PMS: %s. Message: %s", pms.toString(), msg));
    }
}
