package com.demoapp.core.services.gfx;

import com.demoapp.core.Application;
import com.demoapp.core.entity.Camera;
import com.demoapp.core.services.config.Configuration;
import com.demoapp.core.entity.GameObject;
import com.demoapp.core.services.scene.Scene;

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
    private final Application app;

    // debug plate drawing parameters

    int debugXOffset = 10; // X offset for plat against the GameObject
    int debugYOffset = -10; // Y offset for plat against the GameObject
    int debugMargin = 4; // internal plate margin to render debug info text

    // rendering colors
    Color debugColorText = Color.CYAN;
    Color debugColorBackground = new Color(.0f, .0f, .5f, .7f);
    Color debugColorBorder = Color.BLUE;

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
     * @param app the parent {@link Application} object.
     */
    public Renderer(Application app) {
        this.app = app;
        Configuration config = app.getConfiguration();
        this.buffer = new BufferedImage(
                (int) config.getGameArea().getWidth(),
                (int) config.getGameArea().getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        /**
         * retrieve scale factor to be applied on rendering output.
         */
        this.scale = config.getScale();

        /**
         * Retrieve configuration for debug mode rendering
         */
        this.debugMargin = config.debugMargin;
        this.debugColorBorder = config.debugColorBorder;
        this.debugColorBackground = config.debugColorBackground;
        this.debugColorText = config.debugColorText;
        this.debugXOffset = config.debugXOffset;
        this.debugYOffset = config.debugYOffset;
    }

    /**
     * Draw all the objects from application to buffer.
     *
     * @param app the {@link Application} container
     */
    public void draw(Application app, int fps) {
        Scene scene = app.getSceneManager().getCurrent();
        Graphics2D g = buffer.createGraphics();
        g.setRenderingHints(Map.of(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
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
                .sorted((a, b) -> a.layer > b.layer ? (a.priority < b.priority ? 1 : -1) : -1)
                .forEach(o -> {
                    drawGameObject(app, scene, g, o);
                });
        drawDebugInformationLine(g, app, fps);
        // release Graphics component.
        g.dispose();
    }

    private void drawDebugInfo(Application app, Graphics2D g, GameObject o) {
        // compute the longest String
        int maxWidth = 0;
        g.setFont(g.getFont().deriveFont(9f));
        for (String s : o.getDebugInformation()) {
            maxWidth = g.getFontMetrics().stringWidth(s) > maxWidth ? g.getFontMetrics().stringWidth(s) : maxWidth;
        }
        // draw background
        g.setColor(debugColorBackground);
        g.fillRect(
                (int) (o.pos.x + o.w + debugXOffset),
                (int) (o.pos.y + debugYOffset - g.getFontMetrics().getHeight()),
                maxWidth + 2 * debugMargin,
                g.getFontMetrics().getHeight() * o.getDebugInformation().size() - 1 + (2 * debugMargin));
        // draw border
        g.setColor(debugColorBorder);
        g.drawRect(
                (int) (o.pos.x + o.w + debugXOffset),
                (int) (o.pos.y + debugYOffset - g.getFontMetrics().getHeight()),
                maxWidth + 2 * debugMargin,
                g.getFontMetrics().getHeight() * o.getDebugInformation().size() - 1 + (2 * debugMargin));

        // draw debug info text
        int index = 0;
        g.setColor(debugColorText);
        for (String s : o.getDebugInformation()) {
            s = s.replace('>', ' ');
            s = s.replace('<', '*');
            g.drawString(s,
                    (int) (o.pos.x + o.w + debugXOffset + (0.5 * debugMargin)),
                    (int) (o.pos.y + debugYOffset) + (int) (0.5 * debugMargin)
                            + (index * g.getFontMetrics().getHeight()));
            index++;
        }
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
    private void drawGameObject(Application app, Scene scene, Graphics2D g, GameObject o) {

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
            case TEXT -> {
                g.setColor(o.borderColor);
                if (Optional.ofNullable(o.font).isPresent()) {
                    g.setFont(o.font);
                }
                if (o.attributes.containsKey("textFontSize")) {
                    g.setFont(g.getFont().deriveFont((float) o.attributes.get("textFontSize")));
                }
                /**
                 * update the size of the drawn text into the `GameObject` itself.
                 */
                o.h = g.getFontMetrics().getHeight();
                o.w = g.getFontMetrics().stringWidth(o.textValue);
                if (Optional.ofNullable(o.textValue).isPresent()) {
                    g.drawString(o.textValue, (int) o.pos.x, (int) o.pos.y);
                }
            }
        }
        if (app.getConfiguration().getDebugLevel() > 0
                && o.debugLevel > 0) {
            drawDebugInfo(app, g, o);
        }
        if (Optional.ofNullable(currentCam).isPresent() && !o.stickToCamera) {
            g.translate(currentCam.pos.x, currentCam.pos.y);
        }

    }


    /**
     * Helper method on drawing simple shape.
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
                RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF));

        g.drawImage(buffer,
                0, 0,
                window.getWidth(),
                window.getHeight(),
                0, 0,
                (int) (window.getWidth() * (1.0 / this.scale)),
                (int) (window.getHeight() * (1.0 / this.scale)),
                null);
    }

    private void drawDebugInformationLine(Graphics2D g, Application app, int fps) {
        if (app.getConfiguration().getDebugLevel() > 0) {
            g.setFont(g.getFont().deriveFont(Font.BOLD, 10.0f));

            int dlYOffset = 8;
            int screenWidth = (int) app.getConfiguration().getWindowDimension().getWidth();
            int screenHeight = (int) app.getConfiguration().getWindowDimension().getHeight();
            int fh = g.getFontMetrics().getHeight();

            g.setColor(new Color(0.3f, 0.0f, 0.0f, 0.45f));
            g.fillRect(0, screenHeight - (fh + dlYOffset), screenWidth, fh + 8);
            g.setColor(Color.ORANGE);
            g.drawRect(1, screenHeight - (fh + dlYOffset), screenWidth + 2, fh + 8);
            g.setColor(Color.WHITE);
            g.drawString(String.format("[ dbg:%d | fps:%03d | obj:%04d | col:%03d ]",
                            app.getConfiguration().getDebugLevel(),
                            fps,
                            app.getObjects().size(),
                            app.getCollisionDetection().getCollisionCounter()
                    ),
                    8, screenHeight - dlYOffset - 2);
        }
    }
}
