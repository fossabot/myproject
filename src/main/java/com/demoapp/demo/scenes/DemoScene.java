package com.demoapp.demo.scenes;

import com.demoapp.core.Application;
import com.demoapp.core.entity.Camera;
import com.demoapp.core.entity.GameObject;
import com.demoapp.core.services.physic.Material;
import com.demoapp.core.services.physic.PhysicType;
import com.demoapp.core.math.Vec2d;
import com.demoapp.core.services.io.InputHandler;
import com.demoapp.core.services.scene.AbstractScene;
import com.demoapp.core.services.scene.Scene;

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
        for (int i = 0; i < 20; i++) {
            GameObject pf = new GameObject("pf_" + i)
                    .setType(GameObject.ObjectType.RECTANGLE)
                    .setPhysicType(PhysicType.STATIC)
                    .setPosition(((int) (1 + (Math.random() * 38)) * 16), (int) (1 + (Math.random() * 11)) * 32)
                    .setDimension(((int) (Math.random() * 7) + 3) * 16, 8)
                    .setBorderColor(Color.DARK_GRAY)
                    .setFillColor(Color.GRAY)
                    .setMaterial(Material.STEEL)
                    .setMass(0.0)
                    .setLayer(2)
                    .setPriority(1);
            app.add(pf);
        }
        GameObject pfFloor = new GameObject("pf_floor")
                .setType(GameObject.ObjectType.RECTANGLE)
                .setPhysicType(PhysicType.STATIC)
                .setPosition(0.0, (25 * 16) - 8)
                .setDimension(40 * 16, 8)
                .setBorderColor(new Color(0.0f, 0.5f, 0.0f, 0.0f))
                .setFillColor(Color.GREEN)
                .setMaterial(Material.STEEL)
                .setMass(0.0)
                .setLayer(2)
                .setPriority(1);
        app.add(pfFloor);

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

        double step = 0.020;


        if (ih.isCtrlPressed()) {
            step *= 2;
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
