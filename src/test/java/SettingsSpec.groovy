import pl.techdra.engine.events.EventHandler
import pl.techdra.engine.events.OnSettingsChangeEvent
import pl.techdra.models.settings.Settings
import spock.lang.*
import pl.techdra.engine.*
import java.nio.file.*

class SettingsSpec extends Specification {
    def "Initialize SettingsControler. After this should be created default config file"() {
        given:
        Files.deleteIfExists(Paths.get("Settings.json"))
        when:
        def sc = MainSettingsController.getInstance();
        def settings = sc.getConfig()
        then:
        settings.getAppConfig().getLanguage().equals('en')
        settings.getAppConfig().getReminder() == 15
        settings.getAppConfig().getAutoFreezeTimer() == 10
        settings.getAppConfig().isShowNotifications()
    }


    def "Change language to russian and reminder to 30 minutes"() {
        when:
        def eventRun = false

        def EventHandler<OnSettingsChangeEvent<Settings>> onSettingsChangeEventEventHandler = new EventHandler<OnSettingsChangeEvent<Settings>>() {
            @Override
            void handle(OnSettingsChangeEvent<Settings> event) {
                eventRun = true
                println("Hello from event :)")
            }
        };


        def sc = MainSettingsController.getInstance();
        sc.settingsChangeEventInvoker.addListener(onSettingsChangeEventEventHandler)

        def settings = sc.getConfig()
        def appConfig = settings.getAppConfig()
        appConfig.setLanguage("ru")
        appConfig.setReminder(30)
        settings.setAppConfig( appConfig )
        sc.saveConfig(settings)

        settings = sc.getConfig()
        then:
        settings.getAppConfig().getLanguage().equals('ru')
        settings.getAppConfig().getReminder() == 30
        eventRun

    }

    def "Settings return copy of shortcuts array"() {
        when:
        def sc = MainSettingsController.getInstance();
        def appConfig = sc.getConfig().getAppConfig()

        def shortcuts1 = appConfig.getShortcuts()
        def shortcuts2 = appConfig.getShortcuts()
        then:
        shortcuts1 != shortcuts2
    }




}
