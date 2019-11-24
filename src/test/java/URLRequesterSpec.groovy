import pl.techdra.helpers.RequstMethod
import pl.techdra.helpers.URLRequester
import spock.lang.Specification

class URLRequesterSpec extends Specification  {
    def "Get google site"() {
        when:
        def requester = new URLRequester()
        def result = requester.makeRequest("https://www.google.pl", RequstMethod.GET, new HashMap<String, String>(), new HashMap<String, String>(), null)
        then:
        result.statusCode == 200
        !result.parseAsString().isEmpty()
    }
}
