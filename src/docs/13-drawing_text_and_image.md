# Adding a new GameObject Type

Event if we already have a bunch of `ObjectType`, one was not already implemented: TEXT.
If I want to draw some score, number of life, or just some text on my screen, I need a way to do it.

So let's add a new TYPE to the `ObjectType`, and add it to the different services to manage it.

## TEXT

First let's add this new TEXT type to the corresponding enumeration:

```java
public class GameObject implements OnCollisionEvent {
    public enum ObjectType {
        //...           
        // draw a Text from textValue
        TEXT
    }
    //...
}
```

## Drawing text

### Add something to the `GameObject`

To draw some text, we need somewhere a string containing this text. but adding a simple text value would be a too simple
approach.
Sometimes the text to be drawn, like score or some numbers, must be formatted. So let's propose a new way
to proceed.

I am going to add a new capability to the `GameObject`: an attributes map.

These `attributes` map will allow us to add dynamically some new data to any `GameObject` and those data would be
accessible from anywhere

And I am going to use it as extension possibilities for text rendering.

```plantuml
@startuml
!theme plain
class GameObject {
    - id:int
    - name:String
    - pos:Vec2d
    - speed:Vec2d
    - acc:Vec2d
    - physicType:PhysicType
    - mass:double
    - forces:List<Vec2d>
    - stickToCamera:boolean
    ---
    - box:BoundingBox
    - duration:int
    - alive:boolean
    ---
    - textValue:String
    - attributes:Map<String,Object>
    ---
    + isAlive():boolean
    + isPersistent():boolean
}
@enduml
```

### Attributes for Text

Our text valur will be fed some custom capabilities like :

- `textFormat` proposing a way to define a _printf_ format approach to format text,
- `textValue` the value that must be interpreted through te `textFormat` to feed the `GameObject.textValue`,
- `textFontSize` to define the font rendering size for this text.

Those attributes will be used only if there exist in the attributes map. if not, the `Gameobject.textValue`
is simply directly drawn.

### GameObject enhancement

The GameObject class is now adapted with new text processing capability :

```java
public class GameObject {
    //...
    public GameObject addAttribute(String key, Object value) {
        attributes.put(key, value);
        return this;
    }

    //...
    public void update(double elapsed) {
        // update the bounding box.
        box.setRect(pos.x, pos.y, w - 1, h);
        updateText();

        // update the collision box.
        //...
    }

    private void updateText() {
        if (attributes.containsKey("textFormat") && attributes.containsKey("textValue")) {
            this.textValue = String.format(
                    (String) attributes.get("textFormat"),
                    attributes.get("textValue"));
        }
    }
}
```

If a "textFormat" string attribute exists, we search for a "textValue" attribute to be formatted, and assign
the `GameObject.textValue`
with this new one.

### Rendering that

The Renderer service must be updated with new drawing capability upon the `TEXT` for the `GameObject` type:

```java
public class Renderer {
    //...
    private void drawGameObject(Scene scene, Graphics2D g, GameObject o) {

        //...
        switch (o.type) {
            //...
            case TEXT -> {
                g.setColor(o.borderColor);
                if (Optional.ofNullable(o.font).isPresent()) {
                    g.setFont(o.font);
                }
                if (o.attributes.containsKey("textFontSize")) {
                    g.setFont(g.getFont().deriveFont((float) o.attributes.get("textFontSize")));
                }
                g.drawString(o.textValue, (int) o.pos.x, (int) o.pos.y);
            }
        }
        //...
    }
    //...
}
```

And we introduce a new attribute `textFontSize` about... the font size, to set the size of the font (???:))

And to test that, we need to chang ethe DemoScene.

### DemoScene support text !

```java
public class DemoScene extends AbstractScene {
    //...
    public void create(Applciation app) {
        //...
        // add an object stick to camera
        GameObject score = new GameObject("score");
        score.setType(GameObject.ObjectType.TEXT);
        score.setPhysicType(PhysicType.NONE);
        score.setPosition(8.0, 36.0);
        score.setDimension(64.0, 16.0);
        score.setBorderColor(Color.WHITE);
        score.setFillColor(Color.CYAN);
        score.setMass(0.0);
        score.setLayer(0);
        score.setPriority(1);
        score.setStickToCamera(true);
        score.addAttribute("textFormat", "%06d");
        score.addAttribute("textValue", scoreValue);
        score.addAttribute("textFontSize", 20.0f);
        app.add(score);

        //...
    }
}
```

## IMAGE

Now let's support Text and Image, we can add new graphics to the screen with the IMAGE type.
But first to load images, we need to manage those resources.

It is now time for a `ResourceManager`, with a first kind of resources: `BufferedImage`.

### Create the ResourceManager

The `ResourceManager` is a simple internal cache where resources like images, font, sound are loaded,
available and reusable by any components from the `Application`.

I will first implement the support for
the [`BufferedImage`](https://docs.oracle.com/en/java/javase/18/docs/api/java.desktop/java/awt/image/BufferedImage.html "open the JDK18 official documentation")
and some utils to manipulated images, to crop or resize.

```plantuml
!theme plain
hide Application methods
hide Application attributes
class Application
class ResourceManager {
 - cache:Map<String,Object>
 + addResource(name:String):void
 + getImage(name:String):BufferedImage
}
Application "1" --> "1" ResourceManager:resMgr
```

I create first a Java class with an internal cache, and simple trick on the naming entry in the cache will consist in
adding the resource type as a prefix. So for an image with path `/images/myimage.png`, the entry
key would be `java.awt.image.BufferedImage:/images/myimage.png`.

Some magic will be executed to detect the nature of the resource to be loaded, mainly based on the file extension.
And then, to retrieve the image, just add as prefix this class name.

#### Loading resource

As define previously, the type of object corresponding to the loaded resource to store in cache,
is detected on the resource file extension.

For image, we will support only JPEG and PNG.

```java
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManager {
    private static Map<String, Object> cache = new ConcurrentHashMap<>();

    public static void addResource(String path) {
        String[] fileext = path.split(".");
        switch (fileext[1].toUpperCase(Locale.ROOT)) {
            case "PNG":
            case "JPG":
            case "JPEG":
                BufferedImage img = ImageIO.read(
                        ResourceManager.class.getResourceAsStream(path));
                cache.put(
                        img.getClass().getCanonicalName() + ":" + path,
                        img);
                break;
            default:
                System.err.printf("ResourceManager | ERR | Unknown resource type %s%n", path);
                break;
        }
    }
}
```

And then, I implement the method to retrieve such image resource:

```java
public class ResourceManager {
    public static BufferedImage getImage(Stirng path) {
        return (BufferedImage) cache.get(BufferedImage.class.getCanonicalName() + ":" + path);
    }
}
```
