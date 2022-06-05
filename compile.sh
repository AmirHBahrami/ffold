#!/bin/bash
./saveJavaFileNames.sh
javac -d bin @.classnames.txt
echo "compile done."
