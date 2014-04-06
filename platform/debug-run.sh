#!/bin/bash

export dist_package=jaibo-platform
export version=0.4

# Running in debug mode (debug through remote debug in intellij idea)
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar $dist_package-$version.jar