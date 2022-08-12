package com.demo.core.entity;

import com.demo.core.Application;
import com.demo.core.services.gfx.Renderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

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
    public ObjectType type;
    /**
     * Position of this GameObject
     */
    public double x;
    public double y;
    /**
     * Speed of this GameObject
     */
    public double dx;

    public double dy;
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
        this.x = x;
        this.y = y;
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
     * Set GameObject speed on <code>x</code> and <code>y</code>.
     *
     * @param dx horizontal speed of this GameObject.
     * @param dy vertical speed of this GameObject.
     * @return the current GameObject updated (fluent API).
     */
    public GameObject setSpeed(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
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
     * @return the current GameObject updated (fluent API.
     */
    public GameObject setBorderColor(Color c) {
        this.borderColor = c;
        return this;
    }

    /**
     * set the Fill color
     *
     * @param c the {@link Color} to be used.
     * @return the current GameObject updated (fluent API.
     */
    public GameObject setFillColor(Color c) {
        this.fillColor = c;
        return this;
    }

    /**
     * set the mage to be used for this GameObject drawing
     *
     * @param bf
     * @return the current GameObject updated (fluent API.
     */
    public GameObject setImage(BufferedImage bf) {
        this.image = bf;
        return this;
    }

    /**
     * Update physic and display attributes according to newton's laws.
     *
     * @param elapsed the elapsed time since previous call.
     */
    public void update(long elapsed) {
        x += dx * elapsed;
        y += dy * elapsed;
    }

    /**
     * Contrains the current object into a Rectangle area.
     *
     * @param area the play area for the application.
     * @return true if constrained, else false.
     */
    public boolean constrainedBy(Rectangle2D area) {
        if (area.contains(x, y, w, h)) {
            return false;
        } else {
            if (x < area.getX()) {
                x = area.getX();
                return true;
            }
            if (x > area.getWidth() - w) {
                x = area.getWidth() - w;
                return true;
            }
            if (y < area.getY()) {
                y = area.getY();
                return true;
            }
            if (y > area.getHeight() - h) {
                y = area.getHeight() - h;
                return true;
            }
            return true;
        }
    }

}
