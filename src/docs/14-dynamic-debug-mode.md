# Dynamic Debug Mode

## Intends

To easily analyze issue, or simply explore things, a debug mode mouse based will bring data analysis and exploration to
debug some discrepancies into the Application. This mode must be activated with keyboard shortcut, or through a
dedicated key/value into the configuration file.

## Design

A Mouse handler must be added to be able to detect mous cursor and its position relative to `GameObject`. When the debug
mode is activated, a simple **left click** on one of the `GameObject` will open a dynamic data plate, stick to
the `GameObject` to show internal data with different level of details, switch those level with a mouse **right click**.

This Info display plate must be position with some intelligence regarding the position of the `GameObject` on the
window.

a sample display:

```text
pos: 230, 235
spd: 0.0, 0.0
acc: 0.0, 0.0
mat: default
--
   e=0.23, 
   d=1.0, 
   f=0.98 
--
```

Another Debug information panel is displayed at window bottom, with engine information :

- debug mode level/status
- FPS,
- number of objects
- number of collision
- internal time

As the following diagram present it :

```text
+-------------------------------------------+
|                                           |
|                                           |
|                                           |
|                                           |
| [ d:1 | fps: 59 | o:12 | c:0 | 01:02:45 ] |
+-------------------------------------------+
```

## Window and InputHandler to support Mouse

The first things to do is to integrate the mouse handling to our existing `InputHandler`.

So the `InputHandler`, as recommended by the JDK, we need to implement new interfaces to connect with mouse events:

- `MouseEventListener`, to get feed back on buttons,
- `MouseMoveEventListener`, to get feedback on mouse moves,
- `MouseWheelEventListener`, to gather wheel rotation events.

And we will need to update the `Window` to connect those new listener implementations.

### InputHandler modification

_TODO_

### Window listeners plugin

_TODO_

## Debug information and structure

_TODO_

### New debug data in Application

_TODO_

### New debug data in GameObject

_TODO_

## Drawing debug information

### Modification of the Renderer

_TODO_
