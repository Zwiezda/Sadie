package pl.techdra.engine.listeners;

import pl.techdra.engine.CacheController;
import pl.techdra.engine.InputController;
import pl.techdra.engine.MainSettingsController;
import pl.techdra.engine.events.EventHandler;
import pl.techdra.engine.events.OnSettingsChangeEvent;
import pl.techdra.models.settings.Settings;

public class MainSettingsListener {
    private static MainSettingsListener instance;


    public static synchronized MainSettingsListener getInstance()  {
        if (instance == null) {
            instance = new MainSettingsListener();
        }

        return instance;
    }

    private MainSettingsListener() {
        MainSettingsController mainSettingsController = MainSettingsController.getInstance();
        mainSettingsController.settingsChangeEventInvoker.addListener(
                event -> {
                    CacheController.getInstance().setTimerDelay(event.getObject().getAppConfig().getTaskPushTimer());

                    InputController inputController = InputController.getInstance();

                    inputController.setMaxInactivity(event.getObject().getAppConfig().getAutoFreezeTimer());
                    inputController.setShortcuts(event.getObject().getAppConfig().getShortcuts());
                }
        );
    }


}
