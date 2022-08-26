package com.demoapp.core.services.gameloop;

import com.demoapp.core.Application;

public class FixFPSGameLoop extends StandardGameLoop {

    private final double frameTime;

    public FixFPSGameLoop(double fps) {
        this.frameTime = 1000.0 / fps;
    }

    @Override
    public long process(Application app) {
        long startTime = System.nanoTime();

        input(app);

        double elapsed = startTime - previousTime;
        update(app, elapsed);
        render(app, elapsed);

        /*
        try {
            Thread.sleep((int) ((frameTime - elapsed / 1000000.0) > 0 ? (frameTime - elapsed / 1000000.0) : 1));
        } catch (InterruptedException e) {
            System.err.printf("ERR : Unable to sleep during frameTime=%f%n", frameTime);
        }*/

        previousTime = startTime;
        return System.nanoTime() - startTime;
    }

}
