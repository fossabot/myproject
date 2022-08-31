package com.demoapp.core.services.physic;

import com.demoapp.core.Application;
import com.demoapp.core.entity.GameObject;
import com.demoapp.core.math.Vec2d;
import com.demoapp.core.services.config.Configuration;

import java.awt.geom.Rectangle2D;


/**
 * The {@link PhysicEngine} is the service to maintain and compute
 * {@link GameObject} position and behavior.
 *
 * @author Frédéric Delorme
 * @version 1.0.0
 */
public class PhysicEngine {

    private final Configuration config;
    /**
     * Parent {@link Application}.
     */
    private Application app;

    /**
     * The {@link World} where any {@link GameObject} evolves.
     */
    private World world = new World();

    /**
     * Create a new {@link PhysicEngine}.
     *
     * @param app the content Application for this service.
     */
    public PhysicEngine(Application app) {
        this.app = app;
        this.world.gravity = app.getConfiguration().defaultGravity;
        this.config = app.getConfiguration();
    }

    /**
     * Update all the objects of {@link Application}.
     *
     * @param elapsed the elapsed time since previous call.
     */
    public void update(double elapsed) {
        app.getObjects().forEach(o -> {
            updateObject(o, elapsed);
            o.update(elapsed);
            constrainedBy(o, app.getGameArea());
        });
    }

    /**
     * Update physic attributes according to newton's laws.
     *
     * @param elapsed the elapsed time since previous call.
     */
    public void updateObject(GameObject go, double elapsed) {
        double t = elapsed / (1000000.0 * 0.9);
        switch (go.getPhysicType()) {

            case NONE:
            case STATIC:
                break;

            case DYNAMIC:
                Vec2d force = new Vec2d(0, 0);
                go.forces.add(world.gravity.multiply(-0.03));
                for (Vec2d f : go.forces) {
                    force.add(f);
                }
                double density = go.material.density * world.defaultWorldMaterial.density;
                double friction = go.material.friction * world.defaultWorldMaterial.friction;

                go.acc = force.multiply(t / (go.mass * go.material.density));
                go.acc.minMax(config.accMinValue, config.accMaxValue);

                go.speed.add(go.acc.multiply(0.5 * t));

                if (go.collide) {
                    go.speed = go.speed.multiply(go.colliders.get(0).material.friction);
                } else {
                    go.speed.multiply(density);
                }
                go.speed = go.speed.minMax(config.speedMinValue, config.speedMaxValue);

                go.pos = go.pos.add(go.speed.multiply(t));
                go.forces.clear();
                break;
        }

        go.collide = false;
    }

    /**
     * Constrains the current object into a Rectangle area.
     *
     * @param area the play area for the application.
     */
    public void constrainedBy(GameObject go, Rectangle2D area) {
        boolean collide = false;
        double fx = 1.0, fy = 1.0;
        if (!area.contains(go.pos.x, go.pos.y, go.w, go.h)) {
            if (go.pos.x < area.getX()) {
                go.pos.x = area.getX();
                fx = -1.0;
                collide = true;
            }
            if (go.pos.x > area.getWidth() - go.w) {
                go.pos.x = area.getWidth() - go.w;
                fx = -1.0;
                collide = true;
            }
            if (go.pos.y < area.getY()) {
                go.pos.y = area.getY();
                fy = -1.0;
                collide = true;
            }
            if (go.pos.y > area.getHeight() - go.h) {
                go.pos.y = area.getHeight() - go.h;
                fy = -1.0;
                collide = true;
            }
        }
        if (collide) {
            go.forces.clear();
            go.speed.x *= go.material.friction;
            go.speed.y *= go.material.friction;

            go.speed.x *= fx * go.material.elasticity;
            go.speed.y *= fy * go.material.elasticity;
        }
    }

    public World getWorld() {
        return world;
    }
}
