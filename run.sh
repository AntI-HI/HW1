#!/bin/bash

# Compile the project
echo "Compiling..."
mkdir -p bin
javac -d bin -sourcepath src src/com/runner/main/Game.java

# Run the game
if [ $? -eq 0 ]; then
    echo "Starting Game..."
    java -cp bin com.runner.main.Game
else
    echo "Compilation failed!"
fi
