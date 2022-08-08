package com.demo.core.entity;

import com.demo.core.Application;

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
     * Target object to be tracked by this Camera object.
     */
    private GameObject target;
    /**
     * Speed following factor (tween)
     */
    private double tweenFactor;

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
     * Retrieve the Camera Target
     *
     * @return
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
     * Compute the new Camera position according to the target move and position.
     *
     * @param elapsed the elapsed time since previous call.
     */
    @Override
    public void update(double elapsed) {
        double elapsedD = elapsed * 0.000001;
        x += Math.round((target.x + target.w - (w * 0.5) - x) * tweenFactor * elapsedD);
        y += Math.round((target.y + target.h - (h * 0.5) - y) * tweenFactor * elapsedD);
    }
}
