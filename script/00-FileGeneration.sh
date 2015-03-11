#!bin/sh
#Usage: $1 = number of VM to generate.
java -cp /home/peppone/workspace/JMetalVM/bin/ generator.FileGenerator $1
mv *.txt /home/peppone/workspace/JMetalVM/input/
mv *.dat /home/peppone/opl/MultiBinPackaging/
