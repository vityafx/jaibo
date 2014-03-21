#!/bin/bash

export dist_package=jaibo-platform
export version=0.4
export target_directory=target

ls $target_directory
if [ ! -d "$target_directory" ]; then
    echo "Target compile directory not exists. Compiling with gradle."

    gradle build
fi

echo "Target compile directory exists. Running the bot. Press CTRL+C to shut it down."

cd target/libs

# Uncomment line below to run it in background mode
#java -jar $dist_package-$version.jar < /dev/null > aibo.log 2>&1 &

# Running bot without background mode
java -jar $dist_package-$version.jar
