#!/bin/bash

if [[ -n "$*" ]]; then
  ./gradlew -q run --args="$*"
else
  ./gradlew -q run
fi
