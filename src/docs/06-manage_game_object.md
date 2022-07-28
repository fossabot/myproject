# Managing GameObject

Our application must have something to play with. THis is where the `GameObject` will come.

This simple object is a minimal entity to be managed by the application as a moving object
and a displayable object.

Those object must obey to some mechanical physic laws, and must support some generic attributes to
be drawn in a som fancy way.

## Physic laws

We need to stick to some mechanics behavior, and we will go simply with newton's law with some arrangement with reality.

According to some [official definition](https://www.britannica.com/science/Newtons-laws-of-motion), the first and the
second law will help us to design the way our objects will move. While the third law will drive us in the collision
behavior.

But first thing first, what do we need to identify, move and draw an object ?

## Identifying GameObject

Here we will stick with very simple ID: a long number. This long number will be an incremented value on each GameObject
creation, and the current value will be assigned to the GameObject.

We also need a common and useful way to identify any GameObject. A name in as a String will be used. Assigned with a
default value composed of a root name and the generated id.

```java
public class GameObject {
    private static long index = 0;
    private long id = index++;

    private String name = "gameobject_" + id;
}
```

Here wa have some good starting point to enhance the GameObject with new parameters.

## Moving GameObject

First is the object _position_ (`x,y`) on the game space. let's stick wit 2D world as start. AS this object can be a
visual one,
we need more information about its own size. let's add some _dimension_ (`w,h`).

Then we need _speed_ to be applied to this object on each computation step to move it.
We will also manage a rotation angle on the z axis (`rz`):

```java
public class GameObject {
    //...
    // position and size parameters
    private double x, y;
    private double w, h;
    // mechanical parameters
    private double dx, dy;
    private double rz;
    //...
}
```

## Drawing GameObject

We will start with some simple shape like point, line, rectangle and ellipse. And we can add a last one (the fancy one)
a transparent image.

### Type of object shape

So we need a type of Object that can have 4 shape types, 1 image type:

```java
public enum ObjectType {
    POINT,
    LINE,
    RECTANGLE,
    ELLIPSE,
    IMAGE;
}
```

### Required Attributes

To draw things, at least we need a _border color_, a _fill color_, and as we will need an _image_ object:

```java
public class GameObject {
    //...
    private ObjectType type;
    private BufferedImage image;
    private Color borderColor;
    private Color fillColor;
    //...
}
```

## Let Application manages objects

Now we had defined the `GameObject` entity, we can add some new attributes and method to the `Application` to maintained
and managed those objects:

We will need a list of such object, and some helper to `add` or `remove` `GameObject`.

```java
import java.util.concurrent.ConcurrentHashMap;

public class Application {
    //...
    private Map<String, GameObject> objects = new ConcurrentHashMap<>();
    //...

    public void add(GameObject go) {
        objects.put(go.getName(), go);
    }

    public void remove(GameObject go) {
        objects.remove(go.getName());
    }
    //...
}
```

We also need to initialize, like in cinema where actors are playing, the scene where all objects interact.
Let's add a `createScene()` method.

```java
public class Application {
    //...
    public void createScene() {
        add(new GameObject("player"));
    }
//...
}
```

in this sample code, we've just added a new GameObject named "player" at its default position.
We may need some new GameObject helper to set the position, type and size.

```java
public class GameObject {
    //...
    GameObject setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    GameObject setDimension(double w, double h) {
        this.w = w;
        this.h = h;
        return this;
    }

    GameObject setSpeed(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
        return this;
    }
    //...
}
```

So we can define position and size:

```java
public class Application {
    //...
    public void createScene() {
        add(new GameObject("player")
                .setPosition(160.0, 100.0)
                .setDimension(16.0, 16.0)
                .setSpeed(0.0, 0.0));
    }
//...
}
```

Ok,we've just added an object to the Application. and now what else ?

## Computation

Reading back the newton's law, we need to update the objects. this is achieved from the GameLoop implementation:

```java
public class StandardGameLoop implements GameLoop {
    //...
    private void update(Application app, long elapsed) {
        app.getObjects.values().forEach(go -> {
            go.update(elapsed);
        });
    }
    //...
}
```

And at GameObject level:

```java
public class StandardGameLoop implements GameLoop {
    //...
    public void update(long elapsed) {
        x += dx * elapsed;
        y += dy * elapsed;
    }
    //...
}
```

And to animate the created "player" `GameObject`, we need to enhance the StandardGameLoop to implements some input
interaction (see chapter [04](04-delegate_gameloop.md) for GameLoop implementation details):

```java
public class StandardGameLoop implements GameLoop {
    //...
    private void input(Application app) {

        GameObject player = app.getObject("player");
        InputHandler ih = app.getWindow().getInputHandler();
        double dx = 0;
        double dy = 0;

        double step = 0.0000001;
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

        if (ih.getKey(KeyEvent.VK_ESCAPE)) {
            app.requestExit();
        }
        player.setSpeed(dx, dy);
    }
    //...
}
```

## Rendering

As we will see in the chapter [07](07-create_renderer.md) the rendering of object is delegated to `Renderer` component.

