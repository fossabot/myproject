# Scene Management

As we've seen just in some previous chapter, the scene is created in the
`Application#createScene()` method, and if you need to describe multiple
scene because of multiple gameplay, you just can not do that.

So a new concept must be introduced and take place in our small project : some Scene and their manager.

## A Scene, Action !

Like in a movie, the Application can have multiple point of view, multiple gameplay.
To define those different scene and behavior, we are going to introduce the Scene concept.

```java
public interface Scene {
    String getName();

    void create(Application app);

    void input(Application app);
}
```

In this first draft, we are able to identify our scene with a specific name, and we can create it.

So, a first implementation for our "player" may be as follows:

```java
public class DemoScene implements Scene {

    public String getName() {
        return "demo";
    }

    public void create(Application app) {
        app.add(new GameObject("player")
                .setType(RECTANGLE)
                .setPosition(160.0, 100.0)
                .setDimension(16.0, 16.0)
                .setSpeed(0.0, 0.0)
                .setLayer(1)
                .setPriority(1)
        );
    }
}
```

In that scene we will create a new `GameObject` named "player", like in the previous Application implementation.

And extract the input processing from the `StandardGameLoop` object to inject it in to the `DemoScene#input` method:

```java
public class DemoScene implements Scene {
    public void input(Application app) {
        GameObject player = app.getObject("player");
        InputHandler ih = app.getWindow().getInputHandler();
        double dx = 0;
        double dy = 0;

        double step = 0.0000001;

        // manage player directional keys input
        if (ih.getKey(KeyEvent.VK_UP)) {
            dy = -step;
        }
        if (ih.getKey(KeyEvent.VK_DOWN)) {
            dy = step;
        }
        if (ih.getKey(KeyEvent.VK_LEFT)) {
            dx = -step;
        }
        if (ih.getKey(KeyEvent.VK_RIGHT)) {
            dx = step;
        }
        // request to exit from Application
        if (ih.getKey(KeyEvent.VK_ESCAPE)) {
            app.requestExit();
        }
        player.setSpeed(dx, dy);
    }
}
```

And, to let the Application manage the scene, we will delegate the scene management to a new component:
the `SceneManager`.

## Scene manager

In such component, we need to manage multiple scenes, and at least, the current activated one.

The Scene manager will maintain a list of scene with a current one.

```java
public class SceneManager {
    private Scene current;
    private Map<String, Scene> scenes = new ConcurrentHashMap<>();

    public SceneManager(Application app) {
    }

    public void add(Class<? extends Scene> sceneClass) {
        Scene scn = null;
        try {
            scn = sceneClass.getConstructor().newInstance();
            scenes.put(scn.getName(), scn);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            System.out.printf("ERR: unable to ass scene %s%n", sceneClass.getName());
        }
    }

    public void activate(String sceneName) {
        current = scenes.get(sceneName);
    }

    public Scene getCurrent() {
        return current;
    }
} 
```

And the scene list can be set programmatically, but the tbetter would be to get it by configuration. let's add this
capability to the SceneManager:

```java
public class SceneManager {
    //...
    public SceneManager(Application app) {
        initialize(app.getConfiguration());
    }

    //...
    private void initialize(Configruation config) {
        if (!config.getSceneList().equals("") && !config.getSceneDefault().equals("")) {
            String[] scenesList = config.getSceneList().split(",");
            Arrays.asList(scenesList).forEach(s -> {
                String[] kv = s.split(":");
                try {
                    add((Class<? extends Scene>) Class.forName(kv[1]));
                } catch (ClassNotFoundException e) {
                    System.out.printf("ERR: unable to load class %s%n", kv[1]);
                }
            });
            activateScene(config.getSceneDefault());
        } else {
            System.out.printf("ERR: No scene defined into the configuration");
        }
    }
    //...
}
```

And now the scenes list if provisioned by the 2 configuration keys :

```properties
# Scenes management
app.scenes.list=demo:com.demo.core.scenes.DemoScene,
app.scenes.default=demo
```

