package pl.techdra.engine;

import pl.techdra.api.Plugin;

import pl.techdra.engine.generic.GenericDAO;
import pl.techdra.models.db.PMS;


/**
 * @author Kuba
 * @version 1.0
 */
public class PMSController extends GenericDAO<PMS> {
    /* Singleton implementation */

    private static PMSController instance; //Singleton instance
    /**
     * get PMSController instance
     * @return PMSController instance
     */
    public static synchronized PMSController getInstance()  {
        if (instance == null) {
            instance = new PMSController();
        }

        return instance;
    }

    /* SettingsController implementation */

    private PMSController() {
        super(PMS.class);
    }

    /**
     * Checks if a connection can be made using the provided data
     * @param pms pms object for connection test
     * @see PMS
     * @throws Exception if pms config is incorrect
     */
    public void testPMSConfig(PMS pms) {
        try {
            PluginManager pluginManager = PluginManager.getInstance();
            Plugin plugin = pluginManager.getPlugin(pms.getPlugin());

            plugin.getPMSManage().testConfig(pms);
        } catch (Exception e) {
            log.error(e);
        }

    }
}
