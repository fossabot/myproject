package com.demoapp.core.entity;

/**
 * This interface allows the developer to implement a specific behavior on ea collision event between 2 object,
 * attached to the primary GameObject of the collision.
 */
public interface OnCollisionEvent {
    /**
     * Define a specific processing on the Collision Event between primary {@link GameObject} <code>o1</code>
     * and the secondary {@link GameObject} <code>o2</code>.
     *
     * @param o1 the primary {@link GameObject} of the collision event
     * @param o2 the secondary {@link GameObject} in the collision event
     */
    void onCollision(GameObject o1, GameObject o2);
}
