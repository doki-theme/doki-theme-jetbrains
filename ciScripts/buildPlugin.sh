#!/bin/bash
./gradlew clean buildPlugin
git add .
git stash
git stash clear
