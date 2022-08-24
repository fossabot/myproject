package com.demo.core.math;

import com.demo.core.Application;
import com.demo.core.entity.GameObject;

import java.awt.geom.Rectangle2D;


/**
 * The {@link PhysicEngine} is the service to maintain and compute
 * {@link GameObject} position and behavior.
 *
 * @author Frédéric Delorme
 * @version 1.0.0
 */
public class PhysicEngine {

    /**
     * Parent Application.
     */
    private Application app;


    private World world = new World();

    /**
     * Create a new Physic Engine.
     *
     * @param app the content Application for this service.
     */
    public PhysicEngine(Application app) {
        this.app = app;
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
        double t = elapsed / 1000000.0;
        switch(go.getPhysicType()){
            case NONE:
            case STATIC:
                break;
            case DYNAMIC:
                Vec2d force = new Vec2d(0, 0);
                go.forces.add(world.gravity.multiply(0.1));
                for (Vec2d f : go.forces) {
                    force.add(f);
                }
                go.acc = force.multiply(t / (go.mass * go.material.density));
                go.acc.ceilAndMax(0.001,0.5);

                go.speed = go.speed.add(go.acc.multiply(0.5 * t).multiply(go.material.friction));
                go.speed.ceilAndMax(0.01,0.5);

                go.pos = go.pos.add(go.speed.multiply(t));
                go.forces.clear();
                break;
        }
    }

    /**
     * Constrains the current object into a Rectangle area.
     *
     * @param area the play area for the application.
     * @return true if constrained, else false.
     */
    public boolean constrainedBy(GameObject go, Rectangle2D area) {
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
        return collide;
    }

    public World getWorld() {
        return world;
    }
}
