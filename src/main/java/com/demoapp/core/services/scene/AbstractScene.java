package com.demoapp.core.services.scene;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.demoapp.core.Application;
import com.demoapp.core.entity.Camera;
import com.demoapp.core.entity.GameObject;
import com.demoapp.core.math.Vec2d;

/**
 * The default abstract Scene to implement some internal management mechanics
 * for `Camera`.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public abstract class AbstractScene
        implements Scene {

    protected Application app;

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

    public void create(Application app) {
        this.app = app;
    }

    @Override
    public void load() {

    }

    @Override
    public void update(Application app, double elapsed) {

    }

    /**
     * Add a {@link GameObject} to the Scene
     *
     * @param go the {@link GameObject} to be added.
     */
    protected void add(GameObject go) {
        app.add(go);
    }

    /**
     * Retrieve a {@link GameObject} on its name in the {@link Scene}.
     *
     * @param gameObjectName the name of the {@link GameObject} to be retrieved into
     *                       the {@link Scene}.
     * @return the corresponding {@link GameObject} instance.
     */
    protected GameObject get(String gameObjectName) {
        return app.getObject(gameObjectName);
    }

    /**
     * The default onMouseClick provides the debug level management for
     * {@link GameObject} debugging.
     *
     * @param e the corresponding {@link MouseEvent} to be proceed.
     */
    public void onMouseClick(MouseEvent e) {
        // if debug is activated (debugLevel>0)
        if (app.getConfiguration().getDebugLevel() > 0) {
            // retrieve mouse position adapted to camera position (if ever)
            Point mouseWindowPosition = app.getWindow().mouseScreenPosition(e.getPoint());

            // detect if some object is under mouse position
            GameObject go = app.getCollisionDetection().isMouseColliding(mouseWindowPosition, camera);
            if (Optional.ofNullable(go).isPresent()
                    && e.getButton() == 1
                    && e.getClickCount() > 0) {
                go.debugLevel = go.debugLevel + 1 < 6 ? go.debugLevel + 1 : 0;
                System.out.printf("INFO : DemoScene | The debug mode for %s has been set to %d%n",
                        go.getName(),
                        go.debugLevel);
            }

        }
    }

    /**
     * Standard Key actions for scene.
     *
     * @param e the KeyEvent sent.
     */
    public void onKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            /**
             * Invert gravity
             */
            case KeyEvent.VK_G:
                Vec2d g = app.getPhysicEngine().getWorld().gravity.multiply(-1.0);
                app.getPhysicEngine().getWorld().setGravity(g);
                break;
            /**
             * Reset Scene
             */
            case KeyEvent.VK_Z:
                app.setPause(true);
                create(app);
                app.setPause(false);
                break;
            /**
             * Switch debut level
             */
            case KeyEvent.VK_D:
                int d = app.getConfiguration().getDebugLevel() + 1 < 6 ? app.getConfiguration().getDebugLevel() + 1 : 0;
                app.getConfiguration().setDebugLevel(d);
            default:
                break;
        }
    }

    public void onMouseScrolled(MouseWheelEvent e) {
        Point mouseWindowPosition = app.getWindow().mouseScreenPosition(e.getPoint());
        System.out.printf(
                "INFO : DemoScene | The mouse wheel has been scrolled of %d unit at (x:%d,y:%d)%n",
                e.getScrollAmount(),
                mouseWindowPosition.x,
                mouseWindowPosition.y);

    }
}
