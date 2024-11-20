#!/bin/bash

echo "Debugging parser..."

javac -d bin -sourcepath ./src/ src/com/lotterLang/tools/AstPrinter.java 2> >(tee bin/log/printerError.log)

if [[ $? -eq 0 ]]; then 
    echo
    echo "Compiled successfully... Java class files located in bin..."
    echo
else
    echo
    echo "Error log can also be located in bin/log/printerError.log"
    echo
    echo "exiting generate with failure..."
    echo
    exit 1
fi

echo "Debugging info below..."
echo
java -cp bin com.lotterLang.tools.AstPrinter 2>&1
echo