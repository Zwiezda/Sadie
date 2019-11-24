package pl.techdra.engine.events;

@FunctionalInterface
public interface EventHandler<T extends GenericEvent> extends java.util.EventListener {
    void handle(T event);
}
