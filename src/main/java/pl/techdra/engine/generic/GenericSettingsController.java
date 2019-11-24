package pl.techdra.engine.generic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.techdra.engine.events.Observable;
import pl.techdra.engine.events.OnSettingsChangeEvent;
import pl.techdra.engine.exceptions.InvalidConfigFormatException;

import java.io.*;

/**
 * @author Kuba
 * @version 1.0
 *
 * Generic class providing methods for saving and reading settings settings in JSON format
 */

public class GenericSettingsController<T extends Serializable> {
    private String settingsFileName = "";
    protected Gson gsonObject;
    private final Class<T> settingsClass;

    public Observable<OnSettingsChangeEvent<T>> settingsChangeEventInvoker = new Observable<OnSettingsChangeEvent<T>>();

    /**
     * Primary class constructor.
     * The argument settingsFileName specify a path to settings JSON file
     *
     * @param settingsClass Settings class object
     * @param settingsFileName path to settings JSON file
     */
    public GenericSettingsController(Class<T> settingsClass, String settingsFileName) {
        this.settingsClass = settingsClass;
        this.settingsFileName = settingsFileName;
        gsonObject = new GsonBuilder().setPrettyPrinting().create();
    }


    /**
     * Checks if the settings file exists
     * @return true if settings file exist
     */
    protected final boolean settingsFileExist() {
        File configFile = new File(this.settingsFileName);
        return configFile.exists() && configFile.canWrite();
    }


    /**
     * Return path to the settings file
     * @return Settings path
     */
    public final String getSettingsFileName() {
        return settingsFileName;
    }


    /**
     * Parse settings file and return T class instance with settings
     * @return T class instance with settings
     * @throws InvalidConfigFormatException if settings file not exist or content can't be parsed and casted to T class
     */
    public T getConfig() throws InvalidConfigFormatException {
        try {
            Reader settingsFile = new FileReader( getSettingsFileName() );
            T result = gsonObject.fromJson(settingsFile, settingsClass);
            settingsFile.close();

            return result;
        } catch (Exception e) {
            throw new InvalidConfigFormatException("Can't read config file: " + e.getMessage() );
        }

    }


    /**
     *
     * @param settings A T object containing settings to save
     * @throws InvalidConfigFormatException If write permission is missing or the object is malformed
     */
    public synchronized void saveConfig(T settings) throws InvalidConfigFormatException {
        try {
            Writer settingsFile = new FileWriter(settingsFileName);
            gsonObject.toJson(settings, settingsFile);
            settingsFile.flush();
            settingsFile.close();

            settingsChangeEventInvoker.update(new OnSettingsChangeEvent<T>(this, settings));
        } catch (Exception e) {
            throw new InvalidConfigFormatException("Can't save config file: " + e.getMessage() );
        }


    }

}
