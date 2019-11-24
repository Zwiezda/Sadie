package pl.techdra.engine;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import pl.techdra.engine.generic.GenericDAO;
import pl.techdra.models.db.LoginHistory;
import pl.techdra.models.db.PMS;


import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kuba
 * @version 1.0
 *
 * Class designed to saving the last successful user login in the selected project management system (PMS)
 */
public class LoginHistoryController extends GenericDAO<LoginHistory> {
    private static LoginHistoryController instance; //Singleton instance


    /**
     * Get LoginHistoryController instance
     * @return LoginHistoryController instance
     */
    public static synchronized LoginHistoryController getInstance() {
        if (instance == null) {
            instance = new LoginHistoryController();
        }

        return instance;
    }


    private LoginHistoryController() {
        super(LoginHistory.class);
    }


    /**
     * Save a log-in time for specified PMS
     * @see PMS
     */
    public void loginUser(PMS pms) {
        if (pms.getId() < 1) {
            throw new HibernateException(String.format("Specified PMS (%s) not exist!", pms.toString()));
        }

        this.saveOrUpdate(new LoginHistory(pms, LocalDateTime.now()));
    }


    /**
     *
     * @param pms for which the last login date should be returned
     * @return
     * @throws HibernateException
     */
    public LocalDateTime getLastLoginDate(PMS pms)  {
        Session session = sessionFactory.getCurrentSession();

        String hql = "select max(lh.logged) from LoginHistory as lh, PMS as pms where pms.id = :id";
        List result = null;

        try {
            session.beginTransaction();
            Query query = session.createQuery(hql);
            query.setParameter("id", pms.getId());

            result = query.getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
          session.getTransaction().rollback();
        } finally {
            session.close();
        }

        if (result == null || result.isEmpty() ) {
            return null;
        }

        return (LocalDateTime) result.get(0);


    }


    /**
     * Clear the all login history
     */
    public void clearLoginHistory() {
        this.deleteAll();
    }


}
