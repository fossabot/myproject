# MyProject

[![Java CI with Maven](https://github.com/mcgivrer/myproject/actions/workflows/main.yml/badge.svg)](https://github.com/mcgivrer/myproject/actions/workflows/main.yml) [![Known Vulnerabilities](https://snyk.io//test/github/mcgivrer/myproject/badge.svg?targetFile=pom.xml)](https://snyk.io//test/github/mcgivrer/myproject?targetFile=pom.xml) [![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fmcgivrer%2Fmyproject.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fmcgivrer%2Fmyproject?ref=badge_shield) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/b65b99b96d1b4c6e8d3d7e7a96022bb9)](https://www.codacy.com/gh/mcgivrer/myproject/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=mcgivrer/myproject&amp;utm_campaign=Badge_Grade)

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

![A screenshot of the demo](src/docs/images/014-add-dynamic-debug-mode.png "A simple screenshot of the demo")

> _**NOTE**_<br/> > _You can also use the maven command line:_
>
> ```shell
> mvn exec:java
> ```

## Usage

Basic commands:

| Key              | Description                 |
| :--------------- | :-------------------------- |
| <kbd>UP</kbd>    | move up player              |
| <kbd>DOWN</kbd>  | move down player            |
| <kbd>LEFT</kbd>  | move left player            |
| <kbd>RIGHT</kbd> | move right player           |
| <kbd>CTRL</kbd>  | player speed x 2            |
| <kbd>SHIFT</kbd> | player speed x 6            |
| <kbd>Z</kbd>     | regenerate platforms        |
| <kbd>Esc</kbd>   | quit demo                   |
| <kbd>D</kbd>     | switch debug mode ( 0 to 5) |

> __Note__ 
> Mouse click on an object to siwtch its own debug level from 0 to 5.

## Futur is coming

Here is bellow some possible enhancement of this project to bring more possibilities in game design ! 

- Animated [`GameObject`](./src/main/java/com/demoapp/core/entity/GameObject.java) with an `Animation` object will cover the famous sprite purpose,  

![The Animated Sprite are coming](src/docs/images/illustration-sprites.png "Sprites are based on some animation sequences")

- The current backgound color is set to `#000000`, but soon a new background image feature will be added with the tile map management integration, to give an easy access to real platform level design with smart and fun graphism, 

![Some tiles and objects to build levels](src/docs/images/illustration-tiles-scaled-up-480x192.png "Some tiles and objects to build levels with the so oldish standard 16x16 pixels")

- A new `Behavior` interface will come to drive to more flexible and resusable code onto [`GameObject`](./src/main/java/com/demoapp/core/entity/GameObject.java) and [`Scene`](./src/main/java/com/demoapp/core/servcies/scene/Scene.java).

- Sounds will eventually poped-up in a futur release, with the help of goos old Google libraries like `jlayer`, `mp3spi` and `tritonus-share` , bringing MP3 and OGG sound support to the java sound API (see the [Tritonus project](http://www.tritonus.org/) and [Java Sound API](https://www.oracle.com/java/technologies/tiger.html).

McG.
