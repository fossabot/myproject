package com.demoapp.core.tests.scenes;

import com.demoapp.core.Application;
import java.awt.event.KeyEvent;
import com.demoapp.core.services.io.OnKeyReleaseHandler;
import com.demoapp.core.services.scene.AbstractScene;

public class TestScene extends AbstractScene implements OnKeyReleaseHandler {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public void create(Application app) {

    }

    @Override
    public void input(Application app) {

    }

    @Override
    public void onKeyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
}
