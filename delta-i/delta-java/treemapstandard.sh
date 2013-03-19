#!/bin/bash

nodes=2000
array=100000

while [ $nodes -le 2001 ]
do
    while [ $array -le 1000000 ]
    do
	java treemap.DriverTreemapStandard $nodes $array
	((array+=10000))
    done
    ((nodes+=1000))
    ((array=1))
done
exit 0
