# MyProject

This is a Basic java program to rediscover how to code in java from scratch, without framework and runtime libraries.

Only Junit will be used for test purpose.

## Build

A simple maven command will help :

```bash
mvn clean install
```

## Execute the program

After building, you may execute the code from the `target/`:

```bash
java -jar target/demoapp-1.0.0.jar
```

You will see appearing such a window:

![A screenshot of the demo](src/docs/images/demoapp-screenshot-01.png "A simple screenshot of the demo")

> _**NOTE**_<br/>
> _You can also use the maven command line:_
> ```shell
> mvn exec:java
> ```

## Usage

Basic commands:

|       key        | description       |
|:----------------:|:------------------|
|  <kbd>UP</kbd>   | move up player    |
| <kbd>DOWN</kbd>  | move down player  |
| <kbd>LEFT</kbd>  | move left player  |
| <kbd>RIGHT</kbd> | move right player |
| <kbd>CTRL</kbd>  | player speed x 2  |
| <kbd>SHIFT</kbd> | player speed x 6  |


McG.
