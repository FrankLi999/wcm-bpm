#!/usr/bin/env bash

dir=`pwd`
echo "Build the Sling Project Archetype in folder: $dir"
mvn clean install

testFolder=$dir/target/test-classes/projects/all/project/sample-test-all

echo
echo
echo "------------------------------------------"
echo "   Build and Deploy the All Test Project"
echo "------------------------------------------"
echo
echo
cd $testFolder
mvn clean install -P autoInstallAll

testFolder=$dir/target/test-classes/projects/notAll/project/sample-test-ui

echo
echo
echo "------------------------------------------"
echo "Build and Deploy the Not All Test Project"
echo "------------------------------------------"
echo
echo
cd $testFolder
mvn clean install -P autoInstallPackage
echo
echo
echo "------------------------------------------"
echo "         Done"
echo "------------------------------------------"
echo
echo

cd $dir
echo "Current Folder `pwd`"
