package com.demoapp.core;

import com.demoapp.core.entity.GameObject;
import com.demoapp.core.services.physic.PhysicEngine;
import com.demoapp.core.services.collision.CollisionDetection;
import com.demoapp.core.services.config.Configuration;
import com.demoapp.core.services.gameloop.FixFPSGameLoop;
import com.demoapp.core.services.gameloop.StandardGameLoop;
import com.demoapp.core.services.gfx.Renderer;
import com.demoapp.core.services.gfx.Window;
import com.demoapp.core.services.io.InputHandler;
import com.demoapp.core.services.scene.SceneManager;

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
    private final ResourceBundle messages = ResourceBundle.getBundle("i18n.messages", Locale.ROOT);

    /**
     * Flag to request quitting the main application loop;
     */
    private boolean exit;
    /**
     * Configuration component where all configuration are read or set from config
     * file overloaded by arguments from command line
     */
    private final Configuration config;

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
    private final Map<String, GameObject> objects = new ConcurrentHashMap<>();

    /**
     * The internal physic engine to update objects.
     */
    private PhysicEngine physicEngine;

    /**
     * the internal Collision Detection service.
     */
    private CollisionDetection collisionDetection;

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
     * Initialize the application with <code>configurationFilename</code> and then
     * parse <code>args</code>
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
        if (isNotTestMode()) {
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
                config.getWindowDimension(), config.getScale())
                .attachHandler(new InputHandler());

        render = new Renderer(config);
        gameLoop = new FixFPSGameLoop(config.getFPS());
        scm = new SceneManager(this);
        physicEngine = new PhysicEngine(this);
        collisionDetection = new CollisionDetection(this);

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
        } while (!isExit() && isNotTestMode());
    }

    /**
     * Release all resources opened or loaded by thus application.
     */
    public void dispose() {
        System.out.printf("INFO : Application | %s ended%n", config.getTitle());
        if (Optional.ofNullable(window).isPresent()) {
            window.dispose();
        }
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
    public boolean isNotTestMode() {
        return !"test".equals(config.getMode());
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

    /**
     * Retrieve the {@link Window} of the {@link Application}.
     *
     * @return the current instance of the {@link Window}'s {@link Application}.
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Retrieve the collection of {@link GameObject} from this {@link Application}.
     *
     * @return the collection of {@link GameObject} used by the {@link Application}.
     */
    public Collection<GameObject> getObjects() {
        return objects.values();
    }

    /**
     * retrieve the {@link GameObject} named name.
     *
     * @param name the name of the {@link GameObject} to be retrieved from the {@link Application}.
     * @return the corresponding {@link GameObject} to the required name.
     */
    public GameObject getObject(String name) {
        return objects.get(name);
    }

    /**
     * Add a {@link GameObject} to the {@link Application}.
     *
     * @param go the {@link GameObject} to be added to the {@link Application}.
     */
    public void add(GameObject go) {
        objects.put(go.getName(), go);
        if (Optional.ofNullable(collisionDetection).isPresent()) {
            collisionDetection.add(go);
        }
    }

    /**
     * Remove a {@link GameObject} from the {@link Application} and its services.
     *
     * @param go the {@link GameObject} to be removed from the {@link Application}.
     */
    public void remove(GameObject go) {
        objects.remove(go.getName());
        collisionDetection.remove(go);
    }

    /**
     * Request application to exit.
     */
    public void requestExit() {
        this.exit = true;
    }

    /**
     * Retrieve the game area
     *
     * @return the current instance of the game area as a {@link Rectangle2D}.
     */
    public Rectangle2D getGameArea() {
        return this.config.getGameArea();
    }

    /**
     * Retrieve the {@link SceneManager}
     *
     * @return the current instance of the {@link SceneManager}
     */
    public SceneManager getSceneManager() {
        return scm;
    }

    /**
     * Retrieve the {@link PhysicEngine}
     *
     * @return the current instance of the {@link PhysicEngine}
     */
    public PhysicEngine getPhysicEngine() {
        return this.physicEngine;
    }

    /**
     * Retrieve the collision detection service.
     *
     * @return the current instance of the {@link CollisionDetection}
     */
    public CollisionDetection getCollisionDetection() {
        return collisionDetection;
    }

    /**
     * Main method for the {@link Application} class.
     *
     * @param args list of java command line arguments.
     */
    public static void main(String[] args) {
        Application app = new Application(args);
        app.run();
    }
}