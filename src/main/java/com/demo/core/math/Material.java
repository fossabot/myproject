package com.demo.core.math;

/**
 * The {@link Material} Object is used for {@link com.demo.core.entity.GameObject}
 * updated by {@link PhysicEngine} computation.
 *
 * @author Frédéric Delorme
 * @version 1.0.0
 */
public class Material {

    public static final Material DEFAULT =
            new Material("default", 0.0, 1.0, 1.0);
    public static final Material RUBBER =
            new Material("rubber", 0.60, 0.7, 0.78);
    public static final Material SUPER_BALL =
            new Material("superBall", 0.98, 0.7, 0.78);
    public static final Material STEEL =
            new Material("steel", 0.12, 1.5, 0.9);
    /**
     * Name of this Material
     */
    public String name;
    /**
     * Elasticity physic attribute for this material.
     */
    public double elasticity = 1.0;
    /**
     * Density physic attribute for this material.
     */
    public double density = 1.0;
    /**
     * Friction physic attribute for this material.
     */
    public double friction = 1.0;


    public Material(String name) {
        this.name = name;
    }

    /**
     * Create new Material
     *
     * @param name name of this material
     * @param e    elasticity for this new material
     * @param d    density for this new material
     * @param f    friction for this new material
     */
    public Material(String name, double e, double d, double f) {
        setDensity(d);
        setElasticity(e);
        setFriction(f);
    }

    /**
     * set the material elasticity to <code>e</code>
     *
     * @param e the new elasticity for this {@link Material}.
     * @return the updated material.
     */
    public Material setElasticity(double e) {
        this.elasticity = e;
        return this;
    }

    /**
     * set the material density to <code>d</code>.
     *
     * @param d the new density for this {@link Material}.
     * @return the updated material.
     */
    public Material setDensity(double d) {
        this.density = d;
        return this;
    }

    /**
     * set the material friction to <code>f</code>.
     *
     * @param f the new friction for this {@link Material}.
     * @return the updated material.
     */
    public Material setFriction(double f) {
        this.friction = f;
        return this;
    }
}


