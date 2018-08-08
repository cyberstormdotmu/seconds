#/bin/bash

_VERSION=$1

_DIR=`dirname $0`

FIX_VERSION_STYLESHEET="$_DIR/fix-version.xsl"
GET_VERSION_STYLESHEET="$_DIR/get-version.xsl"
GET_PARENT_VERSION_STYLESHEET="$_DIR/get-parent-version.xsl"
FIX_ANT_VERSION_STYLESHEET="$_DIR/fix-ant-version.xsl"


function quit {
   echo $1
   exit 1
}

if [ -z "$_VERSION" ]
then
    echo "No version specified, will use version from ./pom.xml"
    _VERSION=`xsltproc $GET_VERSION_STYLESHEET pom.xml`
    [ $? -ne 0 ] && quit "Unable to read version from pom.xml"
fi

echo "New version: $_VERSION"

########################
# VERSIONED POM's BELOW
########################

echo "Fixing parent version on un-connected POM's"

_FILES="client-api/flash/pom.xml client-api/flash/default-crypto/pom.xml client-api/flash/crypto-api/pom.xml client-api/flash/client-api/pom.xml firebase-tools/maven/firebase-plugin/pom.xml firebase-tests/systest/blackbox/pom.xml"

for i in $_FILES
do 
     _BACKUP="$i.tmp"
     _OLD_VERSION=`xsltproc $GET_PARENT_VERSION_STYLESHEET $i`
     [ $? -ne 0 ] && quit "Unable to get parent version from $i"
     echo "Switching old version $_OLD_VERSION to $_VERSION in $i"
     xsltproc --stringparam version "$_VERSION" $FIX_VERSION_STYLESHEET $i > $_BACKUP
     [ $? -ne 0 ] && quit "Unable to process file $i"
     mv $_BACKUP $i
done

#####################
# SYSTEST BUILD FILE
#####################

echo "Fixing systest ANT test version"

_BACKUP="tmp.xml"
_FILE="firebase-tests/systest/blackbox/build.xml"
echo "Switching systest version to $_VERSION in $_FILE"
xsltproc --stringparam version "$_VERSION" $FIX_ANT_VERSION_STYLESHEET $_FILE > $_BACKUP
[ $? -ne 0 ] && quit "Unable to update $_FILE"
mv $_BACKUP $_FILE
