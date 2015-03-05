#!bin/sh
java -cp /home/peppone/workspace/JMetalVM/bin/ generator.FileGenerator $1
mv *.txt /home/peppone/workspace/JMetalVM/input/
mv *.dat /home/peppone/opl/MultiBinPackaging/
