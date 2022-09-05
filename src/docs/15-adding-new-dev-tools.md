# Adding new Dev Tools

## Gitpod support

The [GitPod.io](https://gitpod.io "The remote development environment on the cloud by gitpod") online service propose a
way to edit the project in a full cloud oriented environment, without any tools on your own computer. The default
project IDE is Visual Studio Code web edition, popping-up a workspace onto a remote amazon machine, tailed as your own
proper configuration requirement.

### the `.gitpod.yml` file

This basic yml file will define your environment, the default software to be installed, the network configuration,
etc...

```yml
# Start with a VNC service with a new dedicated Docker image
image:
  file: .gitpod.Dockerfile

# List the start up tasks. Learn more https://www.gitpod.io/docs/config-start-tasks/
tasks:
  - init: echo 'init script' # runs during prebuild
    command: . /home/gitpod/.sdkman/bin/sdkman-init.sh && sdk env install

# List the ports to expose. Learn more https://www.gitpod.io/docs/config-ports/
ports:
  - port: 3000
    onOpen: open-preview
```

In that file:

1. we clearly tell the gitpod service to pop up a specific docker image, defined in a second gitpod
   file: `.gitpod.Dockerfile`.
2. we start an init script to install some Java oriented stuff, with the help of
   the [sdkman](https://sdkman.io/ "go and visit the official SDKMan web site") command line tool.
3. require to open the 3000 IP port to enable opening a preview UI (through a web VNC client, se ethe docker
   configuration)

### the `.gitpod.Docker` file

This file defines exactly what will be our development environment, ti start the workspace on:

```dockerfile
FROM gitpod/workspace-full-vnc
RUN sudo apt-get update && \
    sudo apt-get install -y libgtk-3-dev && \
    sudo rm -rf /var/lib/apt/lists/*
```

Starting from an already defined gitpod image, we add some specific needs for our own project, here is a VNC client
connection, and some require libraries to allow the remote connection on a desktop to test the Java UI of that project.

### The `.sdkmanrc` file

The last but not the least file, is theSDKMan configuration file I use top define the specific java tools needed to
build/run this project:

```properties
# Enable auto-env through the sdkman_auto_env config
# Add key=value pairs of SDKs to use below
java=18.0.1-zulu
maven=3.8.6
```

Here are defined:

1. the mandatory JDK version, here is 18.0.1-zulu edition, to build this java project,
2. the maven version 3.8.6, to be installed to manage the build lifecycle.

