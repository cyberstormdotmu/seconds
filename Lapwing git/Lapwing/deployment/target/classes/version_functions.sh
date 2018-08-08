#!/usr/bin/env bash

#only functions are declared in this script, it is not intended to be executed directly.
validatePomExistsInPwd() {
    if [ ! -f "${PWD}/pom.xml" ]; then
        echo "no pom found in current directory";
        exit 1;
    fi
}

getArtifactVersion() {
    validatePomExistsInPwd
    echo $(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -e '^[[:digit:]]');
}