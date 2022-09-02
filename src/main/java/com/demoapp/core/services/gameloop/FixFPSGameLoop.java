package com.demoapp.core.services.gameloop;

import com.demoapp.core.Application;

/**
 * A FPS contant {@link GameLoop} implementation for the Application.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class FixFPSGameLoop extends StandardGameLoop {

    /**
     * Target Frame Per Second time rate
     */
    private final double frameTime;

    /**
     * Create the {@link FixFPSGameLoop} with the targeted fps
     * *
     *
     * @param fps the requested Frame Per Second rate
     */
    public FixFPSGameLoop(double fps) {
        this.frameTime = 1000000.0 / fps;
    }

    /**
     * The Fix FPS process implementation
     *
     * @param app the parent {@link Application} hosting this {@link GameLoop}.
     */
    @Override
    public long process(Application app) {
        long startTime = System.nanoTime();

        input(app);

        double elapsed = startTime - previousTime;
        update(app, elapsed);
        render(app, elapsed);

        try {
            long waitTime = (int) ((frameTime - elapsed) > 0.0 ? (frameTime - elapsed) : 1000000.0) / 1000000;
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            System.err.printf("ERR : FixFPSGameLoop | Unable to sleep during frameTime=%f%n", frameTime);
            Thread.currentThread().interrupt();
        }

        previousTime = startTime;
        return System.nanoTime() - startTime;
    }

}
