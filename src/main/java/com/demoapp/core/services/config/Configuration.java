package com.demoapp.core.services.config;

import com.demoapp.core.math.Vec2d;
import com.demoapp.core.services.physic.PhysicEngine;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

public class Configuration {

    private final Properties props = new Properties();
    public double colSpeedMinValue;
    public double colSpeedMaxValue;
    public Vec2d defaultGravity;

    /**
     * Debug modules parameters
     */
    public int debugMargin;
    public Color debugColorBorder;
    public Color debugColorBackground;
    public Color debugColorText;
    public Integer debugXOffset;
    public Integer debugYOffset;


    /**
     * Internal title of this application to b used as display or as logging
     * purpose.
     */
    private String title = "defaultName";
    /**
     * flag to start the application in test mode.
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
     * Frame Per second display rate.
     */
    private double fps;
    /**
     * Default minimum speed for {@link PhysicEngine}. under this value, consider 0.
     */
    public double speedMinValue = 0.1;
    /**
     * Default maximum speed for {@link PhysicEngine}, fixing upper threshold.
     */
    public double speedMaxValue = 4.0;
    /**
     * Default minimum acceleration for {@link PhysicEngine}. under this value, consider 0.
     */
    public double accMinValue = 0.1;
    /**
     * Default maximum acceleration for {@link PhysicEngine}, fixing upper threshold.
     */
    public double accMaxValue = 0.35;


