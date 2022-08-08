# Add a Camera to a Scene

AS in any game application, we need to focus attention on the character moved by the player.
To achieve that, we will use a cinema technic : the "tracking camera".


A camera intends to watch an action in a scene. This exactly what we want to do.

## The Camera

The camera is mainly an object like other in the Application, but with new attributes: a target to be tracked,
A field of view, which in our case correspond to the window size, and "spring" factor, defining how the camera is tracking
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

```Math
cam.pos += cam.pos + (target.pos-(fov.size-target.size)) 
        x 0.5
        x tweenFactor
        x elpasedTime
```

This strange formula will introduce the `tweenFactor` delay in the computation of
the distance between center of the target, and the center of the Field Of View where we 
want to see the targeted `GameObject`.
