package com.demo.core;

import java.awt.event.KeyEvent;

public class StandardGameLoop {

    long previousTime = System.nanoTime();

    /**
     * Process game loop and return duration time for this cycle.
     *
     * @param app
     * @return
     */
    public long process(Application app) {
        long startTime = System.nanoTime();

        input(app);

        long elapsed = startTime - previousTime;
        update(app, elapsed);
        render(app, elapsed);

        previousTime = startTime;
        return System.nanoTime() - startTime;
    }

    /**
     * Manage input from the application window to update actions on concerned GameObject.
     *
     * @param app the parent application this GameLoop is attached to
     */
    private void input(Application app) {


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

    /**
     * Update all application GameObject list
     *
     * @param app     the parent application this GameLoop is attached to
     * @param elapsed the elapsed time since previous call.
     */
    private void update(Application app, long elapsed) {
        app.getObjects().forEach(go -> {
            go.update(elapsed);
            go.contrainedBy(app.getGameArea());
        });
    }

    /**
     * Render  all application GameObject list
     *
     * @param app     the parent application this GameLoop is attached to
     * @param elapsed the elapsed time since previous call.
     */
    private void render(Application app, long elapsed) {
        app.getRender().draw(app);
        app.getRender().drawToWindow(app.getWindow());
    }


}
