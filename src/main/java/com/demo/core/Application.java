package com.demo.core;

import com.demo.core.entity.GameObject;
import com.demo.core.services.config.Configuration;
import com.demo.core.services.gameloop.StandardGameLoop;
import com.demo.core.services.gfx.Renderer;
import com.demo.core.services.gfx.Window;
import com.demo.core.services.io.InputHandler;
import com.demo.core.services.scene.SceneManager;

import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Very Simple Application.
 *
 * @author Frédéric Delorme
 * @version 0.0.1
 * @since 2022
 */
public class Application {

    /**
     * Internationalized messages read according to default locale from
     * i18n/messages.properties
     */
    private ResourceBundle messages = ResourceBundle.getBundle("i18n.messages", Locale.ROOT);

    /**
     * Flag to request quitting the main application loop;
     */
    private boolean exit;
    /**
     * Configuration component where all configuration are read or set from config
     * file overloaded by arguments from command line
     */
    private Configuration config;

    /**
     * The Window used to display application.
     */
    private Window window;
    /**
     * The rendering pipeline processor
     */
    private Renderer render;

    /**
     * THe GameLoop class.
     */
    private StandardGameLoop gameLoop;

    /**
     * The Scene manager maintaining all the scene for this application.
     */
    private SceneManager scm;

    /**
     * The list of internal object maintained by the Application.
     */
    private Map<String, GameObject> objects = new ConcurrentHashMap<>();

    /**
     * Initialize the application with default configuration file ad then parse args
     * from command line.
     *
     * @param args list of arguments from command line.
     */
    public Application(String[] args) {
        this(args, "app.properties");
        System.out.printf("INFO : Application | %s initialized%n", getConfiguration().getTitle());
    }

    /**
     * Initialize the application with <code>configurationFilename</code> and then parse <code>args</code>
     * from command line.
     *
     * @param args                  list of arguments from command line.
     * @param configurationFilename the name of the properties file to be use as
     *                              configuration file.
     */
    public Application(String[] args, String configurationFilename) {

        config = new Configuration(configurationFilename);
        config.parseArguments(args);
    }

    /**
     * Execute internal processing for the application, conforming to standard
     * application structure:
     *
     * <pre>
     * 1. create
     * 2. loop
     * 3. dispose
     * </pre>
     */
    public void run() {
        System.out.printf("INFO : Application | %s started%n", config.getTitle());
        create();
        if (!isTestMode()) {
            loop();
            dispose();
        }
    }

    /**
     * Create all elements for this Application to be managed.
     */
    private void create() {
        window = new Window(
                config.getTitle(),
                config.getWindowDimension(),config.getScale())
                .attachHandler(new InputHandler());
        render = new Renderer(config);
        gameLoop = new StandardGameLoop();
        scm = new SceneManager(this);

        createScene();
    }

    public void createScene() {
        if (Optional.ofNullable(scm.getCurrent()).isPresent()) {
            scm.getCurrent().create(this);
        }
    }

    /**
     * The main loop for this application.
     */
    private void loop() {
        do {
            gameLoop.process(this);
            // Nothing to do right now
        } while (!isExit() && !isTestMode());
    }

    /**
     * Release all resources opened or loaded by thus application.
     */
    public void dispose() {
        System.out.printf("INFO : Application | %s ended%n", config.getTitle());
        window.dispose();
    }

    /**
     * Retrieve a translated message to active Locale.
     *
     * @param key key for the translated message
     * @return value of the translated message into the current active Locale.
     */
    public String getMessage(String key) {
        return messages.getString(key);
    }

    /**
     * Does exit requested ?
     *
     * @return return true is exiting is requested.
     */
    public boolean isExit() {
        return exit;
    }

    /**
     * Is Test mode activated ?
     *
     * @return true if test mode is currently set.
     */
    public boolean isTestMode() {
        return "test".equals(config.getMode());
    }

    /**
     * Is run mode activated
     *
     * @return return true if run mode is currently set.
     */
    public boolean isRunMode() {
        return "run".equals(config.getMode());
    }

    /**
     * Return the configuration instance.
     *
     * @return the current Configuration instance of this application.
     */
    public Configuration getConfiguration() {
        return config;
    }

    /**
     * return the Renderer instance.
     *
     * @return the current Renderer instance of this application.
     */
    public Renderer getRender() {
        return this.render;
    }


    public Window getWindow() {
        return window;
    }

    public Collection<GameObject> getObjects() {
        return objects.values();
    }

    public GameObject getObject(String name) {
        return objects.get(name);
    }

    public void add(GameObject go) {
        objects.put(go.getName(), go);
    }

    public void remove(GameObject go) {
        objects.remove(go.getName());
    }

    /**
     * Main method for the Application class.
     *
     * @param args list of java command line arguments.
     */
    public static void main(String[] args) {
        Application app = new Application(args);
        app.run();
    }

    /**
     * Request application to exit.
     */
    public void requestExit() {
        this.exit = true;
    }

    public Rectangle2D getGameArea() {
        return this.config.getGameArea();
    }

    public SceneManager getSceneManager() {
        return scm;
    }
}