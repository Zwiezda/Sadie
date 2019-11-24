package pl.techdra.engine.events;

import java.util.EventObject;

public class GenericEvent<T> extends EventObject {
    protected T object;
    protected String message;

    public GenericEvent(Object source) {
        super(source);
    }

    public GenericEvent(Object source, T object) {
        super(source);
        this.object = object;
    }

    public GenericEvent(Object source, T object, String message) {
        super(source);
        this.object = object;
        this.message = message;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
