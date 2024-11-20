#!/bin/bash 

usage()
{
    echo " Usage: $0"
    echo " -d     : compile and execute lotter in debug"
    echo " -l     : compile and execute lotter language in REPL mode"
    echo " -g     : compile and execute AST builder"
    echo " -h     : help"
}

# Parse options using getopts
while getopts "hdgl" opt; do
    case $opt in
        d)
            ./debug.sh 
            exit 0
            ;;
        g)
            ./generateAST.sh 
            exit 0
            ;;
        l)
            #TODO: deal with user passed in files
            LOTTER_FILES="com.lotterLang.Lotter"
            ;;
        h)
            usage
            exit 0
            ;;
        *)
            usage
            exit 1
            ;;
    esac
done

if [ -z "${LOTTER_FILES}" ]; then
    usage
    exit 0
fi

echo "Compiling all java files..."

JAVA_FILES=$(find . -name "*.java")

javac -d bin --source-path ./src/ ${JAVA_FILES} 2> >(tee bin/log/eoerror.log)

if [[ $? -eq 0 ]]; then 
    echo
    echo "Java class files located in bin"
    echo
else
    echo
    echo "Error log can also be located in bin/error.log"
    echo
    echo "exiting build with failure..."
    echo
fi

#TODO: deal with user specific files
echo "Entering REPL mode..."

java -cp bin "${LOTTER_FILES}" 2>&1
