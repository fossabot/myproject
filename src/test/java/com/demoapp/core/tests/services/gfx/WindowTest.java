package com.demoapp.core.tests.services.gfx;

import com.demoapp.core.services.io.InputHandler;
import com.demoapp.core.services.gfx.Window;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class WindowTest {
    private Window window;

    @AfterEach
    public void teardown() {
        if (Optional.ofNullable(window).isPresent()) {
            window.dispose();
        }
    }

    /**
     * Create a window with a defined windowTitle.
     *
     * @param windowTitle the window title to be set.
     * @return the curent Window object.
     */
    private Window createWindow(String windowTitle) {
        Window win = new Window(windowTitle, new Dimension(320, 200),1.0);
        InputHandler ih = new InputHandler();
        win.addKeyListener(ih);
        return win;
    }

    /**
     * Retrieve the created InputHandler for the current window.
     *
     * @param window the window to retrieve InputHandler from.
     * @return the curent InputHandler object.
     */
    private InputHandler getInputHandler(Window window) {
        KeyListener kls = window.getListeners(KeyListener.class)[0];
        return (InputHandler) kls;
    }

    @Test
    public void aWindowCanBeCreated() {
        window = createWindow("CreatedWindow");
        assertNotNull(window, "The Window object instance has not been created!");
        assertEquals(320, window.getWidth(), 0,
                "The Window width is not set to the right value!");
        assertEquals(200, window.getHeight(), 0,
                "The Window height has not been set to the right value!");
    }

    @Test
    public void aWindowCanCaptureKeyInput() {
        window = createWindow("CaptureKeysWindow");
        try {
            Robot robot = new Robot();
            window.setFocusable(true);
            window.setVisible(true);
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.delay(300);
            assertTrue(getInputHandler(window).getKey(KeyEvent.VK_ESCAPE),
                    "The Window ESCAPE key has not been pressed");
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            robot.delay(300);
            assertFalse(getInputHandler(window).getKey(KeyEvent.VK_ESCAPE),
                    "The Window ESCAPE key has not been released");
        } catch (AWTException e) {
            fail("Unable to simulate Window listener interaction");
        }
    }


    @Test
    @Disabled("Window:Mouse test must be fixed to be executed")
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
            assertTrue(
                    getInputHandler(window).getMouseButton(mouseButton),
                    "The Window mouse button " + mouseButton + " has not been pressed");

            robot.mouseRelease(requiredButtonMask);
            robot.delay(100);
            assertFalse(
                    getInputHandler(window).getMouseButton(mouseButton),
                    "The Window mouse button " + mouseButton + " has not been released");

        } catch (AWTException e) {
            fail("Unable to simulate Window listener interaction");
        }
    }
}
