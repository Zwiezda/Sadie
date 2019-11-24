package pl.techdra.engine.events;

public class OnSettingsChangeEvent<T> extends GenericEvent<T> {
    public OnSettingsChangeEvent(Object source) {
        super(source);
    }

    public OnSettingsChangeEvent(Object source, T object) {
        super(source, object);
    }

    public OnSettingsChangeEvent(Object source, T object, String message) {
        super(source, object, message);
    }
}
