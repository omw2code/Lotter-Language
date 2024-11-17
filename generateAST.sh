#!/bin/bash

echo "Generating AST tree..."

javac -d bin -sourcepath src/com/lotterLang/tools src/com/lotterLang/tools/GenerateAst.java 2> >(tee bin/log/genError.log)

if [[ $? -eq 0 ]]; then 
    echo
    echo "Compiled successfully... Java class files located in bin..."
    echo
else
    echo
    echo "Error log can also be located in bin/log/genError.log"
    echo
    echo "exiting generate with failure..."
    echo
fi

java -cp bin com.lotterLang.tools.GenerateAst 2>&1