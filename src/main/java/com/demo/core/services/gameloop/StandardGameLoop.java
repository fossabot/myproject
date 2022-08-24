package com.demo.core.services.gameloop;

import com.demo.core.Application;

import java.util.Optional;

public class StandardGameLoop implements GameLoop {

    protected long previousTime = System.nanoTime();

    /**
     * Process game loop and return duration time for this cycle.
     *
     * @param app the container {@link Application}
     * @return the measured execution duration time
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
    public void update(Application app, double elapsed) {
        app.getPhysicEngine().update(elapsed);
        app.getSceneManager().getCurrent().getCamera().update(elapsed);
    }

    /**
     * Render  all application GameObject list
     *
     * @param app     the parent application this GameLoop is attached to
     * @param elapsed the elapsed time since previous call.
     */
    @Override
    public void render(Application app, double elapsed) {
        app.getRender().draw(app, app.getSceneManager().getCurrent());
        app.getRender().drawToWindow(app.getWindow());
    }
}
