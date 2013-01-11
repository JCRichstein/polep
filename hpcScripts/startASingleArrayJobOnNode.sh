#!/bin/bash

# the job number
# we get a directory name from the current job array id
dir=$JOBNAME-$PBS_ARRAYID

#delete database of possible previous run
rm -rf $RAMDISK/$D13NDB/$dir

#cd /var/tmp

# make the directory, and make sure its empty
mkdir $TEMP/$dir
rm -rf $TEMP/$dir/*
#Is important, since it determines where the log file is saved.
cd $TEMP/$dir

#FIXME should also test presence of other simulation files and if they match in md5
if [ -e $JARNAME ]; 
then
	#check if it is the same md5 	
	testMD=`(md5sum $JARNAME | sed 's/ /_/g')`
	if [ $testMD != $MD ];
	then
	#if the MD5 sum is not the same as the one we started the main script with, copy it here
	cp $PBS_O_WORKDIR/$dir/$JARNAME $TEMP/$dir
	fi
else
#if the jar is not here, copy it over
cp $PBS_O_WORKDIR/$JOBNAME/$JARNAME $TEMP/$dir
#cp $PBS_O_WORKDIR/$SCENARIO $TEMP/$dir
fi

#FIXME should not run JARFILE from the PBS_O_Workdir but from /var/tmp/$JARNAME 
#Execute the job 
#java -Drun.id=$JOBNAME-$PBS_ARRAYID -Dresults.path=$TEMP/$dir -Dscenario.file=$SCENARIO -jar $PBS_O_WORKDIR/$JARNAME
java -d64 -server -Xmx3072m -Drun.id=$JOBNAME-$PBS_ARRAYID -Dresults.path=$TEMP/$dir -Dscenario.file=$SCENARIO -jar $TEMP/$dir/$JARNAME


#REMOVE JAR File
rm -f $TEMP/$dir/$JARNAME

#Rename simulation.log
mv simulation.log $JOBNAME-$PBS_ARRAYID.log

#Make a dir for the data and copy it to that directory so we can access the data from the head node
cp -r $TEMP/$dir/* $PBS_O_WORKDIR/$JOBNAME/
#Delete folder and ramdisk.
rm -rf $TEMP/$dir
rm -rf $RAMDISK/$D13NDB/$dir
