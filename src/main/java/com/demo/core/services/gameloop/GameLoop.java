package com.demo.core.services.gameloop;

import com.demo.core.Application;

public interface GameLoop {
    /**
     * describe game loop  processing
     *
     * @param app the Container {@link Application}
     */
    long process(Application app);

    /**
     * a hook for input management in the loop.
     * Useful for test purpose.
     *
     * @param app the Container {@link Application}
     */
    void input(Application app);

    /**
     * a hook for application and scene update in the loop.
     * Useful for test purpose.
     *
     * @param app     the Container {@link Application}
     * @param elapsed the elapsed time since previous call.
     */
    void update(Application app, long elapsed);

    /**
     * a hook for render processing in the loop.
     * Useful for test purpose.
     *
     * @param app     the Container {@link Application}
     * @param elapsed the elapsed time since previous call.
     */
    void render(Application app, long elapsed);
}
