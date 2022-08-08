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
     * Return the current active {@link Camera}.
     *
     * @return the active Camera.
     */
    Camera getCamera();
}
