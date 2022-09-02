package com.demoapp.demo.scenes;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.demoapp.core.Application;
import com.demoapp.core.entity.Camera;
import com.demoapp.core.entity.GameObject;
import com.demoapp.core.math.Vec2d;
import com.demoapp.core.services.files.ResourceManager;
import com.demoapp.core.services.gfx.ImageUtils;
import com.demoapp.core.services.io.InputHandler;
import com.demoapp.core.services.io.OnKeyPressedHandler;
import com.demoapp.core.services.io.OnKeyReleaseHandler;
import com.demoapp.core.services.io.OnMouseClickHandler;
import com.demoapp.core.services.io.OnMouseWheelHandler;
import com.demoapp.core.services.physic.Material;
import com.demoapp.core.services.physic.PhysicType;
import com.demoapp.core.services.scene.AbstractScene;

public class DemoScene extends AbstractScene
        implements OnKeyReleaseHandler, OnKeyPressedHandler, OnMouseClickHandler, OnMouseWheelHandler {

    private int scoreValue = 0;

    double step = 0.020;
    private List<BufferedImage> tiles;

    @Override
    public String getName() {
        return "demo";
    }

    @Override
    public void load() {
        tiles = ImageUtils.splitImageToTile(
                ResourceManager.getImage("/images/tiles01.png"),
                0, 0, 16, 16);
    }

    @Override
    public void create(Application app) {
        super.create(app);
        // add the movable player
        GameObject player = new GameObject("player")
                .setType(GameObject.ObjectType.ELLIPSE)
                .setPosition(160.0, 100.0)
                .setDimension(16.0, 16.0)
                .setMass(100.0)
                .setMaterial(new Material("player", 0.98, 0.6, 0.95))
                .setLayer(10)
                .setPriority(1)
                .setBorderColor(Color.BLUE)
                .setFillColor(Color.CYAN)
                .setDebugLevel(2)
                .setAttribute("lives", 5);
        add(player);

        // add an object stick to camera
        GameObject score = new GameObject("score")
                .setType(GameObject.ObjectType.TEXT)
                .setPhysicType(PhysicType.NONE)
                .setPosition(8.0, 36.0)
                .setDimension(64.0, 16.0)
                .setBorderColor(Color.WHITE)
                .setFillColor(Color.CYAN)
                .setMass(0.0)
                .setLayer(10)
                .setPriority(1)
                .setStickToCamera(true)
                .setAttribute("textFormat", "%06d")
                .setAttribute("textValue", scoreValue)
                .setAttribute("textFontSize", 20.0f);
        add(score);

        // add an object stick to camera
        GameObject heart = new GameObject("heart")
                .setType(GameObject.ObjectType.IMAGE)
                .setPhysicType(PhysicType.NONE)
                .setImage(tiles.get(1))
                .setPosition(292.0, 20.0)
                .setDimension(14.0, 16.0)
                .setMass(0.0)
                .setLayer(10)
                .setPriority(1)
                .setStickToCamera(true)
                .setAttribute("textFontSize", 20.0f);
        add(heart);

        int liveValue = (int) player.attributes.get("lives");
        GameObject lives = new GameObject("lives")
                .setType(GameObject.ObjectType.TEXT)
                .setPhysicType(PhysicType.NONE)
                .setPosition(300.0, 36.0)
                .setDimension(64.0, 16.0)
                .setBorderColor(Color.WHITE)
                .setMass(0.0)
                .setLayer(10)
                .setPriority(1)
                .setStickToCamera(true)
                .setAttribute("textFormat", "x%01d")
                .setAttribute("textValue", liveValue)
                .setAttribute("textFontSize", 16.0f);
        add(lives);

        GameObject pfFloor = new GameObject("pf_floor")
                .setType(GameObject.ObjectType.RECTANGLE)
                .setPhysicType(PhysicType.STATIC)
                .setPosition(0.0, (25 * 16) - 8)
                .setDimension(40 * 16, 8)
                .setBorderColor(new Color(0.0f, 0.5f, 0.0f, 0.0f))
                .setFillColor(Color.GREEN)
                .setMaterial(Material.STEEL)
                .setMass(1000.0)
                .setLayer(1)
                .setPriority(1);
        add(pfFloor);

        generatePlatforms(app,
                20,
                40, 22,
                1,
                2, 2,
                8);

        // and the mandatory camera.
        camera = new Camera("cam01")
                .setTarget(player)
                .setTweenFactor(0.005)
                .setFOV(new Rectangle2D.Double(0, 0,
                        app.getConfiguration().getWindowDimension().getWidth(),
                        app.getConfiguration().getWindowDimension().getHeight()));
        addCamera(camera);
        app.getWindow().getInputHandler().addKeyReleasedHandler(this);
        app.getWindow().getInputHandler().addKeyPressedHandler(this);
    }

    /**
     * Generate platforms (in tile size of 16x16) in the areaPlatform[Width/Height]
     * with
     * a [min/max]PlatformWidth and a [min/max]PlatformHeight with respecting a
     * safeBorderWidth space.
     *
     * @param app                the parent Application
     * @param nbPlatforms        the nb of platforms to be generated
     * @param areaPlatformWidth  the width of the platform area (in tile)
     * @param areaPlatformHeight the height of the platform area (in tile)
     * @param safeBorderWidth    the safe space border around the platform area
     * @param minPlatformWidth   the minimal height of a platform (in tile)
     * @param minPlatformHeight  the minimal width of a platform (in tile)
     * @param maxPlatformWidth   the maximal Width of a platform (in tile)
     */
    private void generatePlatforms(Application app,
            int nbPlatforms,
            int areaPlatformWidth, int areaPlatformHeight,
            int safeBorderWidth,
            int minPlatformWidth, int minPlatformHeight,
            int maxPlatformWidth) {
        for (int i = 0; i < nbPlatforms; i++) {
            GameObject pf = new GameObject("pf_" + i)
                    .setType(GameObject.ObjectType.RECTANGLE)
                    .setPhysicType(PhysicType.STATIC)
                    .setPosition(
                            (int) (safeBorderWidth + (Math.random() * (areaPlatformWidth - (2 * safeBorderWidth))))
                                    * 16,
                            (int) (safeBorderWidth + (Math.random() * (areaPlatformHeight - (2 * safeBorderWidth) / 2)))
                                    * 32)
                    .setDimension(
                            ((int) (minPlatformWidth + Math.random() * (maxPlatformWidth - minPlatformWidth))) * 16,
                            16)
                    .setBorderColor(Color.DARK_GRAY)
                    .setFillColor(Color.GRAY)
                    .setMaterial(Material.STEEL)
                    .setMass(0.0)
                    .setLayer(1)
                    .setPriority(5);
            add(pf);
        }
    }

    @Override
    public void input(Application app) {
        GameObject player = app.getObject("player");
        InputHandler ih = app.getWindow().getInputHandler();
        Vec2d gravity = app.getPhysicEngine().getWorld().gravity;

        if (ih.isCtrlPressed()) {
            step *= 2.5;
        }
        if (ih.isShiftPressed()) {
            step *= 4;
        }
        // if no gravity, allow object to move up freely
        if (gravity.isZero()) {
            if (ih.getKey(KeyEvent.VK_UP)) {
                player.addForce(new Vec2d(0, -step));
            }
        }

        if (ih.getKey(KeyEvent.VK_DOWN)) {
            player.addForce(new Vec2d(0, step));
        }
        if (ih.getKey(KeyEvent.VK_LEFT)) {
            player.addForce(new Vec2d(-step, 0));
        }
        if (ih.getKey(KeyEvent.VK_RIGHT)) {
            player.addForce(new Vec2d(step, 0));
        }

        if (ih.getKey(KeyEvent.VK_ESCAPE)) {
            app.requestExit();
        }
        if (player.forces.size() == 0) {
            player.speed.multiply(player.material.friction);
        }
    }

    @Override
    public void onKeyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z:

                create(app);
                break;
            case KeyEvent.VK_D:
                int d = app.getConfiguration().getDebugLevel() + 1 < 6 ? app.getConfiguration().getDebugLevel() + 1 : 0;
                app.getConfiguration().setDebugLevel(d);
            default:
                break;
        }
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                double jumpImpulsion = app.getConfiguration().jupImpulsion;
                GameObject player = app.getObject("player");
                Vec2d gravity = app.getPhysicEngine().getWorld().gravity;
                if (!gravity.isZero()) {
                    Vec2d jumpVector = gravity
                            .multiply(-0.03) // attenuation factor
                            .multiply(jumpImpulsion); // jump factor
                    player.addForce(jumpVector);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void update(Application app, double elapsed) {
        scoreValue += 10;
        app.getObject("score")
                .setAttribute("textValue", scoreValue);
    }
}
