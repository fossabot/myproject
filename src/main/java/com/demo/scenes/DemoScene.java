package com.demo.scenes;

import com.demo.core.Application;
import com.demo.core.entity.GameObject;
import com.demo.core.math.Vec2d;
import com.demo.core.services.io.InputHandler;
import com.demo.core.services.scene.Scene;

import java.awt.event.KeyEvent;

public class DemoScene implements Scene {

    @Override
    public String getName() {
        return "demo";
    }

    @Override
    public void create(Application app) {
        app.add(new GameObject("player")
                .setType(GameObject.ObjectType.RECTANGLE)
                .setPosition(160.0, 100.0)
                .setDimension(16.0, 16.0)
                .setMass(100.0)
                .setElasticity(0.13)
                .setLayer(1)
                .setPriority(1));
    }

    @Override
    public void input(Application app) {
        GameObject player = app.getObject("player");
        InputHandler ih = app.getWindow().getInputHandler();

        double step = 0.5;
        if (ih.getKey(KeyEvent.VK_UP)) {
            player.addForce(new Vec2d(0, -step));
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
