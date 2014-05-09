#!/bin/bash
cd target/javaunidoc
git init
git config user.name "TravisCI"
git config user.email "build@commercetools.com"
git add .
git commit -m "Deploy to Github Pages."
git push --force "https://${GH_TOKEN}@${GH_REF}" master:gh-pages
