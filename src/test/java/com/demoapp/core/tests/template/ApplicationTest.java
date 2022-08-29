package com.demoapp.core.tests.template;

import com.demoapp.core.Application;
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
