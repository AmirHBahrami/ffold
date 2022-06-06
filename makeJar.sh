#!/bin/bash
./clean.sh
./compile.sh
mkdir deployed
cd bin
#it's just one class!
jar cfe ../deployed/ffold.jar Main Main.class
