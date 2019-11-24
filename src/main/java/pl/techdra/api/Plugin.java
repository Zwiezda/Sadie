package pl.techdra.api;
import javafx.stage.Stage;


/**
 * Interface for all plugins supported by program
 */
public interface Plugin {

    /**
     * Invoked when plugin is loaded
     */
    void Initialize();

    /**
     * Get plugin name
     * @return plugin name
     */
    String getName();

    /**
     * Show the issue management window
     * @param pmsIssue Notification for which the window should be displayed
     * @see PMSIssue
     * @param pmsUser User data
     * @see PMSUser
     *
     * @return window instance
     * @see Stage
     */
    Stage getIssueWindow(PMSIssue pmsIssue, PMSUser pmsUser);

    /**
     * Get a PMSManage object to control the project management system
     * @return PMSManage object
     */
    PMSManage getPMSManage();


    /**
     * When program is shutting down
     */
    void Free();
}