    public double jupImpulsion = -100;

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
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(configurationFilename);
            props.load(is);
            populateValues();
        } catch (NullPointerException | IOException ioe) {
            System.err.printf("ERR : Configuration | Unable to read configuration file '%s'%n", configurationFilename);
        }
    }

    /**
     * After loading the configuration properties file, assign current loaded values
     * to configuration attributes.
     */
    private void populateValues() {
        this.title = props.getProperty("app.window.title", "Default Window Application title");

        // Debug module configuration
        this.debugLevel = getInteger("app.debug", "0");
        this.debugColorText = getColor("app.debug.color.text", "CYAN");
        this.debugColorBackground = getColor("app.debug.color.background", "color(0.0,0.0,0.4,0.7)");
        this.debugColorBorder = getColor("app.debug.color.border", "BLUE");
        this.debugMargin = getInteger("app.debug.plate.margin", "4");
        this.debugXOffset = getInteger("app.debug.plate.offset.x", "10");
        this.debugYOffset = getInteger("app.debug.plate.offset.y", "-10");


        // Window configuration parameters
        this.windowDimension = new Dimension(
                getInteger("app.window.width", "320"),
                getInteger("app.window.height", "200"));
        this.scale = getDouble("app.window.scale", "2.0");

        // Game area default configuration parameters.
        this.gameArea = new Rectangle2D.Double(
                0.0, 0.0,
                getDouble("app.game.area.width", "320.0"),
                getDouble("app.game.area.height", "200.0"));

        // Scene configuration parameters
        this.sceneList = props.getProperty("app.scenes.list", "");
        this.sceneDefault = props.getProperty("app.scenes.default", "");

        // Physic engine configuration parameters
        this.defaultGravity = getVec2d("app.physic.default.gravity", new Vec2d(0.0, 0.0));
        this.speedMaxValue = getDouble("app.physic.speed.max", "0.5");
        this.speedMinValue = getDouble("app.physic.speed.min", "0.01");
        this.accMaxValue = getDouble("app.physic.acc.max", "0.5");
        this.accMinValue = getDouble("app.physic.acc.min", "0.01");
        this.fps = getDouble("app.graphics.fps", "60.0");

        // Collision detection parameters
        this.colSpeedMaxValue = getDouble("app.collision.speed.max", "0.0");
        this.colSpeedMinValue = getDouble("app.collision.speed.min", "0.0");

        // Input handler properties
        this.jupImpulsion = getDouble("app.io.input.jump.impulsion", "-50.0");
    }

    /**
     * Convert a String 2D vector value from property <code>key</code>  to a {@link Vec2d} instance.
     * <p>
     * The string format to be used is <code>vec2d(x,y)</code> where <code>x,y</code> are double values, e.g. :
     *
     * <pre>
     * app.physic.default.gravity=vec2d(0,-0.981)
     * </pre>
     *
     * @param key          the key of the property value to be converted to a Vec2d instance.
     * @param defaultValue the default Vec2d value if key does not exist.
     * @return a converted to Vec2d instance of the key property value from the configuration file.
     */
    private Vec2d getVec2d(String key, Vec2d defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.equals("")) {
            return defaultValue;
        }
        String[] interpretedValue = value
                .substring(
                        "vec2d(".length(),
                        value.length() - ")".length())
                .split(",");
        Vec2d convertedValue = new Vec2d(
                Double.parseDouble(interpretedValue[0]),
                Double.parseDouble(interpretedValue[1]));
        return convertedValue;
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
     * @return the Double value of the parameter.
     */
    private double getDouble(String key, String defaultValue) {
        return Double.parseDouble(
                props.getProperty(key, defaultValue));
    }

    /**
     * Retrieve a Color from a specific configuration file entry, respecting the {@link Color} named color
     * or a clear formatted String with color components.
     * <p>
     * The current textual accepted colors are :
     * "BLACK", "WHITE", "CYAN", "RED", "ORANGE", "BLUE", "GREEN",
     * "YELLOW", "GRAY", "DARK_GRAY".
     * <p>
     * The string format for custom Color is defined in {@link Configuration#convertStringToColor(String)}.
     *
     * @param key          the entry key from configuration file here to retrieve the value to be converted to a Color.
     * @param defaultValue the default value if the entry does not exist into the configuration File.
     * @return
     * @see Configuration#convertStringToColor(String)
     */
    private Color getColor(String key, String defaultValue) {
        Map<String, Color> colors = Map.of(
                "BLACK", Color.BLACK,
                "WHITE", Color.WHITE,
                "CYAN", Color.CYAN,
                "RED", Color.RED,
                "ORANGE", Color.ORANGE,
                "BLUE", Color.BLUE,
                "GREEN", Color.GREEN,
                "YELLOW", Color.YELLOW,
                "GRAY", Color.GRAY,
                "DARK_GRAY", Color.DARK_GRAY);
        Color c = colors.containsKey(props.getProperty(key, defaultValue))
                ? colors.get(props.getProperty(key, defaultValue))
                : convertStringToColor(props.getProperty(key, defaultValue));
        return c;
    }

    /**
     * Convert a String value with coma separated color components as int of float to the corresponding Color.
     * <p>
     * The string format to define a specific color is:
     * <ul>
     *     <li> <code>0   -> 255</code> Byte component value : "color(0, 0, 0, 1)"</li>
     *     <li> <code>0.0 -> 1.0</code> Float component value : "color(0.0, 0.0, 0.0, 1.0)"</li>
     * </ul>
     *
     * @param value the String value formatted as a coma separated components values,
     *              accepting int (0 to 255) or float value (0.0f to 1.0f).
     * @return the created corresponding Color.
     */
    private Color convertStringToColor(String value) {
        String[] components = value.substring(6, value.length() - 1).split(",");
        Color c = Color.BLACK;
        if (value.contains(".")) {
            c = new Color(Float.parseFloat(components[0].trim()),
                    Float.parseFloat(components[1].trim()),
                    Float.parseFloat(components[2].trim()),
                    Float.parseFloat(components[3].trim()));
        } else {
            c = new Color(Integer.parseInt(components[0].trim()),
                    Integer.parseInt(components[1].trim()),
                    Integer.parseInt(components[2].trim()),
                    Integer.parseInt(components[3].trim()));
        }
        return c;
    }

    /**
     * Parse all argument from list of args and try and match known ones.
     *
     * @param args the list of arguments from java command line.
     */
    public void parseArguments(String[] args) {
        if (args.length > 0) {
            Arrays.stream(args).forEach(s -> {
                String[] kv = s.split("=");
                if (kv.length == 2) {
                    parseArgument(kv[0], kv[1]);
                }
            });
        }
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
        this.fps = 60.0;
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
            case "title" -> this.title = value;
            case "mode" -> {
                if ("test,run".contains(value.toLowerCase())) {
                    this.mode = value;
                } else {
                    System.err.printf("ERROR : Configuration | Unknown value %s for parameter %s%n", value, key);
                }
            }
            case "window" -> {
                if (value.contains("x")) {
                    String[] wh = value.split("x");
                    this.windowDimension = new Dimension(
                            Integer.getInteger(wh[0], 320),
                            Integer.getInteger(wh[1], 200));
                } else {
                    System.err.printf(
                            "ERROR : Configuration | window dimension format is [width]x[height]: current parameter is %s=%s%n",
                            key, value);
                }
            }
            default -> System.err.printf("ERROR : Configuration | Unknown parameter %s%n", key);
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

    public double getFPS() {
        return this.fps;
    }

    public void setDebugLevel(int debugLevel) {
        this.debugLevel = debugLevel;
    }
}
