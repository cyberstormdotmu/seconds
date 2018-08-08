#!/usr/bin/env bash

# load functions
. $(find . -name version_functions.sh)
. $(find . -name deploy_functions.sh)

# use when deploying locally to the same host where jenkins lives
ENVIRONMENT=localhost

# use for testing script outside of jenkins
#ENVIRONMENT="dev2-shoal.cloudapp.net";

validateEnvironment() {
    if [ "X$ENVIRONMENT" == "X" ]; then
        echo ENVIRONMENT not set;
        exit 1;
    fi
}
deployAnArtifactToCi() {
    validateEnvironment

    local version=$(getArtifactVersion);
    echo deploying artifact version:  0.0.19-SNAPSHOT;
    # pass all functions active in this script to ssh then execute one
    ssh -t shoal-deploy@${ENVIRONMENT} "$(typeset -f); deployAnArtifactVersion ${version}"
}

deployAnArtifactToCi
