# GameLoop component

All the step of a game loop can be defined following multiple strategy :

- FPS first, when FPS if VERY important and quality rendering is not
- Processing first, when FPS respect is not mandatory, and rendering quality must be maintained.

So the game loop implementation must be delegated to a dedicated component. This where we propose a GameLoop interface.

```java
public interface GameLoop {
    public process(Application app);
} 
```

Our first implementation will be a very simple one, without any constrain on time and FPS:

```java
public class StandardGameLoop implements GameLoop {

    long previousTime = System.nanoTime();

    public long process(Application app) {
        long startTime = System.nanoTime();
        input(app);
        long elapsed = startTime - previousTime;
        update(app, elapsed);
        render(app, elapsed);
        previousTime = startTime;
        return System.nanoTime() - startTime;
    }

    private void input(Application app) {
        // todo Implement the required processing for managing input
    }

    private void update(Application app, long elapsed) {
        // todo Implement the required processing for updating
    }

    private void render(Application app, long elapsed) {
        // todo Implement the required processing for rendering
    }


}
```

These are the 3 main steps of a game loop cycle.

- manage input,
- update entities,
- draw things.

### Manage inputs

Listening to all input event is delegated to the `InputHandler` component, see the chapter [05](05-input_handler.md) for
details.

```java
public class StandardGameLoop implements GameLoop {
    //...
    private void input(Application app) {
        // todo Implement the required processing for managing input
    }
    //...
}
```

### Updating objects

Diving into this part, we will delegate objects update to themselves, this part will be implemented in
chapter [06](06-manage_game_object.md).

```java
public class StandardGameLoop implements GameLoop {
    //...
    private void update(Application app, long elapsed) {
        
        /* parse all objects from application and update themselves.
        
        app.getObjects.values().forEach(go -> {
            go.update(elapsed);
        });
        
        */
    }
    //...
}
```

### Rendering

The rendering will be delegated to the Renderer component (see chapter [07](07-create_renderer.md)).

```java
public class StandardGameLoop implements GameLoop {
    //...
    private void render(Application app, long elapsed) {
        app.getRender().draw(app);
        app.getRender().drawToWindow(app.getWindow());
    }
    //...
}
```
