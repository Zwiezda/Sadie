package pl.techdra.engine.exceptions;

public class NoShortcutException extends Exception {
    public NoShortcutException(String shortcutName) {
        super(String.format("No shortcut named: %s", shortcutName));
    }
}
