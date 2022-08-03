package com.demo.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
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
    private Map<String, Scene> scenes = new ConcurrentHashMap<>();

    /**
     * Create the SceneManager upon the {@link Application} container
     *
     * @param app
     */
    public SceneManager(Application app) {
        initialize(app.getConfiguration());
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
            System.out.printf("ERR : SceneManager | No scene defined into the configuration");
        }

    }

    private void activateScene(String sceneName) {
        this.current = scenes.get(sceneName);
    }

    public void add(Class<? extends Scene> sceneClass) {
        Scene scn = null;
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

}
