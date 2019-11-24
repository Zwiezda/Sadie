package pl.techdra.engine.exceptions;

import pl.techdra.models.db.PMS;

public class PluginException extends Exception {
    private PMS pms;

    public PluginException(String message, PMS pms) {
        super(message);
        this.pms = pms;
    }

    public PMS getPms() {
        return pms;
    }
}
