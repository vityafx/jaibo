# Java Advanced IRC BOt (JAIBO)
**JAIBO** is a port of [AIBO](https://bitbucket.org/fx_/aibo).

*JAIBO* is an irc bot which can easily extended by extensions. The main goal of this project is to make an universal software which can work on irc channels.

[bitbucket](http://bb.jaibo.org/jaibo/)

[github](https://github.com/vityafx/jaibo)

## Issues
[Project's issue tracker](http://goo.gl/fxF3yQ)
[Github project's issue tracker](http://goo.gl/O82tnR)

## License
Code is licensed to GPL v3. See 'LICENSE' file.

## System requirements
### Run
* Java SE 1.6+

### Compile
* Maven 3 (jaibo-0.1), Gradle (jaibo-0.2+)
* JUnit 3.8.1
* SQLite 3.7.2
* Oracle MySQL Connector 5.1.30

## Available extensions at this moment
1. Game Pickup Bot
2. LiveStreams
3. Advertisement
4. Greeting

## Using
Download distribution, modify settings files and execute `dist-run.sh`:

```
##!shell

./dist-run.sh
```

or (with debug mode) by running `debug-run.sh`:

```
##!shell

./debug-run.sh
```


## Contributing
Download sources or clone this git repository.

Right after that you may compile each module and run jaibo.

## Changelog
See `CHANGELOG` file.