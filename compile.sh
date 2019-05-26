#!/bin/bash

echo "Removing Class Files..."
rm *class
echo "Compiling Muser..."
javac -cp .:assets/jars/tika-core-1.9.jar:assets/jars/tika-parsers-1.9.jar Muser.java
echo "Compile Complete"
java -cp .:assets/jars/tika-core-1.9.jar:assets/jars/tika-parsers-1.9.jar Muser

