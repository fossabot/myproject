package com.demo.core;

public interface GameLoop {
    /**
     * describe game loop  processing
     *
     * @param app
     */
    long process(Application app);

    /**
     * a hook for input management in the loop.
     * Useful for test purpose.
     *
     * @param app
     */
    void input(Application app);

    /**
     * a hook for application and scene update in the loop.
     * Useful for test purpose.
     *
     * @param app
     * @param elapsed
     */
    void update(Application app, long elapsed);

    /**
     * a hook for render processing in the loop.
     * Useful for test purpose.
     *
     * @param app
     * @param elapsed
     */
    void render(Application app, long elapsed);
}
