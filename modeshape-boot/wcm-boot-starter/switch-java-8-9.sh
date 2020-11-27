#!/bin/bash
 
java_version=$1
 
if [ $java_version -eq 8 ]; then
	sed -i -e 's/^JAVA_HOME/#JAVA_HOME/' ~/.mavenrc
	if [ -f $PWD/.mvn/jvm.config ]; then
		mv -f $PWD/.mvn/jvm.config $PWD/.mvn/jvm9.config
	fi
elif [ $java_version -eq 9 ]; then
	sed -i -e 's/#*JAVA_HOME/JAVA_HOME/' ~/.mavenrc
	if [ -f $PWD/.mvn/jvm9.config ]; then
		mv -f $PWD/.mvn/jvm9.config $PWD/.mvn/jvm.config
	fi
else
	echo "Unknown version $java_version - doing nothing"
fi