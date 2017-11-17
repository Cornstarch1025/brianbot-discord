# Warning: This is an uploaded discontinued build of Automata and is no longer supported. The hosted one is using a private repo. Feel free to use this as a base for a bot, but please give credit where it's due and do note that stuff may be deprecated.
# Automata 

Automata is a ubiquitous Discord bot developed by Niflheim and Kirbyquerby.

## Quick Start

1. Open a terminal, cd to the directory you want to store the repository, then clone this repository: `git clone https://github.com/NiflheimDev/Automata.git`
2. If you're using command-line, run `gradle run`. Otherwise, open the project in your IDE.
3. Make sure your config.conf has all the necessary information.
4. You're all set!

### Prerequisites

* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Gradle](https://gradle.org/)
* [RethinkDB](https://www.rethinkdb.com/)

### Optional:

An IDE that supports Java + Gradle (or really good command-line-fu. We recommend [Intellij](https://www.jetbrains.com/idea/).

### Deployment:
Deploying the bot is simple - just clone the repository and cd into the root folder. Then type:

```
gradle run
```

If you're on Linux with an SSH session, you can keep the bot running after closing by installing screen and typing:

```
screen gradle run
```

Going back into the ssh session, you can reattach the screen with:

```
screen -x
```

The bot should now be running!

### Support

Join our support server for any questions or suggestions!

https://discord.gg/DC5PzXN

## Built With

* [JDA](https://github.com/DV8FromTheWorld/JDA) - The Discord wrapper used
* [RethinkDB](https://www.rethinkdb.com/) - Database
* [Gradle](https://gradle.org/) - Dependency Management

## Authors

* **David Wang** - *Automata* - [NiflheimDev](https://github.com/NiflheimDev)
* **Nathan Dias** - *Quality Check* - [Kirbyquerby](https://github.com/Kirbyquerby/)

## License

This project is Copyrighted, see the License for more information.
