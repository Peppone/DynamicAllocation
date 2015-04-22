#!bin/sh
#Usage: $1 = number of VM to generate.
java -cp /home/portaluri/workspace/DynamicAllocation/bin/ generator.FileGenerator $1
mv *.txt /home/portaluri/workspace/DynamicAllocation/input/
mv *.dat /home/portlauri/opl/MultiBinPackaging/
