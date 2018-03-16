#!/bin/bash

path=$(realpath IPOSDemo)

echo $path

cd $path && mvn clean install
cd $path/../

java -jar swirlds.jar & > ipos.log

#cp  $path/IPOSApp/target/IPOSApp-0.0.1-SNAPSHOT.jar $path/../