package com.demo.core;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The Input handler service will implement all the processing upon {@link KeyEvent}, {@link MouseEvent} to manage
 * behavior in parent {@link Window}
 * this {@link InputHandler} instance must be added as a {@link KeyListener} and as a {@link MouseListener} to the parent Window.
 *
 * @author Frédéric Delorme
 * @since 1.0.0
 */
public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
    /**
     * Key mapping cache
     */
    private boolean[] keys;
    /**
     * mouse button states
     */
    private boolean[] mouseButton;
    /**
     * mouse position
     */
    private Point mousePosition;

    /**
     * Initialize the InputHandler internal input status caches.
     */
    public InputHandler() {
        keys = new boolean[65635];
        mouseButton = new boolean[MouseInfo.getNumberOfButtons()];
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing to do in that case.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Nothing to do in that case.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseButton[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseButton[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Nothing to do in that case.

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Nothing to do in that case.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Nothing to do in that case.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mousePosition = e.getPoint();
    }

    /**
     * Retrieve the mouse button x status.
     *
     * @param x the mouse button number to retrieve current state of.
     * @return
     */
    public boolean getMouseButton(int x) {
        return mouseButton[x];
    }

    /**
     * Retrieve the key keyCode status.
     *
     * @param keyCode the keyCode number to retrieve current state of.
     * @return
     */
    public boolean getKey(int keyCode) {
        return keys[keyCode];
    }

    /**
     * get the current mouse position.
     *
     * @return
     */
    public Point getMousePosition() {
        return this.mousePosition;
    }

}
