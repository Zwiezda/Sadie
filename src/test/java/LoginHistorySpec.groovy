import org.hibernate.HibernateException
import pl.techdra.engine.HibernateController
import pl.techdra.engine.LoginHistoryController
import pl.techdra.engine.LoginHistoryController
import pl.techdra.models.db.PMS
import pl.techdra.models.settings.AuthType
import spock.lang.Specification
import java.time.LocalDateTime

class LoginHistorySpec extends Specification {
    def "Add login date to log without existing PMS"() {
        given:
        HibernateController.Init("test1234")
        when:
        def lhc = LoginHistoryController.getInstance();
        PMS pms = new PMS("test", "test", "test", AuthType.API_KEY)
        lhc.loginUser(pms)
        then:
        thrown HibernateException
    }
}
