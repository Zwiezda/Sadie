package pl.techdra.engine.generic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import pl.techdra.engine.HibernateController;

import java.util.List;

/**
 * @author Kuba
 * @version 1.0
 *
 * A generic object that allows basic operations on a database model such as write, read or delete
 * @param <T> model class
 */
public class GenericDAO<T> {
    protected SessionFactory sessionFactory;
    protected Logger log;
    private Class<T> modelType;

    /**
     * Class constructor
     * @param modelType class object of used model
     */
    public GenericDAO(Class<T> modelType) {
        sessionFactory = HibernateController.getSessionFactory();
        this.modelType = modelType;
        this.log = LogManager.getLogger( modelType );
    }


    /**
     * Get a object with specified id
     * @param id specified object id
     * @return object from database with specified id
     */
    public T get(int id) {
        Session session = sessionFactory.getCurrentSession();
        T object;

        try {
            session.beginTransaction();

            object = session.get(modelType, id);

            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            log.error(String.format("Error getting %s object with id: %d",modelType.getSimpleName(), id), e);
            throw e;
        } finally {
            session.close();
        }

        return object;
    }


    /**
     * Get a list of all object from database
     * @return list of all object from database
     */
    public List<T> getAll() {
        Session session = sessionFactory.getCurrentSession();
        List<T> result = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery(String.format("from %s", modelType.getSimpleName() ), modelType );
            result = query.getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error(String.format("Error getting all %s objects from database.", modelType.getSimpleName() ), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        return result;
    }


    /**
     * Add or update specified object
     * @param object new or updated object to save in database
     */
    public void saveOrUpdate(T object)  {
        Session session = sessionFactory.getCurrentSession();

        try {
            session.beginTransaction();
            session.saveOrUpdate(object);
            session.getTransaction().commit();

            log.info(String.format("%s object was added or modified: %s", object.getClass().getSimpleName(), object.toString() ) );
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            log.error(String.format("Error adding or updating %s object: %s", object.getClass().getSimpleName(), object.toString() ), e);
            throw e;
        } finally {
            session.close();
        }
    }


    /**
     * Delete specified object from database
     * @param object object to remove
     */
    public void delete(T object) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();

            session.delete(object);

            session.getTransaction().commit();

            log.info(String.format("%s object was deleted: %s",object.getClass().getSimpleName(), object.toString() ) );
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            log.error(String.format("Error deleting %s object: %s",object.getClass().getSimpleName(), object.toString() ), e);
            throw e;
        } finally {
            session.close();
        }
    }


    /**
     * Delete all object from database
     */
    public void deleteAll() {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();

            Query query = session.createQuery(String.format("delete from %s", modelType.getSimpleName() ), modelType );
            query.executeUpdate();

            session.getTransaction().commit();

            log.info(String.format("All %s objects was deleted", modelType.getSimpleName() ) );
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            log.error(String.format("Error deleting all %s objects:", modelType.getSimpleName()), e);
            throw e;
        } finally {
            session.close();
        }
    }


}
