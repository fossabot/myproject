package com.demoapp.core.services.physic;

import com.demoapp.core.math.Vec2d;

/**
 * The World object contains all specific world information where the application is running.
 * At least the default gravity is set.
 * Even it preloaded with a default value, it is configurable with the
 * <code>app.physic.default.gravity</code> configuration key (see <code>app.properties</code> for set values).
 */
public class World {
    /**
     * Default world gravity used by the {@link PhysicEngine}, this value is computed regarding the Earth gravity (0.981)
     */
    public Vec2d gravity = new Vec2d(0, 0.981);
    /**
     * define a default material for the world to set a default physic computation behavior.
     */
    public Material defaultWorldMaterial = Material.AIR;

    public World setGravity(Vec2d newG) {
        this.gravity = newG;
        return this;
    }
}
