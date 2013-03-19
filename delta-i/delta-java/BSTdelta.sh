#!/bin/bash

nodes=25
array=100000

while [ $nodes -le 26 ]
do
    while [ $array -le 1000000 ]
    do
	java bst.DriverBSTDelta $nodes $array
	((array+=10000))
    done
    ((nodes+=1000))
    ((array=400000))
done
exit 0
