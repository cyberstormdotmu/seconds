## Remove dev profile…

Currently in maven we have profiles for
- dev
- local
- prod

Remove the dev profile. I believe that we should use local. Should we rename prod to production?

## Run both clients from a single bash file

currently you have to create two terminal windows and run 

Window 1

$ cd ./shoal-platform/shoal-client/

$ npm run start-watch

Window 2

$ cd ./shoal-platform/shoal-admin-client/

$ npm run start-watch

Can we write a bit of bash trickery to put both into the same window.

## Get Jenkins pass/ fail visible in the office 

Needs a PC and screen. Mac mini? 

## Rename modules/ packages

We have modules called "myapp.????."

## Backup Jenkins configuration

