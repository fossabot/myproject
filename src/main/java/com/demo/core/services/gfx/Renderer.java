package com.demo.core.services.gfx;

import com.demo.core.Application;
import com.demo.core.entity.Camera;
import com.demo.core.services.config.Configuration;
import com.demo.core.entity.GameObject;
import com.demo.core.services.scene.Scene;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * The {@link Renderer} component will support all drawing processing for the game.
 *
 * @author Frédéric Delorme
 * @version 1.0
 */
public class Renderer {
    private final BufferedImage buffer;
    private double scale = 1.0;

    /**
     * Initialize the Renderer component according to defined configuration key :
     * <ul>
     *     <li></li>
     * </ul>
     *
     * @param config the current application {@link Configuration} object.
     */
    public Renderer(Configuration config) {
        this.buffer = new BufferedImage((int) config.getGameArea().getWidth(), (int) config.getGameArea().getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.scale = config.getScale();
    }

    /**
     * Draw all the objects from application to buffer.
     *
     * @param app the {@link Application} container
     */
    public void draw(Application app, Scene scene) {
        Graphics2D g = buffer.createGraphics();
        clearBuffer(g, Color.BLACK, 0, 0, buffer.getWidth(), buffer.getHeight());

        Camera currentCam = scene.getCamera();
        if (Optional.ofNullable(currentCam).isPresent()) {
            g.translate(-currentCam.x, -currentCam.y);
        }
        // draw game area zone
        drawPlayArea(g);

        // draw things with higher layer / higher priority draw first.
        app.getObjects().stream()
                .sorted((a, b) -> a.layer > b.layer ? (a.priority > b.priority ? 1 : -1) : -1)
                .forEach(o -> drawGameObject(g, o));

        if (Optional.ofNullable(currentCam).isPresent()) {
            g.translate(currentCam.x, currentCam.y);
        }

        // release Graphics component.
        g.dispose();
    }

    private void drawPlayArea(Graphics2D g) {
        g.setColor(Color.BLUE);
        for (int tx = 0; tx < buffer.getWidth(); tx += 16) {
            g.drawRect(tx, 0, 16, buffer.getHeight());
        }
        for (int ty = 0; ty < buffer.getHeight(); ty += 16) {
            g.drawRect(0, ty, buffer.getWidth(), 16);
        }
    }

    private void clearBuffer(Graphics2D g, Color clearColor, int x, int x1, int buffer, int buffer1) {
        // clear rendering area.
        g.setColor(clearColor);
        g.fillRect(x, x1, buffer, buffer1);
    }

    /**
     * Draw one GameObject according to its drawing {@link GameObject.ObjectType}
     *
     * @param g the Graphics API
     * @param o the {@link Application} container
     */
    private void drawGameObject(Graphics2D g, GameObject o) {
        switch (o.type) {
            case POINT -> {
                clearBuffer(g, o.borderColor, (int) o.x, (int) o.y, 1, 1);
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
            case IMAGE -> g.drawImage(o.image, (int) o.x, (int) o.y, null);
        }
    }

    /**
     * helper on drawing simple shape.
     *
     * @param g     the Graphics2D API to be used.
     * @param o     the GameObject to be drawn
     * @param shape the current shape for this GameObject
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
                0, 0,
                window.getWidth(),
                window.getHeight(),
                0, 0,
                (int) (window.getWidth() * (1.0 / this.scale)),
                (int) (window.getHeight() * (1.0 / this.scale)),
                null
        );
    }
}
