package com.demo.core;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * The {@link Renderer} component will support all drawing processing for the game.
 *
 * @author Frédéric Delorme
 * @version 1.0
 */
public class Renderer {
    private final BufferedImage buffer;

    /**
     * Initialize the Renderer component according to defined configuration key :
     * <ul>
     *     <li></li>
     * </ul>
     *
     * @param config
     */
    public Renderer(Configuration config) {
        this.buffer = new BufferedImage((int) config.getGameArea().getWidth(), (int) config.getGameArea().getHeight(), BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Draw all the objects from application to buffer.
     *
     * @param app the {@link Application} container
     */
    public void draw(Application app) {
        Graphics2D g = buffer.createGraphics();
        // clear rendering area.
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
        // draw things with higher layer / higher priority draw first.
        app.getObjects().stream()
                .sorted((a, b) -> a.layer > b.layer ? (a.priority > b.priority ? 1 : -1) : -1)
                .forEach(o -> drawGameObject(g, o));
        // release Graphics component.
        g.dispose();
    }

    /**
     * Draw one GameObject according to its drawing {@link com.demo.core.GameObject.ObjectType}
     *
     * @param g the Graphics API
     * @param o the {@link Application} container
     */
    private void drawGameObject(Graphics2D g, GameObject o) {
        switch (o.type) {
            case POINT -> {
                g.setColor(o.borderColor);
                g.fillRect((int) o.x, (int) o.y, 1, 1);
            }
            case LINE -> {
                g.setColor(o.borderColor);
                g.drawLine((int) o.x, (int) o.y, (int) (o.x + o.w), (int) (o.y + o.h));
            }
            case RECTANGLE -> {
                Rectangle2D rect = new Rectangle2D.Double(o.x, o.y, o.w, o.h);
                drawShape(g, o, rect);
            }
            case ELLIPSE -> {
                Ellipse2D ellipse = new Ellipse2D.Double(5, 10, 100, 150);
                drawShape(g, o, ellipse);
            }
            case IMAGE -> {
                g.drawImage(o.image, (int) o.x, (int) o.y, null);
            }
        }
    }

    /**
     * helper on drawing simple shape.
     *
     * @param g
     * @param o
     * @param shape
     */
    private void drawShape(Graphics2D g, GameObject o, Shape shape) {
        g.setColor(o.fillColor);
        g.fill(shape);
        g.setColor(o.borderColor);
        g.draw(shape);
    }

    /**
     * Copy rendering buffer content  to {@link Window}
     *
     * @param window the window where to copy buffer to.
     */
    public void drawToWindow(Window window) {
        window.getGraphics().drawImage(buffer,
                0, 0, window.getWidth(), window.getHeight(),
                0, 0, buffer.getWidth(), buffer.getHeight(),
                null
        );
    }
}
