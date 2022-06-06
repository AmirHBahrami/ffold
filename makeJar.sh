#!/bin/bash
./clean
./compile
mkdir deployed
#touch deployed/MANIFEST.MF
#echo "Main-Class: Main.class" >> deployed/MANIFEST.MF
cd bin
jar cfe ../deployed/ffold.jar Main Main.class
