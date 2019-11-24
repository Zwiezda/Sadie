package pl.techdra.engine.events;

import java.util.HashSet;
import java.util.Set;

public class Observable<T extends GenericEvent> {
    private Set<EventHandler<T>> observers = new HashSet<>();
    public void addListener(EventHandler<T> eventHandler) {
        observers.add(eventHandler);
    }
    public void removeListener(EventHandler<T> eventHandler) {
        observers.remove(eventHandler);
    }

    public void update(T event) {
        for (EventHandler<T> eh: observers) {
            eh.handle(event);
        }
    };
}
