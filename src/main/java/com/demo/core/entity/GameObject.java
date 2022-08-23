package com.demo.core.entity;

import com.demo.core.Application;
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

    /**
     * THe type of GameObject to be used, defining the rendering algorithm.
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
     * internal gameObject counter index .
     */
    private static long index = 0;

    /**
     * GameObject unique identifier
     */
    private long id = index++;
    /**
     * GameObject's name
     */
    private String name = "noname_" + id;

    /**
     * The GameObject type to draw.
     */
    public ObjectType type = ObjectType.RECTANGLE;

    /**
     * Physic attributes
     */
    public Vec2d pos = new Vec2d(0.0, 0.0);
    public Vec2d speed = new Vec2d(0.0, 0.0);
    public Vec2d acc = new Vec2d(0.0, 0.0);
    public double mass = 1.0;

    public List<Vec2d> forces = new ArrayList<>();

    /**
     * Material characteristics
     */
    private double elasticity = 1.0;

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
        this.name = name;
    }

    /**
     * Set GameObject position on <code>x</code> and <code>y</code>.
     *
     * @param x horizontal position of this GameObject.
     * @param y vertical position of this GameObject.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setPosition(double x, double y) {
        this.pos = new Vec2d(x, y);
        return this;
    }

    /**
     * Set GameObject dimension on width (<code>w</code>) and
     * height(<code>h</code>).
     *
     * @param w width of this GameObject.
     * @param h height of this GameObject.
     * @return the current GameObject updated (fluent API).
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
     * Set GameObject acceleration <code>acc</code> with <code>(ax,ay)</code>.
     *
     * @param ax horizontal speed of this GameObject.
     * @param ay vertical speed of this GameObject.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setAcc(double ax, double ay) {
        this.acc = new Vec2d(ax, ay);
        return this;
    }

    /**
     * Set the type of object for rendering purpose.
     *
     * @param t the type of the object to be set (see {@link ObjectType}.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setType(ObjectType t) {
        this.type = t;
        return this;
    }

    /**
     * Set the layer were the object must evolve
     *
     * @param l the layer number.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setLayer(int l) {
        this.layer = l;
        return this;
    }

    /**
     * Set the rendering priority in the layer of this object.
     *
     * @param p the priority number
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setPriority(int p) {
        this.priority = p;
        return this;
    }

    /**
     * Retrieve GaeObject name.
     *
     * @return a String for the current GameObject name.
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
     * set the Border color
     *
     * @param c the {@link Color} to be used.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setBorderColor(Color c) {
        this.borderColor = c;
        return this;
    }

    /**
     * set the Fill color
     *
     * @param c the {@link Color} to be used.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setFillColor(Color c) {
        this.fillColor = c;
        return this;
    }

    /**
     * set the mage to be used for this GameObject drawing
     *
     * @param bf
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setImage(BufferedImage bf) {
        this.image = bf;
        return this;
    }

    /**
     * Set mass for thie GameObject.
     * 
     * @param m the new mass for this object.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setMass(double m) {
        this.mass = m;
        return this;
    }

    /**
     * Set the material elasticity for thie GameObject.
     * 
     * @param e the new Elasticity for the material of this GameObject.
     * @return
     */
    public GameObject setElasticity(double e) {
        this.elasticity = e;
        return this;
    }

    /**
     * Apply a force to this <code>GameObject</code>.
     * 
     * @param f the force to be applied.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject addForce(Vec2d f) {
        this.forces.add(f);
        return this;
    }

    /**
     * Update physic and display attributes according to newton's laws.
     *
     * @param elapsed the elapsed time since previous call.
     */
    public void update(double elapsed) {
        double t = elapsed / 1000000.0;
        Vec2d force = new Vec2d(0, 0);
        for (Vec2d f : forces) {
            force.add(f);
        }
        acc = force.multiply(t / mass);
        speed = speed.add(acc.multiply(0.5 * t));
        pos = pos.add(speed.multiply(t));
        forces.clear();
    }

    /**
     * Contrains the current object into a Rectangle area.
     *
     * @param area the play area for the application.
     * @return true if constrained, else false.
     */
    public boolean constrainedBy(Rectangle2D area) {
        boolean collide = false;
        double fx = 1.0, fy = 1.0;
        if (!area.contains(this.pos.x, this.pos.y, w, h)) {
            if (this.pos.x < area.getX()) {
                this.pos.x = area.getX();
                fx = -1.0;
                collide = true;
            }
            if (this.pos.x > area.getWidth() - w) {
                this.pos.x = area.getWidth() - w;
                fx = -1.0;
                collide = true;
            }
            if (this.pos.y < area.getY()) {
                this.pos.y = area.getY();
                fy = -1.0;
                collide = true;
            }
            if (this.pos.y > area.getHeight() - h) {
                this.pos.y = area.getHeight() - h;
                fy = -1.0;
                collide = true;
            }
        }
        if (collide) {
            this.forces.clear();
            this.acc = new Vec2d(0, 0);
            this.speed.x *= fx * this.elasticity;
            this.speed.y *= fy * this.elasticity;
        }
        return collide;
    }

}
