package com.demoapp.core.services.io;

import com.demoapp.core.services.gfx.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    private boolean ctrlPressed;
    private boolean shiftPressed;

    private List<OnKeyReleaseHandler> keyReleasedHandlers = new CopyOnWriteArrayList<>();

    /**
     * Initialize the InputHandler internal input status caches.
     */
    public InputHandler() {
        keys = new boolean[65635];
        mouseButton = new boolean[MouseInfo.getNumberOfButtons()];
    }

    public void add(OnKeyReleaseHandler oKRH) {
        this.keyReleasedHandlers.add(oKRH);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing to do in that case.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
        ctrlPressed = e.isControlDown();
        shiftPressed = e.isShiftDown();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
        ctrlPressed = e.isControlDown();
        shiftPressed = e.isShiftDown();
        for (OnKeyReleaseHandler oKRH : keyReleasedHandlers) {
            oKRH.onKeyReleased(e);
        }
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
     * @return true if the mouse button <code>x</code> is pressed.
     */
    public boolean getMouseButton(int x) {
        return mouseButton[x];
    }

    /**
     * Retrieve the key keyCode status.
     *
     * @param keyCode the keyCode number to retrieve current state of.
     * @return true if the key <code>keyCode</code> is pressed.
     */
    public boolean getKey(int keyCode) {
        return keys[keyCode];
    }

    /**
     * get the current mouse position.
     *
     * @return the current mouse position as a Point.
     */
    public Point getMousePosition() {
        return this.mousePosition;
    }

    public boolean isCtrlPressed() {
        return ctrlPressed;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }
}
