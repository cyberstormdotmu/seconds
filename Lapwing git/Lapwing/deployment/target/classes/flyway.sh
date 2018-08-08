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

callFlyway() {
    /opt/flyway/flyway -configFile=/opt/shoal/properties/flyway.conf $1
}

migrate() {
    echo "Migrating database..."
    callFlyway migrate
}

clean() {
    echo "This action will remove all the data and tables from the database!"
    read -p "Are you sure you wish to run 'clean'? [N/y] " answer
    case ${answer:0:1} in
        y|Y)
            echo "Cleaning database..."
            callFlyway clean
            ;;
        *)
            echo "Skipping clean"
            ;;
    esac
}

rebase() {
    echo "Rebasing database..."
    callFlyway clean
    callFlyway migrate
}

repair() {
    echo "Repairing database..."
    callFlyway repair
}

case "$1" in

    'migrate')
        checkUser $1
        migrate
        ;;

    'clean')
        checkUser $1
        clean
        ;;

    'rebase')
        checkUser $1
        rebase
        ;;

    'repair')
        checkUser $1
        repair
        ;;

    *)
        echo "Usage: $0 { migrate | clean | repair }"
        exit 1
        ;;
esac