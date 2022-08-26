package com.demoapp.core.services.physic;

/**
 * The Physic Type enumeration is used by the {@link PhysicEngine} and {@link com.demoapp.core.services.collision.CollisionDetection}
 * to process physic computation laws on {@link com.demoapp.core.entity.GameObject} according to this
 * kind of <code>physic node</code> it belongs to.
 *
 * @author Frédéric Delorme
 * @version 1.0.0
 */
public enum PhysicType {
    /**
     * No specific type and must not been take in account in the physic computation engine.
     */
    NONE,
    /**
     * This object is static and will not move, but be considered as constrain for other DYNAMIC node in
     * the CollisionDetection service..
     */
    STATIC,
    /**
     * This object is DYNAMIC, and move according to some forces applied on itself. It must be considered as a
     * moving object and will be compared to other DYNAMIC and STATIC objects.
     */
    DYNAMIC
}
