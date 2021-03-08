# mc4k

Minecraft 4K, but fixed.

![screenshot](https://raw.githubusercontent.com/Alvarito050506/mc4k/master/screenshot.png)

## Requirements
To run this, you will need the Java Runtime Environment (JRE) `>= 8.0`, to build this you will need the Java Development Kit (JDK) `>= 8.0`. Both Oracle and OpenJDK versions of the programs were tested and work correctly.

## Building and usage
To build this, do:
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
 + Next block: Left arrow
 + Previous block: Right arrow
 + Save world: `'G'`
 + Load world: `'C'`
 + Quit: `Esc`
 + Screenshot: `F2`

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

Both player and world data, and screenshots, are saved to the current directory.

## Credits and licensing
I don't even know from where the original code comes, but I downloaded it from a [post](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1290821-minecraft-4k-improved-by-crunchycat-download-now?comment=60) in the MCForums. It looks like it was a modified version of the original reverse-engineered Minecraft 4K code.

All my modifications were made for the only purpose of making it more enjoyable, feel free to modify and distribute them.
