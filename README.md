# Tetris Game

This program uses java swing to create a simple, single player tetris game

## Compile and Run

To compile to java project, make sure you are in the top level directory. Then run a javac:

```bash
javac -d bin --module-source-path . --module com.rishabhn.tetris
```

This javac will compile the single module into the bin folder where it can be run with the java command:

```bash
java --module-path bin --module com.rishabhn.tetris/com.rishabhn.tetris.main.Main
```

Alternatively, after compiling into the bin folder, you can jar the application as well:

```bash
jar -cfe mods/com-rishabhn-tetris.jar com.rishabhn.tetris.main.Main -C bin/com.rishabhn.tetris .
```

Once compiled into a jar, you can run the jar using the java -jar tool:

```bash
java -jar mods/com-rishabhn-tetris.jar
```


## Inspiration

This tetris program was written in one day in order to test myself in coding a completely new game. I also am a huge fan of the tetris game and thought it would be fun.

## Usage

Anyone can use this program or parts of it if they need it. You can fork the entire repo if needed. However, I ask that you do not republish this code without giving me some credit (not licensed though - so totally optional).
