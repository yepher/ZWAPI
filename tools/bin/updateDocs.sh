#!/bin/bash

# Make sure we are in the correct directory
cd "$(dirname "$0")"

# This will put us in the projects root directory
cd ../.. 

mvn javadoc:javadoc



