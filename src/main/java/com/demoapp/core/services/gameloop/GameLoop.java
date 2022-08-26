package com.demoapp.core.services.gameloop;

import com.demoapp.core.Application;

/**
 * The {@link GameLoop} interface defining the entrypoint to execute loop from
 * {@link Application}, and define the  main steps of the game loop.
 * <p>
 * the {@link GameLoop#process(Application)} is called from a looping Application method.
 * <ul>
 * <li>The exit condition will be maintained by the hosting Application itself,</li>
 * <li>the looping condition is also maintained by the hosting Application.</li>
 * </ul>
 *
 * @author Frédéric Delorme
 * @version 1.0.0
 * @since 2022
 */
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
    void update(Application app, double elapsed);

    /**
     * a hook for render processing in the loop.
     * Useful for test purpose.
     *
     * @param app     the Container {@link Application}
     * @param elapsed the elapsed time since previous call.
     */
    void render(Application app, double elapsed);
}
