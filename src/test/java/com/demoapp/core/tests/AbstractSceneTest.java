package com.demoapp.core.tests;

import com.demoapp.core.entity.Camera;
import com.demoapp.core.tests.scenes.TestScene;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AbstractSceneTest {
    @Test
    public void abstractSceneAddAFirstCamera() {
        TestScene scn = new TestScene();
        scn.addCamera(new Camera("testCam"));
        assertNotNull(scn.getCamera("testCam"), "The camera has not been added to the Scene");
    }

    @Test
    public void abstractSceneAddADefaultCamera() {
        TestScene scn = new TestScene();
        scn.addCamera(new Camera("testCam"));
        assertNotNull(scn.getCamera(), "The default camera has not been initialized by adding a first cam");
    }

}
