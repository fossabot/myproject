package com.demo.scenes;

import com.demo.core.Application;
import com.demo.core.entity.Camera;
import com.demo.core.entity.GameObject;
import com.demo.core.services.io.InputHandler;
import com.demo.core.services.scene.AbstractScene;
import com.demo.core.services.scene.Scene;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class DemoScene extends AbstractScene implements Scene {

    @Override
    public String getName() {
        return "demo";
    }

    @Override
    public void create(Application app) {
        GameObject player = new GameObject("player")
                .setType(GameObject.ObjectType.RECTANGLE)
                .setPosition(160.0, 100.0)
                .setDimension(16.0, 16.0)
                .setSpeed(0.0, 0.0)
                .setLayer(1)
                .setPriority(1);
        app.add(player);

        camera = (Camera) new Camera("cam01")
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
        double dx = 0;
        double dy = 0;

        double step = 0.0000001;

        if (ih.isCtrlPressed()) {
            step *= 2;
        }
        if (ih.isShiftPressed()) {
            step *= 4;
        }

        if (ih.getKey(KeyEvent.VK_UP)) {
            dy = -step;
        }
        if (ih.getKey(KeyEvent.VK_DOWN)) {
            dy = step;
        }
        if (ih.getKey(KeyEvent.VK_LEFT)) {
            dx = -step;
        }
        if (ih.getKey(KeyEvent.VK_RIGHT)) {
            dx = step;
        }

        if (ih.getKey(KeyEvent.VK_ESCAPE)) {
            app.requestExit();
        }
        player.setSpeed(dx, dy);
    }

}
