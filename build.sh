#!/bin/bash 

echo "Compiling all java files..."

JAVA_FILES=$(find . -name "*.java")

javac -d ../bin --source-path ./src/com ${JAVA_FILES} 2> >(tee bin/error.log)

if [[ $? -eq 0 ]]; then 
    echo
    echo "Java class files located in bin"
    echo
else
    echo
    echo "Error log can also be located in ../bin/error.log"
    echo
    echo "exiting build with failure..."
    echo
fi