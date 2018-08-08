# readme

## Links

[Stash Instance](https://dev2-shoal.cloudapp.net/stash/)

[Shoal-Platform-Repo](https://dev2-shoal.cloudapp.net/stash/projects/SHOAL/repos/shoal-platform/browse)

[Jenkins](https://dev2-shoal.cloudapp.net/jenkins/)

[Sonar Qube](https://dev2-shoal.cloudapp.net/sonar/)

## Documents

[todo list](./documentation/todo.md)

## Pre-Requisites

### postgresql

Version: 9.4

[http://www.postgresql.org/download/](http://www.postgresql.org/download/)

### git
https://dev2-shoal.cloudapp.net/stash/projects

### npm 

Distributed as part of nodejs installation. Version 4.0.0

[https://nodejs.org/en/](https://nodejs.org/en/)

### python

Version: 2.7.10

[https://www.python.org/downloads/](https://www.python.org/downloads/)

### redis

Version 3.0.5.

[http://redis.io/download](http://redis.io/download)

You will need to build it - take a look at the README file.

Note that Redis is not officially supported on Windows but there is a port available (only of Redis 2.8 unfortunately).

[https://github.com/MSOpenTech/redis](https://github.com/MSOpenTech/redis)

On Mac, Redis is available via Homebrew but all it does is download the source and run the equivalent of 'make install'
on it. This errored for me because it didn't have permission to write to /usr/local/bin, so I just used the normal
download instead.

## Getting Started The First Time.

### setup self signed certificate

????

$ git config --global http.sslverify false  # if not using the self signed jobber

## setup the database users. 

Nobody has worked out how to use pgSqk 3. so command line. Note. in windoze use cmd, not git-bash
Two databases are needed - one for running the app locally ('shoal') and a second one for running the integration tests against ('shoal_it').

$ psql -U postgres

If this command doesn't work then use :

$ psql -h localhost postgres -U postgres 

> create database shoal ;

> create user shoal password 'shoal' ;

> grant all on database shoal to shoal ;


> create database shoal_it ;

> create user shoal_it password 'shoal_it' ;

> grant all on database shoal_it to shoal_it ;

## Check out the code

git clone https://dev2-shoal.cloudapp.net/stash/scm/shoal/shoal-platform.git

## setup the database using flyway

$ cd db-scripts

$ mvn flyway:clean      # this will drop all tables
$ mvn flyway:migrate

## build the backend services

$ cd shoal-platform

$ mvn -U clean install -Dmaven-wagon.http.ssl.insecure=true   # ssl.insecure if you ahve used the certificate

## Run the backend services

### Setting up ShoalApp in IntelliJ

Find the ShoalApp class and right click -> Run to create a new Run configuration.

Note, you will need to add the the following to the program arguments in the run configuration dialog

--spring.config.location=./shoal-config/target/classes/dev/application.properties

You should also ensure that ShoalApp has the latest database schema everytime you run it by doing the following :

Right click on shoal-app in Intellij's project window
Select "Module settings"
Under the dependencies tab, click the "+" to add a new dependency to the classpath
 - Choose flyway-core-X.X.X.jar from your maven repository. It resides in org/flywaydb/flyway-core...
 - Choose runtime scope

Now in your Run configuration for ShoalApp :

In the spring boot settings -> override parameters :
  Add a new parameter called "flyway.locations" and set the value to "filesystem:db-scripts/src"

### Setting up ShoalApp in Eclipse

Find the ShoalApp class and right click -> Run As -> Run Configurations.

Click on Arguments and add the following to program arguments

--spring.config.location=../shoal-config/target/classes/dev/application.properties

### from the command line
$ cd ./shoal-platform/shoal-app
$ mvn spring-boot:run -Dspring.config.location=../shoal-config/target/classes/dev/application.properties

http://localhost:8080/ws/

## run the web client

$ cd shoal-client

$ npm install

$ npm test              # runs karma continuously using a browser

$ npm run build         # runs karma using phantom JS once.

$ npm start             # starts dev javascript client server in the background. Or use the following instead...

$ npm run start-watch   # starts dev javascript client server in the background with a watch on file changes so it will restart when js changed.

$ npm stop              # stops *all* background javascript client server 

Launch browser and navigate to [http://localhost:8443/app](http://localhost:8443/app)

## Run the client unit tests (Karma)

$ npm test              # runs karma continuously using a browser

$ npm run build         # runs karma using phantom JS once.

## Run the client End to End tests (Protractor)

Requires backend services to be running locally

$ cd ./shoal-client/

$ npm run protractor 

[protractor.md](./documentation/protractor.md)

## Testing the back end using Postman

[Testing using Postman](./test-tools/postman-testing.md)

## JSLint

Enabled by default for building Angular code base - keeps the code quality high by eliminating
bad syntax from Javascript.

List of disabled checks with justifications :

Unused parameter - JSLint will complain about any defined parameter in a function that is not used,
however in the likely case where you have a function (a, b) and you want to use b but not a, JSLint
will complain that 'a' is not used. Additionally when using callbacks, 
ie someTrigger = function (event, opts) you ideally want to keep the structure of the callback
intact, whereas JSLint would try to force you to write code like :
someTrigger.push( function (event) {} );
someTrigger.push( function (event, opts) {} );
even though these functions are doing the same thing, this is quite misleading.
Finally, somebody might try writing :
someTrigger.push( function (opts) {} );
And be surprised when 'opts' is actually the value of 'event'.

