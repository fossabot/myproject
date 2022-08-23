package com.demo.core.entity;

import com.demo.core.Application;

import java.awt.geom.Rectangle2D;

/**
 * A {@link Camera} Object intends to follow a <code>target</code> which is a {@link GameObject}.
 * <p>
 * The following speed is adjusted with a <code>tweenFactor</code> to let the Camera smoothly stick
 * to the <code>target</code> position.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class Camera extends GameObject {

    /**
     * Targeted {@link GameObject} to be tracked by this {@link Camera} object.
     */
    private GameObject target;
    /**
     * Speed following factor (tween)
     */
    private double tweenFactor;

    /**
     * The Field Of View for this {@link Camera}.
     */
    private Rectangle2D fov;


    /**
     * Create a new {@link GameObject} named <code>name</code>.
     *
     * @param name the name of the new {@link GameObject} to be managed by the
     *             {@link Application} container.
     */
    public Camera(String name) {
        super(name);
    }

    /**
     * Retrieve the {@link Camera} Target
     *
     * @return the target of the {@link Camera}
     */
    public GameObject getTarget() {
        return target;
    }

    /**
     * Define the Camera target.
     *
     * @param target a GameObject to be tracked by the Camera.
     * @return the Camera object updated with its new target.
     */
    public Camera setTarget(GameObject target) {
        this.target = target;
        return this;
    }

    /**
     * Define the following speed factor for this Camera.
     *
     * @param tf the new tween factor to be applied to the camera new speed.
     * @return the Camera object updated with its new target.
     */
    public Camera setTweenFactor(double tf) {
        this.tweenFactor = tf;
        return this;
    }

    /**
     * Define the Field of view for this Camera.
     *
     * @param fov the new field of view for this camera.
     * @return the Camera object updated with its new target.
     */
    public Camera setFOV(Rectangle2D fov) {
        this.fov = fov;
        return this;
    }

    /**
     * Compute the new Camera position according to the target move and position.
     *
     * @param elapsed the elapsed time since previous call.
     */
    @Override
    public void update(double elapsed) {
        double elapsedD = elapsed * 0.000001;
        pos.x += (target.pos.x + target.w - (fov.getWidth() * 0.5) - pos.x) * tweenFactor * elapsedD;
        pos.y += (target.pos.y + target.h - (fov.getHeight() * 0.5) - pos.y) * tweenFactor * elapsedD;
    }
}
