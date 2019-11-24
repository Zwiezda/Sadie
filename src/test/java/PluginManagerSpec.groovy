import pl.techdra.engine.PluginManager
import spock.lang.Specification

class PluginManagerSpec extends Specification {
    def "Get plugins list"() {
        when:
        def pm = new PluginManager()
        def plugins = pm.getPluginNames()
        println(plugins)
        then:
        ! plugins.isEmpty()
    }

    def "Load plugin"() {
        when:
        def pm = new PluginManager()
        def plugin = pm.getPlugin("RedminePlugin")
        then:
        plugin.getName().equals("RedminePlugin")
    }
}
