package com.demoapp.core.services.scene;

import com.demoapp.core.entity.Camera;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default abstract Scene to implement some internal management mechanics for `Camera`.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public abstract class AbstractScene implements Scene {
    /**
     * Internal current active Camera.
     */
    protected Camera camera;
    /**
     * Map of declared cameras on the scene.
     */
    protected Map<String, Camera> cameras = new ConcurrentHashMap<>();

    @Override
    public Scene setCamera(String camName) {
        this.camera = cameras.get(camName);
        return this;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public Scene addCamera(Camera cam) {
        this.cameras.put(cam.getName(), cam);
        if (Optional.ofNullable(this.camera).isEmpty()) {
            this.camera = cam;
        }
        return this;
    }

    @Override
    public Camera getCamera(String name) {
        return this.cameras.get(name);
    }
}
