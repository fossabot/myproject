package com.demo.core;

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
     * @param app the {@link Application} container.
     */
    void input(Application app);
}
