package com.demoapp.core.tests.services.scene;

import com.demoapp.core.Application;
import com.demoapp.core.services.config.Configuration;
import com.demoapp.core.services.scene.SceneManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class SceneManagerTest {
    Application app;
    SceneManager scm;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @BeforeEach
    public void setup() {
        app = new Application(new String[]{}, "test-scene.properties");
    }

    @Test
    public void sceneManagerHasASceneList() {
        scm = new SceneManager(app);
        assertNotNull(scm.getSceneList(), "The scene list is not set");

    }

    @Test
    public void sceneManageHasTheRightTestScene() {
        scm = new SceneManager(app);
        assertNotNull(scm.getScene("test"), "The scene 'test' has not been declared");
    }

    @Test
    public void sceneManageHasTheCurrentScene() {
        scm = new SceneManager(app);
        assertEquals("test", scm.getCurrent().getName(), "The scene 'test' has not been set as default");
    }

    @Test
    public void sceneManageHasNoDefaultScene() {
        scm = new SceneManager(new Configuration("test-no-default.properties"));
        assertNull(scm.getCurrent(), "The default scene is not null while no default one has been set");
        assertTrue(errContent.toString().contains("ERR : SceneManager | No scene defined into the configuration"), "The default scene has been detected while not set");

    }

    @Test
    public void sceneManagerHaveNoSceneTest2() {
        scm = new SceneManager(app);
        assertNull(scm.getScene("test2"), "The scene test2 does not exists but was not null");
    }


}
