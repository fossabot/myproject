package com.demo.core;

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
                .setSpeed(0.0, 0.0)
                .setLayer(1)
                .setPriority(1)
        );
    }

    @Override
    public void input(Application app) {
        GameObject player = app.getObject("player");
        InputHandler ih = app.getWindow().getInputHandler();
        double dx = 0;
        double dy = 0;

        double step = 0.0000001;
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
