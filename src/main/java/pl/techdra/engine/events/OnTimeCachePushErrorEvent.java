package pl.techdra.engine.events;

import pl.techdra.engine.exceptions.PluginException;

public class OnTimeCachePushErrorEvent extends GenericEvent<PluginException> {
    public OnTimeCachePushErrorEvent(Object source) {
        super(source);
    }

    public OnTimeCachePushErrorEvent(Object source, PluginException object) {
        super(source, object);
    }

    public OnTimeCachePushErrorEvent(Object source, PluginException object, String message) {
        super(source, object, message);
    }
}
