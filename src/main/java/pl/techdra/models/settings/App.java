package pl.techdra.models.settings;

import java.io.Serializable;
import java.util.*;

import org.jnativehook.keyboard.NativeKeyEvent;

public class App implements Serializable {
    private String language = "en";
    private int autoFreezeTimer = 10;
    private int taskPushTimer = 5;
    private int reminder = 15;
    private boolean showNotifications = true;
    private Shortcuts shortcuts = new Shortcuts() {{
       addShortcut("InvokeMainList", new HashSet<>(Arrays.asList(56, 29, NativeKeyEvent.VC_F12)) );
       addShortcut("StopTime", new HashSet<>(Arrays.asList(56, 29, NativeKeyEvent.VC_F10)) );
    }};

    public App() {
    }

    public App(App sourceObject) {  //Copy constructor
        language = sourceObject.getLanguage();
        autoFreezeTimer = sourceObject.getAutoFreezeTimer();
        reminder = sourceObject.getReminder();
        showNotifications = sourceObject.isShowNotifications();

        shortcuts = sourceObject.getShortcuts();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getTaskPushTimer() { return taskPushTimer; }

    public void setTaskPushTimer(int taskPushTimer) { this.taskPushTimer = taskPushTimer; }

    public int getAutoFreezeTimer() {
        return autoFreezeTimer;
    }

    public void setAutoFreezeTimer(int autoFreezeTimer) {
        this.autoFreezeTimer = autoFreezeTimer;
    }

    public int getReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }

    public boolean isShowNotifications() {
        return showNotifications;
    }

    public void setShowNotifications(boolean showNotifications) {
        this.showNotifications = showNotifications;
    }

    public Shortcuts getShortcuts() {
       return new Shortcuts(shortcuts);
    }

    public void setShortcuts(Shortcuts shortcuts) {
        this.shortcuts = new Shortcuts(shortcuts);
    }




}
