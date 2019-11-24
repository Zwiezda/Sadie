package pl.techdra.engine;

import org.hibernate.HibernateException;
import pl.techdra.api.PMSIssue;
import pl.techdra.api.Plugin;
import pl.techdra.engine.generic.GenericDAO;
import pl.techdra.models.db.Favorite;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Kuba
 * @version 1.0
 *
 * Class designed to handle your favorite issues.
 * It enables quick access to issues from various project management systems (PMS)
 */
public class FavoriteController extends GenericDAO<Favorite> {
    private static FavoriteController instance; //Singleton instance
    private final static short MAX_FAVORITE_ISSUES = 12;    //Max number of favorite tasks


    /**
     * Get a FavoriteController instance
     * @return FavoriteController instance
     */
    public static synchronized FavoriteController getInstance() {
        if (instance == null) {
            instance = new FavoriteController();
        }

        return instance;
    }

    private FavoriteController() {
        super(Favorite.class);
    }


    /**
     * List of all available tasks to which remote access is available
     * @return List of PMSIssues
     * @see PMSIssue
     */
    public List<PMSIssue> getAllFavoriteIssues() {
        ArrayList<PMSIssue> result = new ArrayList<>();

        try {
            PluginManager pluginManager = PluginManager.getInstance();
            ArrayList<Favorite> favorites = (ArrayList<Favorite>) getAll();

            for (Favorite favorite: favorites) {
                try {
                    Plugin plugin = pluginManager.getPlugin(favorite.getPms().getPlugin());  //Get dedicated plugin for this favorite issue
                    PMSIssue issue = plugin.getPMSManage().getIssue(favorite.getPms(), favorite.getIssueId());  //Try to get this issue
                    if (issue != null) {    //If no access or task does not exist
                        result.add(issue);
                    }

                } catch (Exception e) {
                    log.error(String.format("Error downloading favorite issue %s.", favorite.toString() ), e);
                }
            }

        } catch (Exception e) {
            log.error("Error downloading favorite issues.", e);
        }

        return result;
    }


    /**
     * Add or update favorite issue
     * @param favorite new or updated issue
     * @see Favorite
     */
    public void saveOrUpdate(Favorite favorite)  {
        if ( favorite.getPms().getId() < 1 ) {
            throw new HibernateException("Specified PMS not exist!");
        }

        if (favorite.getId() < 1 && getAll().size() == MAX_FAVORITE_ISSUES ) {
            throw new HibernateException("Too many favorites issues");
        }

        super.saveOrUpdate(favorite);
    }


}
