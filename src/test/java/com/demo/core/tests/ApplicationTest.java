package com.demo.core.tests;

import org.junit.After;
import org.junit.Before;

import com.demo.core.Application;

public class ApplicationTest {
    Application app;

    @Before
    void setup() {
        app = new Application(new String[] {});
    }

    @After
    void tearDown() {
        app.dispose();
        app = null;
    }

}
