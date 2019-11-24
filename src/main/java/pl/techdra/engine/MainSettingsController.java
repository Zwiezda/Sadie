package pl.techdra.engine;

import pl.techdra.engine.generic.GenericSettingsController;
import pl.techdra.models.settings.Settings;


/**
 * @author Kuba
 * @version 1.0
 *
 * Class designed to manage  on the main application settings
 */
public final class MainSettingsController extends GenericSettingsController<Settings> {
    /* Singleton implementation */

    private static MainSettingsController instance; //Singleton instance

    public static synchronized MainSettingsController getInstance()  {
        if (instance == null) {
            instance = new MainSettingsController();
        }

        return instance;
    }

    /* SettingsController implementation */

    private Settings generateDefaultConfig() {
        return new Settings();
    }

    private MainSettingsController() {
        super(Settings.class,"Settings.json");

        if (! settingsFileExist() ) {
            saveConfig( generateDefaultConfig() );
        }
    }

    /**
     *
     * @return get application Settings object
     */
    @Override
    public Settings getConfig()   {
        return super.getConfig();
    }

}
