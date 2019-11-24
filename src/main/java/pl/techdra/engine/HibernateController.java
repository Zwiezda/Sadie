package pl.techdra.engine;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import pl.techdra.engine.exceptions.InvalidDatabaseConnectionParametersException;
import pl.techdra.engine.exceptions.UninitializedDatabaseConnection;
import pl.techdra.models.db.Favorite;
import pl.techdra.models.db.LoginHistory;
import pl.techdra.models.db.PMS;
import pl.techdra.models.db.TimeCache;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kuba
 * @version 1.0
 * <p>
 * Provides access to the database
 */
public class HibernateController {
    private static SessionFactory sessionFactory;

    public static void Init(String password) {
        Logger.getLogger("").setLevel(Level.WARNING);

        Properties props = new Properties();
        Configuration lConf = new Configuration();
        lConf.addAnnotatedClass(Favorite.class);
        lConf.addAnnotatedClass(LoginHistory.class);
        lConf.addAnnotatedClass(TimeCache.class);
        lConf.addAnnotatedClass(PMS.class);
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.connection.driver_class", "org.h2.Driver");
        props.put("hibernate.connection.url", "jdbc:h2:file:./db/main_db;MULTI_THREADED=0;;WRITE_DELAY=0;DB_CLOSE_ON_EXIT=true;CIPHER=AES");
        props.put("hibernate.current_session_context_class", "thread");
        props.put("hibernate.connection.username", "");
        props.put("hibernate.connection.password", password + " ");
        props.put("hibernate.hbm2ddl.auto", "update");

        props.put("hibernate.hikari.connectionTimeout", "3000");
        props.put("hibernate.hikari.minimumIdle", "1");
        props.put("hibernate.hikari.maximumPoolSize", "1");
        props.put("hibernate.hikari.idleTimeout", "300000");
        props.put("hibernate.show_sql", "false");
        lConf.setProperties(props);

        try {
            sessionFactory = lConf.buildSessionFactory();
        } catch (Exception e) {
            InvalidDatabaseConnectionParametersException ex = new InvalidDatabaseConnectionParametersException("Invalid database connection parameters or password");
            ex.initCause(e);

            throw ex;
        }


    }


    /**
     * Get hibernate's session factory instance
     *
     * @return hibernate's session factory instance
     * @see SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new UninitializedDatabaseConnection("HibernateController is uninitialized. Please first invoke Init() method");
        }


        return sessionFactory;
    }


    /**
     * Close database connection
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}


