#!/bin/bash

RUN_AS_USER=shoal

SCRIPT_NAME=`basename ${0}`
BASE_DIR=`dirname $(dirname $(readlink -f "${0}"))`

checkUser() {
    if [ "`/usr/bin/id -u -n`" != "${RUN_AS_USER}" ]
    then
        sudo -u ${RUN_AS_USER} "${BASE_DIR}/bin/${SCRIPT_NAME}" "${1}"
        exit 0
    fi
}

stage() {
    pushd ${BASE_DIR}/staging
    wget --user=deployment --password=H0fahtte0tn --no-check-certificate https://dev2-shoal.cloudapp.net/nexus/content/repositories/releases/com/ishoal/deployment/${DEPLOY_VERSION}/deployment-${DEPLOY_VERSION}-demo.zip
    unzip -d ${BASE_DIR} deployment-${DEPLOY_VERSION}-demo.zip
    chmod +x ${BASE_DIR}/shoal-platform-${DEPLOY_VERSION}/bin/*.sh
    popd
}

stopService() {
    ${BASE_DIR}/current/bin/shoalapp.sh stop
}

flywayClean() {
    ${BASE_DIR}/current/bin/flyway.sh migrate
}

flywayMigrate() {
    ${BASE_DIR}/current/bin/flyway.sh migrate
}

startService() {
    ${BASE_DIR}/current/bin/shoalapp.sh start
}

softLink() {
    pushd ${BASE_DIR}
    rm current
    ln -s shoal-platform-${DEPLOY_VERSION} current
    popd
}

readVersion() {
    read -p "What version do you want to work with? (e.g. 0.0.8) " version
    DEPLOY_VERSION=$version
}

case "$1" in

    'stage')
        checkUser $1
        readVersion
        stage
        ;;

    'clean-deploy')
        checkUser $1
        readVersion
        stopService
        softLink
        flywayClean
        flywayMigrate
        startService
        ;;

    'deploy')
        checkUser $1
        readVersion
        stopService
        softLink
        flywayMigrate
        startService
        ;;

    *)
        echo "Usage: $0 { stage | deploy | clean-deploy }"
        exit 1
        ;;
esac