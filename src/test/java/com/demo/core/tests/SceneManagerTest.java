package com.demo.core.tests;

import com.demo.core.Application;
import com.demo.core.services.scene.SceneManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SceneManagerTest {
    Application app;
    SceneManager scm;

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
        assertEquals("test", scm.getScene("test").getName(), "The scene 'test' has not been set");
    }

}
