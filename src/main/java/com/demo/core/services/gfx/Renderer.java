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
import java.util.Map;
import java.util.Optional;

/**
 * The {@link Renderer} component will support all drawing processing for the
 * game.
 *
 * @author Frédéric Delorme
 * @version 1.0
 */
public class Renderer {
    private final BufferedImage buffer;
    private final double scale;

    /**
     * Initialize the {@link Renderer} component according to defined configuration
     * key :
     * <p>
     * 2 configuration attributes are defining the internal graphic buffer size:
     * <ul>
     * <li><code>app.window.width</code> set its width,</li>
     * <li><code>app.window.height</code> set its height.</li>
     * </ul>
     *
     * @param config the current application {@link Configuration} object.
     */
    public Renderer(Configuration config) {
        this.buffer = new BufferedImage(
                (int) config.getGameArea().getWidth(),
                (int) config.getGameArea().getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        this.scale = config.getScale();
    }

    /**
     * Draw all the objects from application to buffer.
     *
     * @param app the {@link Application} container
     */
    public void draw(Application app, Scene scene) {
        Graphics2D g = buffer.createGraphics();
        g.setRenderingHints(Map.of(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        ));
        fillRect(g, Color.BLACK,
                0, 0,
                buffer.getWidth(), buffer.getHeight());

        Camera currentCam = scene.getCamera();
        if (Optional.ofNullable(currentCam).isPresent()) {
            g.translate(-currentCam.pos.x, -currentCam.pos.y);
        }
        // draw game area zone
        drawPlayArea(g);

        if (Optional.ofNullable(currentCam).isPresent()) {
            g.translate(currentCam.pos.x, currentCam.pos.y);
        }

        // draw things with higher layer / higher priority draw first.
        app.getObjects().stream()
                .sorted((a, b) -> a.layer > b.layer ? (a.priority > b.priority ? 1 : -1) : -1)
                .forEach(o -> drawGameObject(scene, g, o));


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

    private void fillRect(Graphics2D g, Color clearColor, int x, int y, int width, int height) {
        // clear rendering area.
        g.setColor(clearColor);
        g.fillRect(x, y, width, height);
    }

    /**
     * Draw one {@link GameObject} according to its drawing
     * {@link GameObject.ObjectType}
     *
     * @param g the Graphics API (see {@link Graphics2D}
     * @param o the {@link Application} container
     */
    private void drawGameObject(Scene scene, Graphics2D g, GameObject o) {

        Camera currentCam = scene.getCamera();
        if (Optional.ofNullable(currentCam).isPresent() && !o.stickToCamera) {
            g.translate(-currentCam.pos.x, -currentCam.pos.y);
        }


        switch (o.type) {
            case POINT -> {
                g.setColor(o.borderColor);
                g.fillRect((int) o.pos.x, (int) o.pos.y, 1, 1);
            }
            case LINE -> {
                g.setColor(o.borderColor);
                g.drawLine(
                        (int) o.pos.x, (int) o.pos.y,
                        (int) (o.pos.x + o.w), (int) (o.pos.y + o.h));
            }
            case RECTANGLE -> {
                Rectangle2D rect = new Rectangle2D.Double(
                        o.pos.x, o.pos.y,
                        o.w, o.h);
                drawShape(g, o, rect);
            }
            case ELLIPSE -> {
                Ellipse2D ellipse = new Ellipse2D.Double(
                        o.pos.x, o.pos.y,
                        o.w, o.h);
                drawShape(g, o, ellipse);
            }
            case IMAGE -> g.drawImage(o.image,
                    (int) o.pos.x, (int) o.pos.y,
                    null);
        }

        if (Optional.ofNullable(currentCam).isPresent() && !o.stickToCamera) {
            g.translate(currentCam.pos.x, currentCam.pos.y);
        }

    }

    /**
     * helper on drawing simple shape.
     *
     * @param g     the {@link Graphics2D} API to be used.
     * @param o     the {@link GameObject} to be drawn
     * @param shape the current shape for this GameObject
     */
    private void drawShape(Graphics2D g, GameObject o, Shape shape) {
        g.setColor(o.fillColor);
        g.fill(shape);
        g.setColor(o.borderColor);
        g.draw(shape);
    }

    /**
     * Copy rendering buffer content to {@link Window}
     *
     * @param window the window where to copy buffer to.
     */
    public void drawToWindow(Window window) {
        Graphics2D g = (Graphics2D) window.getGraphics();
        g.setRenderingHints(Map.of(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF,
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
        ));

        g.drawImage(buffer,
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
