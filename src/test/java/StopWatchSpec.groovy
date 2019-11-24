import pl.techdra.engine.exceptions.UnpushedTimeException
import pl.techdra.models.StopWatch
import pl.techdra.models.db.PMS
import pl.techdra.models.db.TimeCache
import pl.techdra.models.settings.AuthType
import spock.lang.Specification

import java.time.LocalDateTime

class StopWatchSpec extends Specification {
    def "Start StopWatch for 2 seconds"() {
        when:
        def stopWatch = StopWatch.getInstance()
        stopWatch.resetStopWatch(null, null)
        def examplePMS = new PMS("test", "test", "test", AuthType.API_KEY)
        stopWatch.resetStopWatch(examplePMS, "12345")
        stopWatch.start()
        Thread.sleep(2000)
        stopWatch.stop()
        def timeCacheObject = stopWatch.getTimeCacheObject(false)

        then:
        stopWatch.getCalculatedSeconds() == 2
        timeCacheObject.getPms() == examplePMS
        timeCacheObject.getIssueID().equals("12345")
        timeCacheObject.getReportedTime() == 2
    }

    def "Start StopWatch for 2 seconds, stop it, and resume for 3 seconds"() {
        when:
        def stopWatch = StopWatch.getInstance()
        stopWatch.resetStopWatch(null, null)
        def examplePMS = new PMS("test", "test", "test", AuthType.API_KEY)
        stopWatch.resetStopWatch(examplePMS, "12345")
        stopWatch.start()
        Thread.sleep(2000)
        stopWatch.stop()

        stopWatch.start()
        Thread.sleep(3000)
        stopWatch.stop()
        def timeCacheObject = stopWatch.getTimeCacheObject(false)

        then:
        stopWatch.getCalculatedSeconds() == 5
        timeCacheObject.getPms() == examplePMS
        timeCacheObject.getIssueID().equals("12345")
        timeCacheObject.getReportedTime() == 5
    }

    def "Get current time from StopWatch"() {
        when:
        def stopWatch = StopWatch.getInstance()
        stopWatch.resetStopWatch(null, null)
        def examplePMS = new PMS("test", "test", "test", AuthType.API_KEY)
        stopWatch.resetStopWatch(examplePMS, "12345")
        stopWatch.start()
        Thread.sleep(1000)
        def time1 = stopWatch.getCurrentTime()
        Thread.sleep(1000)
        def time2 = stopWatch.getCurrentTime()
        Thread.sleep(1000)
        stopWatch.stop()
        def timeCacheObject = stopWatch.getTimeCacheObject(false)

        then:
        stopWatch.getCalculatedSeconds() == 3
        time1 == 1
        time2 == 2
        timeCacheObject.getPms() == examplePMS
        timeCacheObject.getIssueID().equals("12345")
        timeCacheObject.getReportedTime() == 3
    }

    def "Load interrupted TimeCache object and resume"() {
        when:
        def stopWatch = StopWatch.getInstance()
        stopWatch.resetStopWatch(null, null)
        def examplePMS = new PMS("test", "test", "test", AuthType.API_KEY)

        def oldTimeCacheObject = new TimeCache(examplePMS, "12345", 2, LocalDateTime.now(), "Interrupted")
        oldTimeCacheObject.setInterrupted(true)
        stopWatch.loadEarlierTime(oldTimeCacheObject)
        stopWatch.start()
        Thread.sleep(3000)
        stopWatch.stop()

        def timeCacheObject = stopWatch.getTimeCacheObject(false)

        then:
        stopWatch.getCalculatedSeconds() == 5
        timeCacheObject.getPms() == examplePMS
        timeCacheObject.getIssueID().equals("12345")
        timeCacheObject.getReportedTime() == 5
        timeCacheObject.getComment().equals("Interrupted")
    }

    def "Load interrupted TimeCache if StopWatch is busy"() {
        when:
        def stopWatch = StopWatch.getInstance()
        stopWatch.resetStopWatch(null, null)
        def examplePMS = new PMS("test", "test", "test", AuthType.API_KEY)

        def oldTimeCacheObject = new TimeCache(examplePMS, "12345", 2, LocalDateTime.now(), "Interrupted")
        oldTimeCacheObject.setInterrupted(true)
        stopWatch.start()
        Thread.sleep(1000)
        stopWatch.loadEarlierTime(oldTimeCacheObject)

        then:
        thrown(UnpushedTimeException)
    }


    def "Busy test"() {
        when:
        def stopWatch = StopWatch.getInstance()
        stopWatch.resetStopWatch(null, null)
        def examplePMS = new PMS("test", "test", "test", AuthType.API_KEY)
        stopWatch.resetStopWatch(examplePMS, "12345")
        stopWatch.start()
        Thread.sleep(2000)
        def state1 = stopWatch.isBusy()
        stopWatch.stop()
        def state2 = stopWatch.isBusy()
        stopWatch.resetStopWatch(examplePMS, "12345")
        def state3 = stopWatch.isBusy()

        then:
        state1
        state2
        !state3
    }


}
