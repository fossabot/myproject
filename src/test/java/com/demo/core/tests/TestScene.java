package com.demo.core.tests;

import com.demo.core.Application;
import com.demo.core.services.scene.Scene;

public class TestScene implements Scene {
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
}
