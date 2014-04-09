## Game PickupBot extension of JAIBO

# How to build
You need compiled `jaibo-api-1.0.jar` and `sqlite-jdbc-3.7.2.jar` in root directory of extension.
Then you may build it by running

```
#!shell

gradle build

```

# Using with jaibo-platform
1. Copy `games.pickupbot.jar` file from `target/libs` directory into jaibo-platform working directory + libs
2. Copy `Games.PickupBot.ini` file from `target/libs/settings/` directory into jaibo-platform working directory + settings