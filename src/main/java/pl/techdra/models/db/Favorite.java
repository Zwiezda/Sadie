package pl.techdra.models.db;
import javax.persistence.*;

@Entity
@Table(name="favorite")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pms_id", nullable = false)
    private PMS pms;

    @Column(name = "issue_id")
    private String issueId;


    public Favorite() { }

    public Favorite(PMS pms, String issueId) {
        this.pms = pms;
        this.issueId = issueId;
    }

    public int getId() {
        return id;
    }

    public PMS getPms() {
        return pms;
    }

    public void setPms(PMS pms) {
        this.pms = pms;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    @Override
    public String toString() {
        return String.format("Favorite{ id: %s, plugin: %s, URL: %s }", getId(), getPms().getPlugin(), getPms().getApiUrl() );
    }
}