#!/bin/bash

echo ; echo ; echo "Setting /foo/bar"

curl -v -X PUT 127.0.0.1:8080/cluster/foo/bar

# wait for consensus
sleep 2

echo ; echo ; echo "Getting /foo/bar"

curl -X GET 127.0.0.1:8081/cluster/foo

echo ; echo ; echo "Deleting /foo/bar"

curl -v -X DELETE  127.0.0.1:8082/cluster/foo

echo ; echo ; echo "Getting /foo/bar"

curl -v -X GET 127.0.0.1:8080/cluster/foo