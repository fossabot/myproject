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
        GameObject stickToCam = new GameObject("stick")
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
        app.add(stickToCam);

        // draw some platforms
        for (int i = 0; i < 10; i++) {
            GameObject pf = new GameObject("pf_" + i)
                    .setType(GameObject.ObjectType.RECTANGLE)
                    .setPhysicType(PhysicType.STATIC)
                    .setPosition(((int)(Math.random() * 38) * 16)+1, (int)((Math.random() * 22) * 16)+1)
                    .setDimension(((int)(Math.random() * 5) + 1) * 16, 16)
                    .setBorderColor(Color.DARK_GRAY)
                    .setFillColor(Color.GRAY)
                    .setMaterial(Material.DEFAULT)
                    .setMass(0.0)
                    .setLayer(2)
                    .setPriority(1);
            app.add(pf);
        }

        // and the mandatory camera.
        camera = new Camera("cam01")
                .setTarget(player)
                .setTweenFactor(0.005)
                .setFOV(new Rectangle2D.Double(0, 0,
                        app.getConfiguration().getWindowDimension().getWidth(),
                        app.getConfiguration().getWindowDimension().getHeight()));
        addCamera(camera);
    }

    @Override
    public void input(Application app) {
        GameObject player = app.getObject("player");
        InputHandler ih = app.getWindow().getInputHandler();

        double step = 0.040;


        if (ih.isCtrlPressed()) {
            step *= 2;
        }
        if (ih.isShiftPressed()) {
            step *= 4;
        }
        if (ih.getKey(KeyEvent.VK_UP)) {
            player.addForce(new Vec2d(0, -4 * step));

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
