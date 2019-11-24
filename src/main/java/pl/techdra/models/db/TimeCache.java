package pl.techdra.models.db;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="time_cache")
public class TimeCache {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pms_id", nullable = false)
    private PMS pms;

    @Column(name = "reported_time", nullable = false)
    private long reportedTime;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "issue_id", nullable = false)
    private String issueID;

    @Column(name= "interrupted")
    private boolean interrupted;

    @Column(name= "pushed")
    private LocalDateTime pushed;

    @Column(name= "comment")
    private String comment;


    public TimeCache() {
    }

    public TimeCache(PMS pms, String issueID, long reportedTime, LocalDateTime date, String comment) {
        this.pms = pms;
        this.issueID = issueID;
        this.reportedTime = reportedTime;
        this.date = date;
        this.comment = comment;
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

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public long getReportedTime() {
        return reportedTime;
    }

    public void setReportedTime(long reportedTime) {
        this.reportedTime = reportedTime;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public LocalDateTime getPushed() {
        return pushed;
    }

    public void setPushed(LocalDateTime pushed) {
        this.pushed = pushed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
