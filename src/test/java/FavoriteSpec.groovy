import org.hibernate.HibernateException
import pl.techdra.engine.FavoriteController
import pl.techdra.engine.HibernateController
import pl.techdra.models.db.Favorite
import pl.techdra.models.db.PMS
import pl.techdra.models.settings.AuthType
import spock.lang.Specification

class FavoriteSpec extends Specification {
    def "Add favorite issue without existing PMS"() {
        given:
        HibernateController.Init("test1234")
        when:
        def fc = FavoriteController.getInstance();
        def favorite = new Favorite(new PMS("test", "test", "test", AuthType.API_KEY), "123456" )
        fc.saveOrUpdate(favorite)
        then:
        thrown HibernateException
    }


    def "Add favorite issue with existing PMS"() {
        when:
        def fc = FavoriteController.getInstance();
        def favorite = new Favorite(new PMS("test", "test", "test", AuthType.API_KEY), "123456" )
        fc.saveOrUpdate(favorite)
        then:
        thrown HibernateException
    }


}
