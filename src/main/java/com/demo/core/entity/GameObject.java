package com.demo.core.entity;

import com.demo.core.Application;
import com.demo.core.math.Material;
import com.demo.core.math.PhysicType;
import com.demo.core.math.Vec2d;
import com.demo.core.services.gfx.Renderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A {@link GameObject} is a minimal entity managed by the {@link Application}
 * container.
 * Tt has physical computation attributes
 * </p>
 * <ul>
 * <li>position,</li>
 * <li>speed,</li>
 * <li>dimension</li>
 * </ul>
 * <p>
 * and some graphical attributes:
 * </p>
 * <ul>
 * <li>type (see {@link GameObject.ObjectType}),</li>
 * <li>fillColor,</li>
 * <li>borderColor,</li>
 * <li>image</li>
 * </ul>
 */
public class GameObject {


    public boolean stickToCamera=false;
    private PhysicType physicType;

    public GameObject setPhysicType(PhysicType physicType) {
        this.physicType = physicType;
        return this;
    }

    public GameObject setStickToCamera(boolean stick) {
        stickToCamera = stick;
        return this;
    }

    public PhysicType getPhysicType() {
        return physicType;
    }

    /**
     * The type of {@link GameObject} to be used, defining the rendering algorithm.
     */
    public enum ObjectType {
        // draw a simple point
        POINT,
        // draw a line
        LINE,
        // draw a rectangle
        RECTANGLE,
        // draw an ellipse
        ELLIPSE,
        // draw an image
        IMAGE
    }

    /**
     * internal {@link GameObject} counter index .
     */
    private static long index = 0;

    /**
     * {@link GameObject} unique identifier
     */
    private long id = index++;
    /**
     * {@link GameObject}'s name
     */
    private String name = "noname_" + id;

    /**
     * The {@link GameObject} type to draw.
     */
    public ObjectType type = ObjectType.RECTANGLE;

    /**
     * {@link GameObject}'s Physic attributes
     */
    public Vec2d pos = new Vec2d(0.0, 0.0);
    public Vec2d speed = new Vec2d(0.0, 0.0);
    public Vec2d acc = new Vec2d(0.0, 0.0);
    public double mass = 1.0;

    public List<Vec2d> forces = new ArrayList<>();

    /**
     * Material for this object
     */
    public Material material;

    /**
     * Dimension of this GameObject
     */
    public double w;
    public double h;

    /**
     * Border color (not for Image rendering)
     */
    public Color borderColor = Color.RED;
    /**
     * Filling color (not for Image rendering)
     */
    public Color fillColor = Color.ORANGE;

    /**
     * Image to be drawn if type is {@link ObjectType#IMAGE}.
     */
    public BufferedImage image;

    public int layer;
    public int priority;

    /**
     * Create a new {@link GameObject} named <code>name</code>.
     *
     * @param name the name of the new {@link GameObject} to be managed by the
     *             {@link Application} container.
     */
    public GameObject(String name) {
        physicType = PhysicType.DYNAMIC;
        this.name = name;
    }

    /**
     * Set {@link GameObject} position on <code>x</code> and <code>y</code>.
     *
     * @param x horizontal position of this {@link GameObject}.
     * @param y vertical position of this {@link GameObject}.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setPosition(double x, double y) {
        this.pos = new Vec2d(x, y);
        return this;
    }

    /**
     * Set {@link GameObject} dimension on width (<code>w</code>) and
     * height(<code>h</code>).
     *
     * @param w width of this {@link GameObject}.
     * @param h height of this {@link GameObject}.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setDimension(double w, double h) {
        this.w = w;
        this.h = h;
        return this;
    }

    /**
     * Set {@link GameObject} <code>speed</code> with <code>(dx,dy)</code>.
     *
     * @param dx horizontal speed of this GameObject.
     * @param dy vertical speed of this GameObject.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setSpeed(double dx, double dy) {
        this.speed = new Vec2d(dx, dy);
        return this;
    }

    /**
     * Set {@link GameObject} acceleration <code>acc</code> with <code>(ax,ay)</code>.
     *
     * @param ax horizontal speed of this {@link GameObject}.
     * @param ay vertical speed of this {@link GameObject}.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setAcc(double ax, double ay) {
        this.acc = new Vec2d(ax, ay);
        return this;
    }

    /**
     * Set the type of object for rendering purpose.
     *
     * @param t the type of the {@link GameObject} to be set (see {@link ObjectType}.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setType(ObjectType t) {
        this.type = t;
        return this;
    }

    /**
     * Set the layer were the {@link GameObject} must evolve
     *
     * @param l the layer number.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setLayer(int l) {
        this.layer = l;
        return this;
    }

    /**
     * Set the rendering priority in the layer of this {@link GameObject}.
     *
     * @param p the priority number
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setPriority(int p) {
        this.priority = p;
        return this;
    }

    /**
     * Retrieve {@link GameObject} name.
     *
     * @return a String for the current {@link GameObject} name.
     */
    public String getName() {
        return name;
    }

    /**
     * Draw the {@link GameObject} according to the required type.
     *
     * @param g the {@link Graphics2D} API to use, passed by the {@link Renderer}
     *          component.
     */
    public void draw(Graphics2D g) {

    }

    /**
     * set the Border color for this {@link GameObject}.
     *
     * @param c the {@link Color} to be used.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setBorderColor(Color c) {
        this.borderColor = c;
        return this;
    }

    /**
     * set the Fill color for the {@link GameObject}.
     *
     * @param c the {@link Color} to be used.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setFillColor(Color c) {
        this.fillColor = c;
        return this;
    }

    /**
     * set the mage to be used for this {@link GameObject} drawing
     *
     * @param bf the {@link BufferedImage} to be used for this {@link GameObject}.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setImage(BufferedImage bf) {
        this.image = bf;
        return this;
    }

    /**
     * Apply a force to this {@link GameObject}.
     * 
     * @param f the force to be applied.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject addForce(Vec2d f) {
        this.forces.add(f);
        return this;
    }

    /**
     * set the Mass for thi {@link GameObject}.
     *
     * @param m the new mass for this object.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setMass(double m) {
        this.mass = m;
        return this;
    }

    /**
     * Set the material for this {@link GameObject}.
     *
     * @param m the new Material of this {@link GameObject}.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setMaterial(Material m) {
        this.material = m;
        return this;
    }

    /**
     * update the {@link GameObject}
     *
     * @param elapsed the elapsed time since previous call.
     */
    public void update(double elapsed) {
    }
}
