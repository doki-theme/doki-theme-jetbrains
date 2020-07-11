#!/bin/bash

# Pre-Build
git add .
git stash
git fetch
git checkout master
git pull origin master
./gradlew markdownToHtml

# Build
./gradlew clean buildPlugin
git add .
git stash
git stash clear
