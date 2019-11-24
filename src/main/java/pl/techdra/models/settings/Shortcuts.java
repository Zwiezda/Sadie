package pl.techdra.models.settings;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Shortcuts implements Serializable {
    private final HashMap<String, HashSet<Integer>> shortcuts;

    public Shortcuts() {
        shortcuts = new HashMap<String, HashSet<Integer>>();
    }


    public Shortcuts(Shortcuts source) {
        this();

        for (String actionName: source.getActionNames()) {
            this.addShortcut(actionName, source.getShortcut(actionName));
        }
    }


    public HashSet<Integer> getShortcut(String actionName) {
        return new HashSet<>(shortcuts.get(actionName));
    }

    public Set<String> getActionNames() {
        return shortcuts.keySet();
    }


    public boolean containShortcut(String actionName) {
        return shortcuts.containsKey(actionName);
    }

    public void addShortcut(String actionName, HashSet<Integer> shortcut ) {
        shortcuts.put(actionName, new HashSet<>(shortcut));
    }

    public ConcurrentHashMap<HashSet<Integer>, String> reverseShortcuts() {
        ConcurrentHashMap<HashSet<Integer>, String> result = new ConcurrentHashMap<>();

        for (String actionName : shortcuts.keySet() ) {
            result.put( new HashSet<>(shortcuts.get(actionName)), actionName );
        }

        return result;
    }




}
