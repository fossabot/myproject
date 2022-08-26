package com.demo.scenes;

import com.demo.core.Application;
import com.demo.core.entity.Camera;
import com.demo.core.entity.GameObject;
import com.demo.core.math.Material;
import com.demo.core.math.PhysicType;
import com.demo.core.math.Vec2d;
import com.demo.core.services.io.InputHandler;
import com.demo.core.services.scene.AbstractScene;
import com.demo.core.services.scene.Scene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class DemoScene extends AbstractScene implements Scene {

    @Override
    public String getName() {
        return "demo";
    }

    @Override
    public void create(Application app) {
        // add the movable player
        GameObject player = new GameObject("player")
                .setType(GameObject.ObjectType.RECTANGLE)
                .setPosition(160.0, 100.0)
                .setDimension(16.0, 16.0)
                .setMass(100.0)
                .setMaterial(Material.RUBBER)
                .setLayer(1)
                .setPriority(1);
        app.add(player);

        // add an object stick to camera
        GameObject score = new GameObject("score")
                .setType(GameObject.ObjectType.RECTANGLE)
                .setPhysicType(PhysicType.NONE)
                .setPosition(10.0, 20.0)
                .setDimension(64.0, 16.0)
                .setBorderColor(Color.BLACK)
                .setFillColor(Color.CYAN)
                .setMass(0.0)
                .setLayer(0)
                .setPriority(1)
                .setStickToCamera(true);
        app.add(score);

        // draw some platforms
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
    }

    /**
     * Generate platforms (in tile size of 16x16) in the areaPlatform[Width/Height] with
     * a [min/max]PlatformWidth and a [min/max]PlatformHeight with respecting a safeBorderWidth space.
     *
     * @param app                the parent Application
     * @param nbPlatforms        the nb of platforms to be generated
     * @param areaPlatformWidth  the width of the platform area (in tile)
     * @param areaPlatformHeight the height of the platform area (in tile)
     * @param safeBorderWidth    the safe space border around the platform area
     * @param minPlatformWidth   the minimal height  of a platform (in tile)
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
                            (int) (safeBorderWidth + (Math.random() * (areaPlatformWidth - (2 * safeBorderWidth)))) * 16,
                            (int) (safeBorderWidth + (Math.random() * (areaPlatformHeight - (2 * safeBorderWidth) / 2))) * 32)
                    .setDimension(
                            ((int) (minPlatformWidth + Math.random() * (maxPlatformWidth - minPlatformWidth))) * 16,
                            16)
                    .setBorderColor(Color.DARK_GRAY)
                    .setFillColor(Color.GRAY)
                    .setMaterial(Material.DEFAULT)
                    .setMass(0.0)
                    .setLayer(2)
                    .setPriority(1);
            app.add(pf);
        }
    }

    @Override
    public void input(Application app) {
        GameObject player = app.getObject("player");
        InputHandler ih = app.getWindow().getInputHandler();

        double step = 0.030;

        if (ih.isCtrlPressed()) {
            step *= 2.5;
        }
        if (ih.isShiftPressed()) {
            step *= 4;
        }
        if (ih.getKey(KeyEvent.VK_UP)) {
            player.addForce(new Vec2d(0, -5 * step));
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
    }

}
