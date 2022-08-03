package com.demo.core.tests;

import com.demo.core.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ApplicationTest {
    Application app;

    @BeforeEach
    void setup() {
        app = new Application(new String[]{});
    }

    @AfterEach
    void tearDown() {
        app.dispose();
        app = null;
    }

}
