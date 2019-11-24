package pl.techdra.engine;
import org.hibernate.Session;
import pl.techdra.api.Plugin;
import pl.techdra.engine.events.Observable;
import pl.techdra.engine.events.OnTimeCachePushErrorEvent;
import pl.techdra.engine.events.OnTimeCachePushEvent;
import pl.techdra.engine.exceptions.InterruptedTaskAlreadyExistException;
import pl.techdra.engine.exceptions.PluginException;
import pl.techdra.engine.generic.GenericDAO;
import pl.techdra.models.db.TimeCache;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;


public class CacheController extends GenericDAO<TimeCache> {
    private static CacheController instance; //Singleton instance
    private PluginManager pluginManager;
    private LocalDateTime lastCronRun;

    private Timer timer;

    public Observable<OnTimeCachePushEvent> timeCachePushEventInvoker = new Observable<OnTimeCachePushEvent>();
    public Observable<OnTimeCachePushErrorEvent> timeCachePushErrorEventInvoker = new Observable<OnTimeCachePushErrorEvent>();

    public static synchronized CacheController getInstance() {
        if (instance == null) {
            instance = new CacheController();
        }

        return instance;
    }

    private CacheController() {
        super(TimeCache.class);
        pluginManager = PluginManager.getInstance();

        MainSettingsController mainSettingsController = MainSettingsController.getInstance();
        timer = new Timer(mainSettingsController.getConfig().getAppConfig().getTaskPushTimer(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pushAllTask();
            }
        });
    }

    public void setTimerDelay(int delay) {
        timer.setDelay(delay * 1000);
    }

    public boolean cronIsRunning() {
        return timer.isRunning();
    }


    private void confirmTask(TimeCache timeCache) {
        timeCache.setPushed(LocalDateTime.now());
        saveOrUpdate(timeCache);
    }



    public TimeCache getInterruptedTask() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        TimeCache result = session.createQuery("from TimeCache where interrupted = TRUE", TimeCache.class)
                .getResultList().stream().findFirst().orElse(null);
        session.getTransaction().commit();

        return result;

    }


    public void addTask(TimeCache timeCache)  {
        if ( timeCache.isInterrupted() ) {
            TimeCache currentInterrputedTask = getInterruptedTask();

            if (currentInterrputedTask != null && currentInterrputedTask.getId() != timeCache.getId() ) {
                throw new InterruptedTaskAlreadyExistException("Another interrupted tassk already exist!.");
            }
        }

        saveOrUpdate(timeCache);

    }

    public List<TimeCache> getUnconfirmedTasks() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<TimeCache> result = session.createQuery("from TimeCache where interrupted = FALSE AND pushed is NULL", TimeCache.class).getResultList();
        session.getTransaction().commit();

        return result;
    }


    public void pushTask(TimeCache timeCache) throws PluginException {
        if ( timeCache.isInterrupted() ) {
            throw new IllegalArgumentException("Task can not be interrupted!");
        }

       Plugin plugin = pluginManager.getPlugin( timeCache.getPms().getPlugin() );
       try {
           plugin.getPMSManage().raportTime(timeCache);
           confirmTask(timeCache);

           timeCachePushEventInvoker.update(new OnTimeCachePushEvent(this, timeCache));
       } catch (Exception e) {
           PluginException pe = new PluginException("Error pushing time to PMS", timeCache.getPms() );
           pe.initCause(e);

           throw pe;

       }
    }

    public void pushAllTask() {
        for (TimeCache tc: getUnconfirmedTasks()) {
            try {
                pushTask(tc);
            } catch (PluginException ex) {
                timeCachePushErrorEventInvoker.update(new OnTimeCachePushErrorEvent(this, ex));
            }
        }
    }


    public void startCron() {
        timer.start();
    }

    public void stopCron() {
        timer.stop();
    }



}
