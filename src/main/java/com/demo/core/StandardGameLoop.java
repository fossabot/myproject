package com.demo.core;

import java.awt.event.KeyEvent;
import java.util.Optional;

public class StandardGameLoop implements GameLoop {

    long previousTime = System.nanoTime();

    /**
     * Process game loop and return duration time for this cycle.
     *
     * @param app
     * @return
     */
    @Override
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
    @Override
    public void input(Application app) {
        if (Optional.ofNullable(app.getSceneManager().getCurrent()).isPresent()) {
            app.getSceneManager().getCurrent().input(app);
        }
    }

    /**
     * Update all application GameObject list
     *
     * @param app     the parent application this GameLoop is attached to
     * @param elapsed the elapsed time since previous call.
     */
    @Override
    public void update(Application app, long elapsed) {
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
    @Override
    public void render(Application app, long elapsed) {
        app.getRender().draw(app);
        app.getRender().drawToWindow(app.getWindow());
    }


}
