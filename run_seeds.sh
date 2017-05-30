#!/bin/bash -eEx

j_url="http://hpc-master.lab.mtl.com:8080"

if [ ! -f jenkins-cli.jar ]; then
    wget $j_url/jnlpJars/jenkins-cli.jar
fi

java -jar jenkins-cli.jar -s $j_url \
   -ssh -user hpcuser -i /hpc/local/etc/jenkins/jenkins_rsa build \
   "Seed All (miked)"
