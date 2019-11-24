package pl.techdra.engine;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import pl.techdra.models.settings.App;
import pl.techdra.models.settings.Shortcuts;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Kuba
 * @version 1.0
 *
 * A controller that allows global listening of mouse and keyboard events.
 * Responsible for global keyboard shortcuts and stopping the stopwatch in the case of user inactivity
 */
public class InputController {
    /* Singleton implementation */

    private static InputController instance; //Singleton instance

    /**
     * Get InputController instance
     * @return InputController instance
     */
    public static synchronized InputController getInstance() {
        if (instance == null) {
            instance = new InputController();
        }

        return instance;
    }


    /* Nested class for JNativeHook */

    private class InputListenerHook implements NativeKeyListener, NativeMouseInputListener {
        public void nativeKeyPressed(NativeKeyEvent e) {
            preesedKeys.add(e.getKeyCode());
            updateActivityTime();

            System.out.println( e.getKeyCode() );

            invokeShortcut();
        }

        public void nativeKeyReleased(NativeKeyEvent e) {
            preesedKeys.remove( e.getKeyCode() );

            System.out.println( e.getKeyCode() );

            updateActivityTime();
        }

        public void nativeKeyTyped(NativeKeyEvent e) {
            //preesedKeys.add(e.getKeyCode());

            //System.out.println( e.getKeyCode() );

            updateActivityTime();
            //invokeShortcut();
        }

        public void nativeMouseClicked(NativeMouseEvent e) {
            updateActivityTime();
        }

        public void nativeMousePressed(NativeMouseEvent e) {
            updateActivityTime();
        }

        public void nativeMouseReleased(NativeMouseEvent e) {
            updateActivityTime();
        }

        public void nativeMouseMoved(NativeMouseEvent e) {
            updateActivityTime();
        }

        public void nativeMouseDragged(NativeMouseEvent e) {
            updateActivityTime();
        }
    }



    /* InputController implementation */

    private ConcurrentHashMap<HashSet<Integer>, String> shortcuts;   //Reversed map of shortcuts
    private HashMap<String, Runnable> actions;  //Map of actions for specified shortcuts

    private volatile long maxInactivity;
    private long lastUserActivity;  //Time of last user activity

    private Timer timer;
    private volatile Runnable userInactivityAction;  //Action invoked when user is inactive
    private HashSet<Integer> preesedKeys;
    private MainSettingsController settingsController;

    private InputListenerHook inputListenerHook;


    private InputController() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
        preesedKeys = new HashSet<>();
        lastUserActivity = System.currentTimeMillis();

        timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( System.currentTimeMillis() - lastUserActivity >= maxInactivity ) {
                    userInactivityAction.run();
                }
            }
        });

        inputListenerHook = new InputListenerHook();

    }

    /**
     * Initializes listening for the specified keyboard shortcuts and their actions
     * @param shortcuts Shortcuts object
     * @see Shortcuts
     * @param actions actions to specified actions
     */
    public void initialize(Shortcuts shortcuts, HashMap<String, Runnable> actions) {
        setShortcuts(shortcuts);

        this.actions = new HashMap<>(actions);
    }



    /* If user move mouse or pressed key - update time */
    private void updateActivityTime() {
        lastUserActivity = System.currentTimeMillis();
    }

    /* Invoke shortcut action */
    private void invokeShortcut() {
        System.out.println( preesedKeys.toString() );

        if ( shortcuts.containsKey( preesedKeys ) ) {
            String actionName = shortcuts.get( preesedKeys );
            if (actions.containsKey(actionName)) {
                actions.get( actionName ).run();
            }

        }
    }


    /**
     * Starts the stopwatch that measures the time of user inactivity
     * @return true of stopwatch is running
     */
    public boolean starInactivityTimer() {
        if (maxInactivity  <= 0 || userInactivityAction == null) {
            return false;
        }

        timer.start();
        return timer.isRunning();
    }

    /**
     * Stop inactivity stopwatch
     */
    public void stopInactivityTimer() {
       timer.stop();
    }


    /**
     * Get max user inactivity period (in seconds)
     * @return max user inactivity period (in seconds)
     */
    public int getMaxInactivity() {
        return (int) (maxInactivity / 1000);
    }

    /**
     * Set max user inactivity in seconds
     * @param maxInactivity max user inactivity (in seconds)
     */
    public void setMaxInactivity(int maxInactivity) {
        this.maxInactivity =  maxInactivity * 1000;
    }


    /**
     * Set user inactivity action
     * This action is invoked when user is inactive for longer than maxInactivity param
     * @param userInactivityAction inactivity action
     */
    public void setUserInactivityAction(Runnable userInactivityAction) {
        this.userInactivityAction = userInactivityAction;
    }


    /**
     * Keyboard shortcuts that the system should respond to
     * @param shortcuts
     * @see Shortcuts
     */
    public void setShortcuts(Shortcuts shortcuts) {
        this.shortcuts = shortcuts.reverseShortcuts();
    }


    /**
     *  Start keyboard and mouse global listener
     * @throws NativeHookException if OS is not supported to set global hook
     */
    public void startListener() throws NativeHookException {
        GlobalScreen.registerNativeHook();

        GlobalScreen.addNativeKeyListener(inputListenerHook);
        GlobalScreen.addNativeMouseListener(inputListenerHook);
    }

    /**
     *  Stop keyboard and mouse global listener
     * @throws NativeHookException if any problems with disconnecting global mose and keyboard listener
     */
    public void stopListener() throws NativeHookException {
        if ( GlobalScreen.isNativeHookRegistered() ) {
            GlobalScreen.unregisterNativeHook();
        }
    }


    public static void main(String[] args) {
        try {
            MainSettingsController sc = MainSettingsController.getInstance();
            App appConfig = sc.getConfig().getAppConfig();

            HashMap<String, Runnable> test = new HashMap<>();
            for (String keySystem: appConfig.getShortcuts().getActionNames()) {
                test.put(keySystem, new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("test");
                    }
                });
            }

            InputController ic = InputController.getInstance();
            ic.initialize(appConfig.getShortcuts(), test);
            ic.startListener();
            ic.setUserInactivityAction(new Runnable() {
                @Override
                public void run() {
                    System.out.println("2 LATE");
                    try {
                        ic.stopListener();
                    } catch (Exception e) {
                    }
                }
            });

            ic.setMaxInactivity(10);
            ic.starInactivityTimer();

        }
        catch (Exception ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

    }
}
