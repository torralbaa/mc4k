# mc4k

Minecraft 4K, but fixed.

![screenshot](https://raw.githubusercontent.com/Alvarito050506/mc4k/master/screenshot.png)

## Requirements
To run mc4k, you will need the Java Runtime Environment (JRE) `>= 8.0`, to build this you will need the Java Development Kit (JDK) `>= 8.0`. Both Oracle and OpenJDK versions of the programs were tested and work correctly.

## Building and usage
To build mc4k, do:
```sh
make
```
That will generate an executable JAR file under the `build/` directory, run it like:
```sh
java -jar ./build/Minecraft4K.jar
```

## Block types
 + `1`: Grass
 + `2`: Dirt
 + `3`: Diamond
 + `4`: Stone
 + `5`: Bricks
 + `6`: Dirt
 + `7`: Wood
 + `8`: Leaves
 + `9`: Blue bricks
 + `10-15`: Dirt

## Controls
 + Movement: `"WASD"`
 + Next block: Mouse wheel, left arrow
 + Previous block: Mouse wheel, right arrow
 + Save world: `'G'`
 + Load world: `'C'`
 + Quit: `Esc`
 + Screenshot: `F2`
 + Show/hide HUD: `F1`

## Changes
 + Ported the code from a Java Applet to a Java application.
 + Modified controls to not depend on the keypad.
 + Added a better world saving and loading system.
 + Added a player data saving and loading system.
 + Added a diamond counter, and removed it from the default "inventory".
 + Added world generation.
 + Packed the program in a single JAR.
 + Modified the code internally, to make it more friendly.
 + Made small changes and fixes to the way the GUI works and looks.
 + Added automatic world and player data saving on exit.
 + Added screenshot feature.
 + Added external textures support.
 + Added multiple worlds support.
 + Added basic HUD and show/hide HUD keys.
 + Added support for a `scale` command line argument.
 + Added partial support for the MCPI API.
 + Fixed some bugs.

Player and world data are saved under `$HOME/.mc4k` (UNIX-like systems) or `%APPDATA%\.mc4k` (Windows). Screenshots, are saved to the current directory. To change the game scale on startup, execute it from the command line, like:
```sh
JAVA_CMD -jar JAR SCALE
```
Where `JAVA_CMD` is the command or path to Java, `JAR` is the path to the `Minecraft4K.jar` file, and `SCALE` is a floating point value equal to or greater than `1.0`.

## API
mc4k now partially supports the [Minecraft: Pi Edition](https://mcpi.tk/) API. Currently the list of supported functions is:
 + `world.getBlock(x,y,z)`
 + `world.setBlock(x,y,z,id)`
 + `world.getHeight(x,y)`
 + `world.setting(key,value)`
 + `world.checkpoint.save()`
 + `world.checkpoint.restore()`
 + `player.getPos()`
 + `player.getTile()`
 + `player.setPos(x,y,z)`
 + `player.setTile(x,y,z)`

### Notes
 + The coordinates should be in MCPI format, but without negative numbers (i.e. valid coordinates are `0,0,0`-`63,63,63`).
 + Currenlty the only valid setting `key` is `world_immutable`, and the only valid `value`s are `0` and `!0` (`False` and `True`).
 + Block IDs should be in MCPI format (e.g. `45` for brick, not `5`), but unavailable blocks will be replaced by dirt (`3` in MCPI, `2` in mc4k).

## Credits and licensing
I don't even know from where the original code comes, but I downloaded it from a [post](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1290821-minecraft-4k-improved-by-crunchycat-download-now?comment=60) in the MCForums. It looks like it was a modified version of the original reverse-engineered Minecraft 4K code.

All my modifications were made for the only purpose of making it more enjoyable, feel free to modify and distribute them, with the following conditions:
 + Redistribute the modified sources.
 + Keep the attributions in the files.
 + Redistribute a copy of this file, with this section unmodified.
