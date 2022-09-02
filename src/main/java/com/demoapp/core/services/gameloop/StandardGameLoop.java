package com.demoapp.core.services.gameloop;

import com.demoapp.core.Application;

import java.util.Optional;

public class StandardGameLoop implements GameLoop {

    protected long previousTime = System.nanoTime();
    protected double cycleTime = 0.0;
    protected int fps = 0;
    private int fpsCount = 0;

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
        if (!app.isPaused()) {
            update(app, elapsed);
            render(app, elapsed);
        }
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
        app.getCollisionDetection().update(elapsed);
        app.getSceneManager().getCurrent().update(app, elapsed);
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
        app.getRender().draw(app, fps);
        app.getRender().drawToWindow(app.getWindow());
        computeFps(elapsed);
    }

    private void computeFps(double elapsed) {
        cycleTime += elapsed;
        fpsCount += 1;
        if (cycleTime > 1000000000) {
            cycleTime = 0.0;
            fps = fpsCount;
            fpsCount = 0;
        }
    }
}
