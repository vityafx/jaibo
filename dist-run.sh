#!/bin/bash

export dist_package=jaibo
export version=0.4

# Uncomment line below to run it in background mode
#java -jar $dist_package-$version.jar < /dev/null > aibo.log 2>&1 &

# Running bot without background mode
java -jar $dist_package-$version.jar