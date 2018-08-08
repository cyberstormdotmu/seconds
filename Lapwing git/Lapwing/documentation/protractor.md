## Running from the command line

Requires backend services to be running locally

$ cd ./shoal-client/

$ npm run protractor 

## Running from Intellij

Run Protractor from Intelli ( which is not a bad idea) then take the workspace.xml Run config or else configure a new runner for Node, you will need to install the Node plugin for this.

-> Run 
-> Edit Configurations 
-> + (new) 
-> NodeJS (may need to install the Intellij Node plugin)

Name: Protractor
Node Interpreter: /home/roger/.nvm/current/bin/node  c:\Program Files\nodejs\node.exe
Node parameters: node_modules\protractor\bin\protractor
Working Directory: C:\dev\src\java\shoal-platform\shoal-client\
JavaScript File: test\protractor.conf.js

## Add a new protractor test

To create a new protractor test, add it to the E2E directory.

Use a new app page AccountPage to the AppPage.js file, use this to keep all the messy selenium code out of your test.

https://angular.github.io/protractor/#/api

## Start selenium/ web-driver on windows server for protractor end to end tests

Connect to dev-shoal.cloudapp.net:57261 (via rdp or mstsc.exe)

$ cd C:\Users\shoal\shoal-platform\shoal-client 

$ git pull username@ repo

$ npm run selenium-server 

