package com.demo.core.services.scene;

import com.demo.core.Application;
import com.demo.core.services.config.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Scene management component to maintain a current scene and a list of available scene in the Application.
 *
 * @author Frédéric Delorme
 * @version 1.0.0
 * @since 2022
 */
public class SceneManager {
    /**
     * Current active Scene
     */
    private Scene current;
    /**
     * list of available scene loaded from configuration.
     */
    private final Map<String, Scene> scenes = new ConcurrentHashMap<>();

    /**
     * Create the SceneManager upon the {@link Application} container
     *
     * @param app the container {@link Application}
     */
    public SceneManager(Application app) {
        initialize(app.getConfiguration());
    }

    /**
     * Create the SceneManager upon the {@link Configuration} component
     *
     * @param config the configuration component {@link Configuration}
     */
    public SceneManager(Configuration config) {
        initialize(config);
    }

    private void initialize(Configuration config) {
        if (!config.getSceneList().equals("") && !config.getSceneDefault().equals("")) {
            String[] scenesList = config.getSceneList().split(",");
            Arrays.asList(scenesList).forEach(s -> {
                String[] kv = s.split(":");
                try {
                    add((Class<? extends Scene>) Class.forName(kv[1]));
                    System.out.printf("INFO : SceneManager | Add scene %s:%s%n", kv[0], kv[1]);
                } catch (ClassNotFoundException e) {
                    System.out.printf("ERR : SceneManager | Unable to load class %s%n", kv[1]);
                }
            });
            activateScene(config.getSceneDefault());
        } else {
            System.err.println("ERR : SceneManager | No scene defined into the configuration");
        }

    }

    private void activateScene(String sceneName) {
        this.current = scenes.get(sceneName);
    }

    public void add(Class<? extends Scene> sceneClass) {
        Scene scn;
        try {
            scn = sceneClass.getConstructor().newInstance();
            scenes.put(scn.getName(), scn);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            System.out.printf("ERR: unable to ass scene %s%n", sceneClass.getName());
        }
    }

    public void activate(String sceneName) {
        current = scenes.get(sceneName);
    }

    public Scene getCurrent() {
        return current;
    }

    /**
     * Return the list of scenes loaded from configuration into SceneManager.
     *
     * @return the current instantiated Scene list
     */
    public Collection<Scene> getSceneList() {
        return this.scenes.values();
    }

    /**
     * Return a specific scene by its name from tha scenes list.
     *
     * @param sceneName the scene name to be retrieved from the list.
     * @return the Scene instance corresponding to the sceneName.
     */
    public Scene getScene(String sceneName) {
        return scenes.get(sceneName);
    }
}
