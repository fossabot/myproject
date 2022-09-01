package com.demoapp.core.entity;

import com.demoapp.core.Application;
import com.demoapp.core.math.Vec2d;
import com.demoapp.core.services.gfx.Renderer;
import com.demoapp.core.services.physic.Material;
import com.demoapp.core.services.physic.PhysicEngine;
import com.demoapp.core.services.physic.PhysicType;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

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
public class GameObject implements OnCollisionEvent {

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
        IMAGE,
        // draw a Text from textValue
        TEXT
    }

    /**
     * internal {@link GameObject} counter index .
     */
    private static long index = 0;

    /**
     * {@link GameObject} unique identifier
     */
    public long id = index++;
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
    public Material material = Material.DEFAULT;

    public boolean stickToCamera = false;
    public List<GameObject> colliders = new CopyOnWriteArrayList<>();
    private PhysicType physicType = PhysicType.DYNAMIC;
    private boolean alive = true;
    private int duration = -1;
    /**
     * Dimension of this GameObject
     */
    public double w;
    public double h;

    /**
     * All the boundary boxes used for collision of for debug purpose.
     */
    public Rectangle2D box = new Rectangle2D.Double(0, 0, 0, 0);
    /**
     * Internal GameObject boundary box.
     */
    public Shape offsetBox = new Rectangle2D.Double(0, 0, 0, 0);
    /**
     * collision box.
     */
    public Shape collisionBox = new Rectangle2D.Double(0, 0, 0, 0);
    /**
     * Collision flag.
     */
    public boolean collide;

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

    /**
     * Text to be drawn
     */
    public String textValue;

    /**
     * A possible font to render text.
     */
    public Font font;

    /**
     * Layer number where to display the object:
     * 
     * <pre>
     * <code>layer 0 -> n = front -> back</code>
     * </pre>
     */
    public int layer;

    /**
     * Rendering priority in the same layer:
     * 
     * <pre>
     * <code>0->n = back -> front</code>
     * </pre>
     */
    public int priority;

    /**
     * Map of specific attributes to be set at Scene creation, to add new values
     * attached to a {@link GameObject}
     */
    public Map<String, Object> attributes = new HashMap<>();

    /**
     * The debug level will set how the debug information must be
     * activated (debugLevel>0) and how many details to display (1=minimum to
     * 5=maximum).
     */
    public int debugLevel;

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
        box.setRect(pos.x, pos.y, w, h);
        setCollisionBox(0, 0, 0, 0);
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
     * Set {@link GameObject} acceleration <code>acc</code> with
     * <code>(ax,ay)</code>.
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
     * @param t the type of the {@link GameObject} to be set (see
     *          {@link ObjectType}.
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
     * Deinf the {@link PhysicType} for this {@link GameObject}.
     *
     * @param physicType the type of physic object to be correctly processed by the
     *                   {@link PhysicEngine}.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setPhysicType(PhysicType physicType) {
        this.physicType = physicType;
        return this;
    }

    /**
     * Flag to set {@link GameObject} fix on {@link Camera} viewport
     *
     * @param stick set to true, the object will be stick to camera viewport.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setStickToCamera(boolean stick) {
        stickToCamera = stick;
        return this;
    }

    /**
     * Retrieve the {@link PhysicType} of this {@link GameObject}.
     *
     * @return the current {@link GameObject#physicType} value.
     */
    public PhysicType getPhysicType() {
        return physicType;
    }

    /**
     * Return the computed alive value.
     *
     * @return true if the GameObject is alive (persistent or duration>0).
     */
    public boolean isAlive() {
        this.alive = isPersistent() || duration > 0;
        return this.alive;
    }

    /**
     * Is this object is a persistent one (no duration)
     *
     * @return true if duration==-1
     */
    public boolean isPersistent() {
        return this.duration == -1;
    }

    /**
     * Set the duration for this {@link GameObject}.
     *
     * @param d the new duration for this GameObject.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setDuration(int d) {
        this.duration = d;
        return this;
    }

    /**
     * Define the internal collision box for this GameObject.
     *
     * @param left   left marge for this collision box.
     * @param top    top marge for this collision box.
     * @param right  right marge for this collision box.
     * @param bottom bottom marge for this collision box.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setCollisionBox(double left, double top, double right, double bottom) {
        switch (type) {
            case IMAGE, RECTANGLE, default -> this.offsetBox = new Rectangle2D.Double(left, top, right, bottom);
            case ELLIPSE -> this.offsetBox = new Ellipse2D.Double(left, top, right, bottom);
        }
        update(0.0);
        return this;
    }

    /**
     * Add a new entry in the {@link GameObject#attributes} map, with
     * <code>key</code> and <code>value</code>.
     *
     * @param key   the key for this attribute.
     * @param value the value of this attribute.
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setAttribute(String key, Object value) {
        attributes.put(key, value);
        return this;
    }

    /**
     * Set the Debug level for this {@link GameObject}.
     *
     * @param level the debug leve (from 0=off to 5 max details).
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setDebugLevel(int level) {
        this.debugLevel = level;
        return this;
    }

    /**
     * Set the text to be displayed (if object is a {@link ObjectType#TEXT}.
     *
     * @param text the text to be displayed
     * @return the current {@link GameObject} updated (fluent API).
     */
    public GameObject setText(String text) {
        this.textValue = text;
        return this;
    }

    /**
     * update the {@link GameObject}
     *
     * @param elapsed the elapsed time since previous call.
     */
    public void update(double elapsed) {
        // update the bounding box.
        box.setRect(pos.x, pos.y, w - 1, h);
        updateText();

        // update the collision box.
        switch (type) {
            case RECTANGLE, TEXT, IMAGE, default -> this.collisionBox = new Rectangle2D.Double(
                    box.getX() + offsetBox.getBounds().getX(),
                    box.getY() + offsetBox.getBounds().getY(),
                    box.getWidth() - (offsetBox.getBounds().getWidth() + offsetBox.getBounds().getX()),
                    box.getHeight() - (offsetBox.getBounds().getHeight() + offsetBox.getBounds().getY()));
            case ELLIPSE -> this.collisionBox = new Ellipse2D.Double(
                    box.getX() + offsetBox.getBounds().getX(),
                    box.getY() + offsetBox.getBounds().getY(),
                    box.getWidth() - (offsetBox.getBounds().getWidth() + offsetBox.getBounds().getX()),
                    box.getHeight() - (offsetBox.getBounds().getHeight() + offsetBox.getBounds().getY()));
        }
    }

    /**
     * Convert the exiting attribute <code>textValue</code> with the existing
     * attribute <code>textFormat</code>
     * to feed {@link GameObject#textValue}. If none of those attributes exist,
     * nothing is done.
     */
    private void updateText() {
        if (attributes.containsKey("textFormat") && attributes.containsKey("textValue")) {
            this.textValue = String.format(
                    (String) attributes.get("textFormat"),
                    attributes.get("textValue"));
        }
    }

    /**
     * Define a specific processing in case of collision between this
     * {@link GameObject} (e1) and another one (e2).
     *
     * @param e1 the primary {@link GameObject} in collision.
     * @param e2 the secondary {@link GameObject} in the collision.
     */
    public void onCollision(GameObject e1, GameObject e2) {

    }

    /**
     * Prepare and set the debug information according to the
     * {@link GameObject#debugLevel} value.
     *
     * @return a list of debug information defined accordingly to the debugLevel for
     *         this {@link GameObject}.
     */
    public List<String> getDebugInformation() {
        List<String> debugInfo = new ArrayList<>();

        if (debugLevel > 0) {
            debugInfo.add(String.format("#%d", this.id));
            debugInfo.add(String.format(">name: %s", this.name));
            if (debugLevel > 1) {
                debugInfo.add(String.format(">>pos: %s", this.pos.toStringAsInt()));
                debugInfo.add(String.format(">>spd: %s", this.speed));
                debugInfo.add(String.format(">>acc: %s", this.acc));
                if (debugLevel > 2) {
                    debugInfo.add(String.format(">>>mat: %s", this.material.name));
                    if (debugLevel > 3) {
                        debugInfo.add(String.format(">>>>e=%1.02f", this.material.elasticity));
                        debugInfo.add(String.format(">>>>d=%1.02f", this.material.density));
                        debugInfo.add(String.format(">>>>f=%1.02f", this.material.friction));
                    }
                }
            }
        }
        return debugInfo;
    }
}
