#!/bin/bash

echo "Debugging parser..."

javac -d bin -sourcepath src/com/lotterLang/tools/AstPrinter.java 2> >(tee bin/log/printerError.log)

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
fi

java -cp bin com.lotterLang.tools.GenerateAst 2>&1