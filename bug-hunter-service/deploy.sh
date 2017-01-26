#!/bin/sh
set -e
mvn clean install && mvn fabric8:deploy