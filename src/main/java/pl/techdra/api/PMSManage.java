package pl.techdra.api;
import pl.techdra.models.db.PMS;
import pl.techdra.models.db.TimeCache;

import java.util.List;

public interface PMSManage {
    void testConfig(PMS pmsConfig);

    PMSUser getUserInfo(PMS pmsConfig);

    PMSIssue getIssue(PMS pmsConfig, String issueId);

    List<PMSIssue> getIssues(PMS pmsConfig);

    String raportTime(TimeCache timeCache);

    String updateIssue(PMS pmsConfig, PMSIssue issue);

}
