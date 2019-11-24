package pl.techdra.models.db;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="login_history")
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pms_id", nullable = false)
    private PMS pms;

    @Column(name = "logged", nullable = false)
    private LocalDateTime logged;

    public LoginHistory() {}


    public LoginHistory(PMS pms, LocalDateTime logged) {
        this.pms = pms;
        this.logged = logged;
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

    public LocalDateTime getLogged() {
        return logged;
    }

    public void setLogged(LocalDateTime logged) {
        this.logged = logged;
    }
}
