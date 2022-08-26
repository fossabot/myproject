# Add a Camera to a Scene

AS in any game application, we need to focus attention on the character moved by the player.
To achieve that, we will use a cinema technic : the "tracking camera".

A camera intends to watch an action in a scene. This exactly what we want to do.

## The Camera

The camera is mainly an object like other in the Application, but with new attributes: a target to be tracked,
A field of view, which in our case correspond to the window size, and "spring" factor, defining how the camera is
tracking
the target object.

```plantuml
@startuml "The Camera class"
!theme plain
hide methods
hide attributes
class GameObject
class Scene
show Camera attributes
show Camera methods

class Camera extends GameObject{
  -target:GameObject
  -fov:Rectangle2D
  -tweenFactor:double
  +setTarget(t:GameObject):Camera
  +setTweenFactor(tf:double):Camera
  +setFOV(fov:Rectangle2D):Camera
  +update(double e):void
}

Scene "1" --> "1" Camera:activeCamera
Scene "1" --> "n" Camera:cameras
@enduml
```

### Updating the camera position

To track another `GameObject`, the `Camera` must "copy" its position.
But to get a better tracking effect, let's introduce a delayed effect with the tween factor.

```math
cam.pos = cam.pos
        + (target.pos-(fov.size-target.size)) 
          x 0.5
          x tweenFactor
          x elpasedTime
```

This strange formula will introduce the `tweenFactor` delay in the computation of
the distance between center of the target, and the center of the Field Of View where we
want to see the targeted `GameObject`.

Now we need to interest to the Scene integration.

## Scene with a camera

The Scene interface must propose a camera getter, returning the default active `Camera`:

```java
public interface Scene {
    //...
    Camera getCamera();
    //...
}
```

And in the Demo scene, you need to clearly implement this one:

```java
import java.util.concurrent.ConcurrentHashMap;

public class DemoScene implements Scene {

    private Camera camera;
    private Map<String, Camera> cameras = new ConcurrentHashMap<>();

    //...
    @Override
    public Camera getCamera() {
        return camera;
    }
    //...
}
```

As those components and attributes may take benefit to all Scene, it's time to create an abstract scene and
extends `DemoScene` from.

## The AbstractScene

The `Scene` interface require to get the active `Camera`. The AbstractScene will provide all the mechanic to manage
those
cameras.

```plantuml
@startuml
!theme plain
hide attributes
hide methods
class Camera extends GameObject
show Scene methods
interface Scene {
    +getCamera():Camera
}

show AbstractScene attributes
show AbstractScene methods
abstract class AbstractScene implements Scene{
 + addCamera(cam:Camera):Scene
 + getCamera(name:String):Camera
 + getCamera():Camera
 + setCamera(camName:String):Scene 
}

AbstractScene "1"-->"1" Camera:#camera
AbstractScene "1"-->"n" Camera:#cameras

class DemoScene extends AbstractScene

@enduml
```

So the `Camera`'s management is delegated to the `AbstractScene`.

```java
import com.demoapp.core.Application;
import com.demoapp.core.services.scene.AbstractScene;

class DemoScene extends AbstractScene {
    @Override
    public void create(Application app) {
        // add a camera
        Camera cam = new Camera("cam01");
        addCamera(cam);
    }
}
```

So in the `Renderer` component, the `Scene#getCamera()` is used to set the camera view at rendering time.

```java
public class Renderer {
    //...
    public void draw(Application app, Scene scene) {
        Graphics2D g = buffer.createGraphics();
        //...
        Camera currentCam = scene.getCamera();
        if (Optional.ofNullable(currentCam).isPresent()) {
            g.translate(-currentCam.x, -currentCam.y);
        }
        // draw all objects
        //...
        if (Optional.ofNullable(currentCam).isPresent()) {
            g.translate(currentCam.x, currentCam.y);
        }
        //...
    }
    //...
}
```