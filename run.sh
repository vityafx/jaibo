#!/bin/bash

export dist_package=jaibo
export version=0.1
export target_directory=target

ls $target_directory
if [ ! -d "$target_directory" ]; then
    echo "Target distribution directory not exists. Creating distribution with maven."

    mvn install
fi

echo "Target distribution directory exists. Running the bot. Press CTRL+C to shut it down."

cd target

# Uncomment line below to run it in background mode
#java -jar $dist_package-$version.jar < /dev/null > aibo.log 2>&1 &

# Running bot without background mode
java -jar $dist_package-$version.jar