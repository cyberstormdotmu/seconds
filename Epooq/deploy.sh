#!/bin/bash
RED5_DIR=/home/tkorri/Tools/red5-0.8.0
ANT_DIR=/home/tkorri/Tools/apache-ant-1.9.1

$ANT_DIR/bin/ant package

mv $RED5_DIR/webapps/epooq/streams .

cd $RED5_DIR

./red5-shutdown.sh

sleep 2

cd -

rm -fr $RED5_DIR/webapps/epooq
unzip -d $RED5_DIR/webapps/epooq server/dist/epooq.war 

mv streams $RED5_DIR/webapps/epooq/

cd $RED5_DIR
./red5.sh
