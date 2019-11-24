package pl.techdra.engine.events;

import pl.techdra.models.db.TimeCache;

public class OnTimeCachePushEvent extends GenericEvent<TimeCache> {
    public OnTimeCachePushEvent(Object source) {
        super(source);
    }

    public OnTimeCachePushEvent(Object source, TimeCache object) {
        super(source, object);
    }

    public OnTimeCachePushEvent(Object source, TimeCache object, String message) {
        super(source, object, message);
    }
}
