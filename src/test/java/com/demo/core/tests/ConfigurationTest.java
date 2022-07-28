package com.demo.core.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.demo.core.Configuration;

public class ConfigurationTest {

    /**
     * The default configuration file name for test.
     */
    private static final String DEFAULT_TEST_CONFIGURATION_FILE = "test-app.properties";
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void unknownArgForConfiguration() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[] { "arg=unknown" });

        assertTrue("The application known this argument !",
                !errContent.toString().contains("ERROR : Unknown parameter test=unknown"));
    }

    @Test
    public void testModeForConfiguration() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[] { "mode=test" });
        assertTrue("The application is not in test mode", config.getMode().equals("test"));
    }

    @Test
    public void unknownModeForConfiguration() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[] { "mode=unknown" });
        assertTrue("The application mode is unknown, got to run fallback mode", config.getMode().equals("run"));
    }

    @Test
    public void windowDimensionIsSetByArg() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);
        config.parseArguments(new String[] { "window=320x200" });

        assertAll(
                () -> assertTrue("The window application width size is not set !",
                        config.getWindowDimension().width == 320),
                () -> assertTrue("The window application height size is not set !",
                        config.getWindowDimension().height == 200));
    }

    @Test
    public void windowDimensionHasDefaultValue() {
        Configuration config = new Configuration(DEFAULT_TEST_CONFIGURATION_FILE);

        assertAll(
                () -> assertTrue("The window application width size is not set !",
                        config.getWindowDimension().width == 640),
                () -> assertTrue("The window application height size is not set !",
                        config.getWindowDimension().height == 400));
    }
}
