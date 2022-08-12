package com.demo.core.tests;

import com.demo.core.services.config.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTest {

    /**
     * The default configuration file name for test.
     */
    private static final String DEFAULT_TEST_CONFIGURATION_FILE = "test-app.properties";
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

    @Test
    public void unknownArgForConfiguration() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[]{"arg=unknown"});

        assertTrue(!errContent.toString().contains("ERROR : Unknown parameter test=unknown"), "The application known this argument !");
    }

    @Test
    public void testModeForConfiguration() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[]{"mode=test"});
        assertEquals("test", config.getMode(), "The application is not in test mode");
    }

    @Test
    public void unknownModeForConfiguration() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[]{"mode=unknown"});
        assertEquals("run", config.getMode(), "The application mode is unknown, got to run fallback mode");
    }

    @Test
    public void windowDimensionIsSetByArg() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[]{"window=320x200"});

        assertAll(
                () -> assertEquals(320, config.getWindowDimension().width, "The window application width size is not set !"),
                () -> assertEquals(200, config.getWindowDimension().height, "The window application height size is not set !"));
    }

    @Test
    public void windowDimensionHasDefaultValue() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);

        assertAll(
                () -> assertEquals(640, config.getWindowDimension().width, "The window application width size is not set !"),
                () -> assertEquals(400, config.getWindowDimension().height, "The window application height size is not set !"));
    }
}
