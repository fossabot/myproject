package com.demoapp.core.services.collision;

import com.demoapp.core.Application;
import com.demoapp.core.entity.GameObject;
import com.demoapp.core.math.MathUtils;
import com.demoapp.core.math.Vec2d;
import com.demoapp.core.services.config.Configuration;
import com.demoapp.core.services.physic.PhysicType;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Collision Detection service.
 * This service intends to detect collision (sic) between multiple GameObject into the application.
 * It is typically executed just after the PhysicEngine to compute and detect collision.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class CollisionDetection {
    /**
     * the {@link Configuration} for this service.
     */
    private final Configuration config;

    /**
     * List of the {@link GameObject} to be collision detect to.
     */
    public Map<String, GameObject> colliders = new ConcurrentHashMap<>();

    /**
     * initialization of the CollisionDetector service
     *
     * @param a the parent Application
     */
    public CollisionDetection(Application a) {
        this.config = a.getConfiguration();
    }

    /**
     * Adding an {@link GameObject} to the collision detection service.
     *
     * @param e the {@link GameObject} to kae part in the collision detection system.
     */
    public void add(GameObject e) {
        colliders.put(e.getName(), e);
    }

    /**
     * Remove an {@link GameObject}
     * detection service.
     *
     * @param e the {@link GameObject} to be removed from the collision detection
     *          system.
     */
    public void remove(GameObject e) {
        colliders.remove(e.getName());
    }

    /**
     * Remove a {@link GameObject} on its name
     * detection service.
     *
     * @param gameObjectName the name of the {@link GameObject} to be removed from the collision detection
     *                       system.
     */
    public void remove(String gameObjectName) {
        colliders.remove(gameObjectName);
    }

    /**
     * Step into the detection
     *
     * @param elapsed the elapsed time since previous call.
     */
    public void update(double elapsed) {
        detect();
    }

    private void detect() {
        List<GameObject> targets = colliders.values().stream().filter(e -> e.isAlive() || e.isPersistent()).toList();
        for (GameObject e1 : colliders.values()) {
            e1.collide = e1.collide || false;
            for (GameObject e2 : targets) {
                e2.collide = e2.collide || false;
                if (e1.id != e2.id) {
                    if (e1.collisionBox.getBounds().intersects(e2.collisionBox.getBounds())) {
                        resolve(e1, e2);
                        e1.onCollision(e1, e2);
                    }
                }
            }
        }
    }

    /**
     * Collision response largely inspired by the article from
     * <a href=
     * "https://spicyyoghurt.com/tutorials/html5-javascript-game-development/collision-detection-physics">collision-detection-physics</a>
     *
     * @param e1 first GameObject in the collision
     * @param e2 second GameObject in the collision
     */
    private void resolve(GameObject e1, GameObject e2) {
        e1.collide = true;
        e2.collide = true;

        Vec2d vp = new Vec2d((e2.pos.x - e1.pos.x), (e2.pos.y - e1.pos.y));
        double distance = Math
                .sqrt((e2.pos.x - e1.pos.x) * (e2.pos.x - e1.pos.x) + (e2.pos.y - e1.pos.y) * (e2.pos.y - e1.pos.y));
        Vec2d colNorm = new Vec2d(vp.x / distance, vp.y / distance);

        if (e1.getPhysicType().equals(PhysicType.DYNAMIC)
                && e2.getPhysicType().equals(PhysicType.DYNAMIC)) {
            e1.colliders.add(e2);
            e2.colliders.add(e1);

            Vec2d vRelSpeed = new Vec2d(e1.speed.x - e2.speed.x, e1.speed.y - e2.speed.y);
            double colSpeed = vRelSpeed.x * colNorm.x + vRelSpeed.y * colNorm.y;
            var impulse = 2 * colSpeed / (e1.mass * e1.material.density + e2.mass * e2.material.density);
            e1.speed.x -= MathUtils.ceilMinMaxValue(impulse * e2.mass * e2.material.density * colSpeed * colNorm.x,
                    config.colSpeedMinValue, config.colSpeedMaxValue);
            e1.speed.y -= MathUtils.ceilMinMaxValue(impulse * e2.mass * e2.material.density * colSpeed * colNorm.y,
                    config.colSpeedMinValue, config.colSpeedMaxValue);
            e2.speed.x += MathUtils.ceilMinMaxValue(impulse * e1.mass * e2.material.density * colSpeed * colNorm.x,
                    config.colSpeedMinValue, config.colSpeedMaxValue);
            e2.speed.y += MathUtils.ceilMinMaxValue(impulse * e1.mass * e2.material.density * colSpeed * colNorm.y,
                    config.colSpeedMinValue, config.colSpeedMaxValue);

            /*
            System.out.printf("e1.%s collides e2.%s Vp=%s / dist=%f / norm=%s%n", e1.getName(),
                    e2.getName(), vp, distance, colNorm);
            */
        } else {

            if (e1.getPhysicType().equals(PhysicType.DYNAMIC)
                    && e2.getPhysicType().equals(PhysicType.STATIC)) {
                e1.colliders.add(e2);
                if (e2.material.elasticity > 0) {
                    // 4 = nb min pixel to authorise going upper e2 object.
                    if (e1.pos.y + e1.h > e2.pos.y && vp.y > 0) {
                        e1.pos.y = e2.pos.y - e1.h;
                        e1.speed.y = -e1.speed.y * e1.material.elasticity * e2.material.elasticity;
                    } else {
                        e1.pos.y = e2.pos.y + e2.h;
                        e1.speed.y = -e1.speed.y * e1.material.elasticity * e2.material.elasticity;
                    }
                    /*
                    System.out.printf("e1.%s collides static e2.%s%n", e1.getName(), e2.getName());
                    */
                }
            }
            if (e1.getPhysicType().equals(PhysicType.DYNAMIC)
                    && e2.getPhysicType().equals(PhysicType.NONE)) {
                e1.colliders.add(e2);
            }
        }
        e1.update(1);
        e2.update(1);
    }

    /**
     * Detect if a 2D point is contained by a {@link GameObject}
     *
     * @param position the position to test against the list of colliders {@link GameObject}t in the {@link CollisionDetection}
     * @return the colliding {@link GameObject} or null;
     */
    public GameObject isColliding(Point2D position) {
        for (GameObject o : colliders.values()) {
            if (o.box.contains(position)) {
                return o;
            }
        }
        return null;
    }
}
