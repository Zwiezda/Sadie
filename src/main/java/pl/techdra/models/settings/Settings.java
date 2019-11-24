package pl.techdra.models.settings;

import java.io.Serializable;

public class Settings implements Serializable {
    private App appConfig;

    public Settings() {
        appConfig = new App();
    }


    public App getAppConfig() {
        return new App(appConfig);
    }

    public void setAppConfig(App appConfig) {
        this.appConfig = new App(appConfig);
    }

}
