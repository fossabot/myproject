# Display in a Window

The `Window` component goal is to display rendered ting into OS window.

## Design

```plantuml
@startuml
!theme plain
hide methods
hide attributes
show Window attributes 
show Window methods 
class Window extends JFrame{
    - inputHander:inputHandler
    - createWindow(dim:Dimension):void
    +attachHandler(ih:InputHanler):Window
    +getInputHandler():InputHandler
}
@enduml
```

Based on
the [JFrame JDK component](https://docs.oracle.com/en/java/javase/18/docs/api/java.desktop/javax/swing/JFrame.html), we
enhance it to add new capability with our internal object (see the Input Handler
chapter [05](05-input_handler.md)).

So the window creation is delegated to the createWindow(Dimension),
with basic operations to define window size according to the given Dimension object, coming from Configuration
(see chapter [02](02-configuration_component.md)).

```java
public class Window extends JFrame {
    //...
    private void createWindow(Dimension dim) {
        setPreferredSize(dim);
        setMaximumSize(dim);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    //...
}
```

And add the capability to attach some `InputHandler` to the Window, to manage key and mouse input:

```java
public class Window extends JFrame {
    //...
    public Window attachHandler(InputHandler ih) {
        addKeyListener(ih);
        addMouseListener(ih);
        addMouseMotionListener(ih);
        this.inputHandler = ih;
        return this;
    }
    //...
}
```

## Window's application

The `Window` object is used from `Application` and its creation is performed during
application creation step:

```java
public class Application {
    //...
    private void create() {
        window = new Window(
                config.getTitle(),
                config.getWindowDimension())
                .attachHandler(new InputHandler());
        //...
        createScene();
    }
    //...
}
```

> **NOTE** It's the first component created by the Application, after Configuration loaded.

