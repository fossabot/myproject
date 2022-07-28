package com.demo.core.tests;

import com.demo.core.InputHandler;
import com.demo.core.Window;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Optional;

import static org.junit.Assert.*;

public class WindowTest {
    private Window window;

    @After
    public void teardown() {
        if (Optional.ofNullable(window).isPresent()) {
            window.dispose();
        }
    }

    /**
     * Create a window with a defined windowTitle.
     *
     * @param windowTitle the window title to be set.
     * @return
     */
    private Window createWindow(String windowTitle) {
        Window win = new Window(windowTitle, new Dimension(320, 200));
        InputHandler ih = new InputHandler();
        win.addKeyListener(ih);
        return win;
    }

    /**
     * Retrieve the created InputHandler for the current window.
     *
     * @param window the window to retrieve InputHandler from.
     * @return
     */
    private InputHandler getInputHandler(Window window) {
        KeyListener kls = window.getListeners(KeyListener.class)[0];
        InputHandler ih = (InputHandler) kls;
        return ih;
    }

    @Test
    public void aWindowCanBeCreated() {
        window = createWindow("CreatedWindow");
        assertNotNull("The Window object instance has not been created!", window);
        assertEquals("The Window width is not set to the right value!", 320, window.getWidth(), 0);
        assertEquals("The Window height has not been set to the right value!", 200, window.getHeight(), 0);
    }

    @Test
    public void aWindowCanCaptureKeyInput() {
        window = createWindow("CaptureKeysWindow");
        try {
            Robot robot = new Robot();
            window.setFocusable(true);
            window.setVisible(true);
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.delay(100);
            assertTrue("The Window ESCAPE key has not been pressed", getInputHandler(window).getKey(KeyEvent.VK_ESCAPE));
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            robot.delay(100);
            assertFalse("The Window ESCAPE key has not been released", getInputHandler(window).getKey(KeyEvent.VK_ESCAPE));
        } catch (AWTException e) {
            fail("Unable to simulate Window listener interaction");
        }
    }


    @Test
    @Ignore("Mouse button input test need to be fixed")
    public void aWindowCanCaptureMouseInput() {
        window = createWindow("MouseButtonWindow");
        try {
            int mouseButton = MouseEvent.BUTTON1;
            int requiredButtonMask = InputEvent.getMaskForButton(mouseButton);

            Robot robot = new Robot();
            window.setFocusable(true);
            window.setVisible(true);

            robot.mouseMove(window.getX() + 5, window.getY() + 5);
            robot.mousePress(requiredButtonMask);
            robot.delay(100);
            assertTrue("The Window mouse button " + mouseButton + " has not been pressed", getInputHandler(window).getMouseButton(mouseButton));

            robot.mouseRelease(requiredButtonMask);
            robot.delay(100);
            assertFalse("The Window mouse button " + mouseButton + " has not been released", getInputHandler(window).getMouseButton(mouseButton));

        } catch (AWTException e) {
            fail("Unable to simulate Window listener interaction");
        }
    }
}
