package pl.techdra.models.db;

import pl.techdra.models.settings.AuthType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="PMS")
public class PMS  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "api_url", nullable = false)
    private String apiUrl = "";

    @Column(name = "api_key")
    private String apiKey = "";

    @Column(name = "login")
    private String login = "";

    @Column(name = "password")
    private String password = "";

    @Column(name = "plugin", nullable = false)
    private String plugin = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false)
    private AuthType authType = AuthType.API_KEY;


    @OneToMany(mappedBy = "pms", cascade = CascadeType.ALL)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "pms", cascade = CascadeType.ALL)
    private List<LoginHistory> historyList;

    @OneToMany(mappedBy = "pms", cascade = CascadeType.ALL)
    private List<TimeCache> timeCaches;


    public PMS() {
    }

    public PMS(String apiUrl, String apiKey, String plugin, AuthType authType) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.plugin = plugin;
        this.authType = authType;
    }

    public int getId() {
        return id;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }

    public List<LoginHistory> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<LoginHistory> historyList) {
        this.historyList = historyList;
    }

    public List<TimeCache> getTimeCaches() {
        return timeCaches;
    }

    public void setTimeCaches(List<TimeCache> timeCaches) {
        this.timeCaches = timeCaches;
    }

    @Override
    public String toString() {
        return String.format("PMS{ id: %d, plugin: %s, URL: %s, Login: %s }", getId(), getPlugin(), getApiUrl(), getLogin() );
    }
}
