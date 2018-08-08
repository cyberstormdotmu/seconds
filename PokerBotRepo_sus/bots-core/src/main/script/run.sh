#!/bin/bash

CP=

for j in lib/*.jar; do
  CP=$CP:$j
done

for j in bots/*.jar; do
  CP=$CP:$j
done

CP=$CP:conf/

echo $CP

java -classpath $CP com.cubeia.firebase.bot.LocalBotServer $*


