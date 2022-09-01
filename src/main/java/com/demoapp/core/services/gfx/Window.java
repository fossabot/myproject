package com.demoapp.core.services.gfx;

import com.demoapp.core.services.io.InputHandler;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;

/**
 * A Window container based on the JFrame object.
 *
 * @author Frédéric Delorme
 * @version 1.0.0
 */
public class Window extends JFrame {

    private InputHandler inputHandler;
    private double scale;

    /**
     * Initialize the Window with its new title and a default Dimension.
     *
     * @param title title of the window
     * @param dim   Dimension of the window.
     */
    public Window(String title, Dimension dim, double scale) {
        setTitle(title);
        Dimension winDim = new Dimension(
                (int) (dim.width * scale),
                (int) (dim.height * scale));
        createWindow(winDim);
        this.scale = scale;
    }

    /**
     * Create the JFrame corresponding object with the right title and dimension
     *
     * @param dim the required window dimension.
     */
    private void createWindow(Dimension dim) {
        setPreferredSize(dim);
        setMaximumSize(dim);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusTraversalKeysEnabled(true);
        pack();
        setVisible(true);
    }

    /**
     * Declare a new InputHandler on this window to support Mouse and keyboard
     * operations.
     *
     * @param ih the new InputHandler implementation to be linked to this Window.
     * @return the current Window object updated (Fluent API).
     */
    public Window attachHandler(InputHandler ih) {
        addKeyListener(ih);
        addMouseListener(ih);
        addMouseMotionListener(ih);
        this.inputHandler = ih;
        return this;
    }

    /**
     * Return the currently attached InputHandler to this window.
     *
     * @return THe Current InputHandler.
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Compute mouse position into the {@link com.demoapp.core.Application} screen
     * according to scale factor.
     * 
     * @param point the mouse potision to be converted
     * @return the scale converted mouse position to the
     *         {@link com.demoapp.core.Application} screen
     */
    public Point mouseScreenPosition(Point point) {
        return new Point((int) (point.x / scale), (int) (point.y / scale));
    }
}
