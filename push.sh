#!/bin/bash

git add jobs/seed_all.groovy
git ci -m "github pull request trigger" jobs/seed_all.groovy
git push

