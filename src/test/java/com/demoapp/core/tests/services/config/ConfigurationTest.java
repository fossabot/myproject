package com.demoapp.core.tests.services.config;

import com.demoapp.core.services.config.Configuration;
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
    private static final String UNKNOWN_TEST_CONFIGURATION_FILE = "unknown.properties";
    private static final String NO_VALUE_TEST_CONFIGURATION_FILE = "test-no-value.properties";
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
    public void unknownPropertiesFilePassedToConfiguration() {
        Configuration config = new Configuration(UNKNOWN_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[]{"arg=unknown"});

        assertTrue(errContent.toString().contains("ERR : Configuration | Unable to read configuration file '" + UNKNOWN_TEST_CONFIGURATION_FILE + "'"), "The application known this argument !");
    }

    @Test
    public void noValuesPropertiesFilePassedToConfigurationAndDefaultValuesAreSet() {
        Configuration config = new Configuration(NO_VALUE_TEST_CONFIGURATION_FILE);
        assertEquals("Default Window Application title", config.getTitle(), "The default window title is wrong");
        assertEquals(0, config.getDebugLevel(), "The default debug level is wrong");
        assertEquals(320, config.getWindowDimension().width, "The default window width is wrong");
        assertEquals(200, config.getWindowDimension().height, "The default window height is wrong");
        assertEquals(2.0, config.getScale(), "The default scale is wrong");
        assertEquals(320.0, config.getGameArea().getWidth(), "The default game area width is wrong");
        assertEquals(200.0, config.getGameArea().getHeight(), "The default game area height is wrong");
        assertEquals("", config.getSceneList(), "The default scene list is wrong");
        assertEquals("", config.getSceneDefault(), "The default active scene is wrong");
        assertEquals(60.0, config.getFPS(), "The default FPS is wrong");
    }

    @Test
    public void unknownArgForConfiguration() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[]{"arg=unknown"});

        assertFalse(errContent.toString().contains("ERROR : Configuration | Unknown parameter test=unknown"), "The application known this argument !");
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

    @Test
    public void unknownWindowArgumentSendToConfiguration() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[]{"window=200*200"});
        assertTrue(errContent.toString().contains(
                        "ERROR : Configuration | window dimension format is [width]x[height]:"),
                "The application window arg has been not correctly detected !");
    }

    @Test
    public void unknownModeArgumentSendToConfiguration() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[]{"mode=unknown"});
        assertTrue(errContent.toString().contains(
                        "ERROR : Configuration | Unknown value "),
                "The application mode arg has been not correctly detected !");
    }
}
