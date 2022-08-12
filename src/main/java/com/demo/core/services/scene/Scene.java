package com.demo.core.services.scene;

import com.demo.core.Application;
import com.demo.core.entity.Camera;

/**
 * Scene interface to define Scene creation.
 *
 * @author Frédéric Delorme
 * @version 1.0.0
 * @since 2022
 */
public interface Scene {

    /**
     * Retrieve the Scene name.
     *
     * @return the name of the scene.
     */
    String getName();

    /**
     * Create the scene's objects
     *
     * @param app the {@link Application} container
     */
    void create(Application app);

    /**
     * Manage input for that scene.
     *
     * @param app the {@link Application} container.
     */
    void input(Application app);

    /**
     * Return the {@link Scene} current active {@link Camera}.
     *
     * @return the active {@link Camera}.
     */
    Camera getCamera();

    /**
     * Add a {@link Camera} to the {@link Scene}. If none already set, add it as the default active one.
     *
     * @param cam the new {@link Camera} to be added to the {@link Scene}.
     * @return the updated {@link Scene}.
     */
    Scene addCamera(Camera cam);

    /**
     * Retrieve one of the declared {@link Camera} in the {@link Scene}.
     *
     * @param camName the name of the {@link Camera} to be retrieved.
     * @return the corresponding {@link Camera}.
     */
    Camera getCamera(String camName);

    /**
     * Set <code>camName</code> {@link Camera} as the default active camera.
     *
     * @param camName the name of the camera to be activated.
     * @return the updated {@link Scene}.
     */
    Scene setCamera(String camName);

}
