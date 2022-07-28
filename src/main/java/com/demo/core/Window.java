package com.demo.core;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * A Window container based on the JFrame object.
 *
 * @author Frédéric Delorme
 * @version 1.0.0
 */
public class Window extends JFrame {

    private InputHandler inputHandler;

    /**
     * Initialize the Window with its new title and a default Dimension.
     *
     * @param title title of the window
     * @param dim   Dimension of the wnidow.
     */
    public Window(String title, Dimension dim) {
        setTitle(title);
        createWindow(dim);
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

        pack();
        setVisible(true);
    }

    public Window attachHandler(InputHandler ih) {
        addKeyListener(ih);
        addMouseListener(ih);
        addMouseMotionListener(ih);
        this.inputHandler = ih;
        return this;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
