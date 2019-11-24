package pl.techdra.engine;

import org.reflections.Reflections;
import pl.techdra.api.Plugin;
import pl.techdra.engine.exceptions.InvalidPluginPathException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * @author Kuba
 * @version 1.0
 *
 * Class designed for loading and managing plugins
 */

public final class PluginManager {
    /* Singleton implementation */

    private static PluginManager instance; //Singleton instance
    /**
     * get PluginManager instance
     * @return PluginManager instance
     */
    public static synchronized PluginManager getInstance()  {
        if (instance == null) {
            instance = new PluginManager();
        }

        return instance;
    }

    /* PluginManager implementation */



    private static String PLUGIN_PATH = "Plugins";
    private URLClassLoader classLoader;
    private HashMap<String, Plugin> plugins = new HashMap<>();
    private static Logger log = LogManager.getLogger( PluginManager.class );

    private PluginManager() throws InvalidPluginPathException {
        File pluginPath = new File(PLUGIN_PATH);
        try {
            URL url = pluginPath.toURI().toURL();
            URL[] urls = new URL[]{url};

            classLoader = new URLClassLoader(urls);
            ReloadAllPlugins();
        } catch (MalformedURLException e) {
            throw new InvalidPluginPathException(e.getMessage());
        }

    }

    /**
     *
     * @param className plugin class name
     * @return plugin instance
     * @throws Exception if any problems with loading plugins
     */
    public Plugin LoadPlugin(String className) throws Exception {
        Class cls = classLoader.loadClass(className);
        Plugin plugin = (Plugin) cls.newInstance();
        plugin.Initialize();    //Invoke plugin initialization

        return plugin;
    }

    /**
     * Reloads all plugins located in the plugins directory
     */
    public void ReloadAllPlugins() {
        HashSet<String> pluginNames = getPluginNames();
        plugins.clear();

        for (String pluginName: pluginNames) {
            try {
                plugins.put(pluginName, LoadPlugin(pluginName));
            } catch (Exception e) {
                log.error(String.format("Can't load plugin %s:", pluginName), e);
            }
        }
    }


    /**
     * Get all plugin names located in plugins directory
     * @return list of plugin's names
     */
    public HashSet<String> getPluginNames()  {
        HashSet<String> result = new HashSet<String>();

        Reflections reflections = new Reflections(classLoader);
        Set<Class<? extends Plugin>> subTypes = reflections.getSubTypesOf(Plugin.class);


        for (Class plugin: subTypes) {
            result.add(plugin.getName());
        }


        return result;
    }


    /**
     * Get specified plugin instance
     * @param pluginName name of the plugin whose instance we want to get
     *
     * @return specified plugin instance
     */

    public Plugin getPlugin(String pluginName) {
        return plugins.get(pluginName);
    }



}
