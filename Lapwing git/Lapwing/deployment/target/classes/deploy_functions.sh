#!/bin/bash
#only functions are declared in this script, it is not intended to be executed directly.

checkVersionParameterExists() {
    if [ "$VERSION" == "" ]; then
      echo "Version parameter not given.";
      exit 1;
    fi
}

checkDeploymentTargetExists() {
    cd /opt/shoal/;
    if [ "$?" -ne "0" ]; then
      echo ERROR: Target deployment directory not found;
      exit 1;
    fi
}

waitForStartup() {
    echo Waiting for server to start..
    # blocking call
    ( timeout ${WAITTIME} tail -F -n0 ${LOGPATH} 2>&1 & ) | grep -q "Started ShoalApp" && echo Started. && return 0;
    return 1;
}

makeTempDirectory() {
    if [ ! -d "/opt/shoal/tmp" ]; then
        $(mkdir "/opt/shoal/tmp");
    else
        $(rm -r "/opt/shoal/tmp");
    fi
}

unPackArtifactToTemp() {
    echo Unzipping deployment artifact...
    unzip -d /opt/shoal/tmp "$ZIPNAME";
    if [ "$?" -ne "0" ]; then
        echo ERROR: Unzipping artifact failed.;
        exit 1;
    fi
}

checkForExistingDeploymentWithSameVersion() {
    if [ -d "/opt/shoal/$NAME" ]; then
        echo Found a deployment with the same version - will append a timestamp to this version.;
        DEPLOYED_NAME="$NAME-$TIMESTAMP";
    fi
}

copyArtifactToDestination() {
    echo Moving artifact to deployment path...
    mv "/opt/shoal/tmp/$NAME" "/opt/shoal/$DEPLOYED_NAME";
    if [ "$?" -ne "0" ]; then
        echo ERROR: Renaming artifact with timestamp failed.;
        exit 1;
    fi
    rm -r /opt/shoal/tmp/*;
}

stopExistingService() {
    echo Stopping existing service...
    /opt/shoal/current/bin/shoalapp.sh stop
    if [ "$?" -ne "0" ]; then
        echo ERROR: Failed to stop existing service - will continue anyway.;
    fi
}

rebuildSymlink() {
    echo Rebuild current server symlink...;
    rm current
    ln -s "$DEPLOYED_NAME" current
    if [ "$?" -ne "0" ]; then
        echo ERROR: Failed to rebuild current server symlink.;
        exit 1;
    fi
}

setPermissions() {
    echo Make scripts executable...;
    chmod +x /opt/shoal/current/bin/*.sh
    if [ "$?" -ne "0" ]; then
        echo ERROR: Failed to make scripts executable;
        exit 1;
    fi

    echo Setting deployment user permissions...;
    sudo chown -R shoal:shoal "/opt/shoal/$DEPLOYED_NAME"
    if [ "$?" -ne "0" ]; then
        echo ERROR: Failed to change deployment user permissions.;
        exit 1;
    fi
}

migrateDatabaseSchema() {
    echo Migrating database schema...;
    sudo -u shoal /opt/shoal/current/bin/flyway.sh migrate
    if [ "$?" -ne "0" ]; then
        echo ERROR: Failed to migrate database schema.;
        exit 1;
    fi
}

startNewService() {
    echo Starting service...;
    sudo -u shoal nohup /opt/shoal/current/bin/shoalapp.sh start </dev/null >/dev/null 2>&1 &
    if [ "$?" -ne "0" ]; then
        echo ERROR: Failed to start service.;
        exit 1;
    fi
}

deleteDeploymentZip() {
    echo Deleting deployment zip...;
    rm "/opt/shoal/$ZIPNAME";
    if [ "$?" -ne "0" ]; then
        echo ERROR: Failed to delete the deployment zip $ZIPNAME - this should be deleted manually.;
    fi
}

cleanupOldDeployments() {
    echo cleanupOldDeployments is not implemented yet !
}

deployAnArtifactVersion() {
    # these are globals - watch out
    VERSION=$1;shift;
    NAME="shoal-platform-$VERSION";
    ZIPNAME="deployment-$VERSION-ci.zip"
    DEPLOYED_NAME="$NAME";
    TIMESTAMP=$(date +%s);
    LOGPATH="/var/log/shoal/shoalapp.out"
    WAITTIME="5m"

    checkVersionParameterExists

    checkDeploymentTargetExists

    makeTempDirectory

    unPackArtifactToTemp

    checkForExistingDeploymentWithSameVersion

    copyArtifactToDestination

    stopExistingService

    rebuildSymlink

    setPermissions

    migrateDatabaseSchema

    startNewService

    waitForStartup

    deleteDeploymentZip

    cleanupOldDeployments
}