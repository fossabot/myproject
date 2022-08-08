package com.demo.core.services.config;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class Configuration {

    private Properties props = new Properties();

    /**
     * Internal title of this application to b used as displat or as logging
     * purpose.
     */
    private String title = "defaultName";
    /**
     * flag to start the applicaiton in test mode.
     */
    private String mode;

    /**
     * Internal debug level, 0->6, will be used to show visual debug information at
     * display time.
     */
    private Integer debugLevel;
    /**
     * Window preferred dimension.
     */
    private Dimension windowDimension;
    private double scale;
    private Rectangle2D gameArea;

    /**
     * List of scene, comma separated and respecting the item format [name]:[implementation_class]
     */
    private String sceneList;
    /**
     * the default scene name to be activated.
     */
    private String sceneDefault;


    /**
     * Initialize the configuration component with values from the properties file
     * <code>configurationFilename</code>.
     *
     * @param configurationFilename the name of the preferred configuration file
     *                              where to find default values.
     */
    public Configuration(String configurationFilename) {
        initDefaultValues();
        loadProperties(configurationFilename);
    }

    /**
     * Load default properties values from the configurationFilename.
     *
     * @param configurationFilename the name of the preferred configuration file
     *                              where to find default values.
     */
    private void loadProperties(String configurationFilename) {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("./" + configurationFilename);
            props.load(is);
            populateValues();
        } catch (IOException ioe) {
            System.err.println("Unable ti read configuration file " + configurationFilename);
        }
    }

    /**
     * After loading the confguration properties file, assign current loaded values
     * to configuration attributes.
     */
    private void populateValues() {
        this.title = props.getProperty("app.window.title", "Default Window Application title");
        this.debugLevel = getInteger("app.debug", "0");
        this.windowDimension = new Dimension(
                getInteger("app.window.width", "320"),
                getInteger("app.window.height", "200"));
        this.scale = getDouble("app.window.scale", "2.0");
        this.gameArea = new Rectangle2D.Double(
                0.0, 0.0,
                getDouble("app.game.area.width", "320"),
                getDouble("app.game.area.height", "200"));
        this.sceneList = props.getProperty("app.scenes.list", "");
        this.sceneDefault = props.getProperty("app.scenes.default", "");
    }

    /**
     * Convert an integer value from its property value in the configuration file.
     *
     * @param key          key of the property to be loaded
     * @param defaultValue a default value if no value exists.
     * @return the Integer value of the parameter.
     */
    private Integer getInteger(String key, String defaultValue) {
        return Integer.parseInt(
                props.getProperty(key, defaultValue));
    }

    /**
     * Convert an double value from its property value in the configuration file.
     *
     * @param key          key of the property to be loaded
     * @param defaultValue a default value if no value exists.
     * @return the Double value of the paramater.
     */
    private double getDouble(String key, String defaultValue) {
        return Double.parseDouble(
                props.getProperty(key, defaultValue));
    }

    /**
     * Parse all argument from list of args and try and match known ones.
     *
     * @param args the list of arguments from java command line.
     */
    public void parseArguments(String[] args) {
        Arrays.stream(args).forEach(s -> {
            String[] kv = s.split("=");
            parseArgument(kv[0], kv[1]);
        });
    }

    /**
     * Initialize default values if no configuration file or argument are set.
     */
    private void initDefaultValues() {
        this.title = "NoTitle";
        this.mode = "run";
        this.debugLevel = 0;
        this.windowDimension = new Dimension(320, 200);
        this.scale = 1.0;
        this.gameArea = new Rectangle2D.Double(0.0, 0.0, 320.0, 200.0);
    }

    /**
     * Parse the key argument and assign its value to the corresponding confguration
     * attribute, if key is matching.
     *
     * @param key   key of the required argument
     * @param value the value for this argument.
     */
    public void parseArgument(String key, String value) {
        switch (key.toLowerCase()) {
            case "title" -> {
                this.title = value;
            }
            case "mode" -> {
                if ("test,run".contains(value.toLowerCase())) {
                    this.mode = value;
                } else {
                    System.out.printf("ERROR : Unknown value %s for parameter %s%n", value, key);
                }
            }
            case "window" -> {
                if (value.contains("x")) {
                    String[] wh = value.split("x");
                    this.windowDimension = new Dimension(
                            Integer.getInteger(wh[0], 320),
                            Integer.getInteger(wh[1], 200));
                } else {
                    System.out.printf(
                            "ERROR : window dimension format is [width]x[height]: current parameter is %s=%s%n",
                            key, value);
                }
            }
            default -> {
                System.out.printf("ERROR : Unknown parameter %s%n", key);
            }
        }
    }

    /**
     * Retrieve default configured mode value
     *
     * @return the current game mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * Retrieve default configured window {@link Dimension} value
     *
     * @return the dimension of the window
     */
    public Dimension getWindowDimension() {
        return windowDimension;
    }

    /**
     * Retrieve default configured title value
     *
     * @return the current title for this application
     */
    public String getTitle() {
        return title;
    }

    /**
     * return current configured debug level.
     *
     * @return the level of debug (from 0 to 5)
     */
    public int getDebugLevel() {
        return this.debugLevel;
    }

    /**
     * Return true if request i level debug is active
     *
     * @param i the target debug level to check with current application debug level
     * @return true if current debug level is lower than i
     */
    public boolean isDebugLevel(int i) {
        return this.debugLevel < i;
    }

    /**
     * Retrieve the game area Rectangle2D.
     *
     * @return the current Game area represented by a Rectangle2D
     */
    public Rectangle2D getGameArea() {
        return this.gameArea;
    }

    /**
     * Retrieve the current Scenes list
     *
     * @return a list of Scene instances
     */
    public String getSceneList() {
        return sceneList;
    }

    /**
     * Retrieve he default active scene at start
     *
     * @return the scene name to be activated at start
     */
    public String getSceneDefault() {
        return sceneDefault;
    }

    public double getScale() {
        return scale;
    }
}
