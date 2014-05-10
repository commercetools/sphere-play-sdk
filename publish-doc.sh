#!/bin/bash
cd target/javaunidoc
git init
git config user.name "TravisCI"
git config user.email "build@commercetools.com"
git add .
git commit -m "Deploy to Github Pages."
git push --force --quiet "https://${GH_TOKEN}@github.com/commercetools/sphere-play-sdk" master:gh-pages
