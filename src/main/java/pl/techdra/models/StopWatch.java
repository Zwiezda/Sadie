package pl.techdra.models;

import pl.techdra.engine.exceptions.UnpushedTimeException;
import pl.techdra.models.db.PMS;
import pl.techdra.models.db.TimeCache;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Kuba
 * @version 1.0
 *
 *
 * A class designed for counting time for a specific issue
 */
public class StopWatch {
    private long calculatedSeconds;

    private LocalDateTime starTime;
    private LocalDateTime stopTime;

    private PMS pms;
    private String issueID;
    private String comment;

    private static StopWatch instance; //Singleton instance

    private StopWatch() {
    }


    /**
     * Get StopWatch instance
     * @return StopWatch instance
     */

    public static synchronized StopWatch getInstance() {
        if (instance == null) {
            instance = new StopWatch();
        }

        return instance;
    }

    /**
     * Reset stopwatch and set new pms as IssueID
     * @param pms pms with which time will be counted
     * @see PMS
     * @param issueID issueID from specified  pms with which time will be counted
     */
    public void resetStopWatch(PMS pms, String issueID) {
        this.pms = pms;
        this.issueID = issueID;

        starTime = null;
        stopTime = null;
        comment = "";

        calculatedSeconds = 0;
    }

    /**
     * Return true if inner time wasn't dump yet
     * @return true if inner time wasn't dump yet
     */
    public boolean isBusy() {
        if (starTime != null || stopTime != null || calculatedSeconds > 0) {
            return true;
        }
        return false;
    }


    /**
     * Load earlier, unfinished time
     * @see TimeCache
     * @param timeCache unfinished TimeCache object
     * @throws UnpushedTimeException if stopwatch is busy
     */
    public void loadEarlierTime(TimeCache timeCache) throws UnpushedTimeException {
        if ( isBusy() ) {
            throw new UnpushedTimeException(String.format("StopWatch contain unpushed time - %d seconds. Please reset stopWatch", calculatedSeconds));
        }

        pms = timeCache.getPms();
        issueID = timeCache.getIssueID();
        comment = timeCache.getComment();
        calculatedSeconds = timeCache.getReportedTime();

    }


    /**
     * Start counting time
     */
    public void start() {
        starTime = LocalDateTime.now();
        stopTime = null;
    }

    /**
     * Stop counting time
     */
    public void stop() {
        if (! isBusy() ) {
            throw new NullPointerException("StopWatch is not started. Please start StopWatch");
        }

        stopTime = LocalDateTime.now();
        calculatedSeconds += starTime.until(stopTime, ChronoUnit.SECONDS);
    }


    /**
     * Get TimeCache object containing calculated time
     * @param interrupted if it's interruption
     * @return TimeCache object containing calculated time
     * @see TimeCache
     */

    public TimeCache getTimeCacheObject(boolean interrupted) {
        if (pms == null) {
            throw new NullPointerException("PMS object is undefined");
        }

        if (issueID == null || issueID.equals("")) {
            throw new NullPointerException("issueID is undefined");
        }

        if (starTime == null) {
            throw new NullPointerException("startTime is undefined, Please start StopWatch");
        }

        if (stopTime == null || calculatedSeconds == 0)  {
            throw new NullPointerException("stopTime is undefined. Please stop StopWatch");
        }

        TimeCache result = new TimeCache(pms, issueID, calculatedSeconds, starTime, comment);
        result.setInterrupted(interrupted);

        return result;

    }

    /**
     * Get calculated seconds
     * @return counted time (in seconds)
     */

    public long getCalculatedSeconds() {
        return calculatedSeconds;
    }


    /**
     * Manual time set
     * @param calculatedSeconds new time value (in seconds)
     */
    public void setCalculatedSeconds(long calculatedSeconds) {
        this.calculatedSeconds = calculatedSeconds;
    }


    /**
     * Get current calculated seconds
     * @return calculated seconds
     */
    public long getCurrentTime() {
        if (starTime == null) {
            throw new NullPointerException("startTime is undefined, Please start StopWatch");
        }

        return starTime.until(LocalDateTime.now(), ChronoUnit.SECONDS);
    }


}
