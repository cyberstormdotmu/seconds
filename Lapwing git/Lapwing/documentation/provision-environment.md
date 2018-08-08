# Provision a new deployment environment

Create a new VM with Ubuntu 14.04 LTS and log into it as a user with sudo capability.

#### Update the VM with the latest packages
```
$ sudo apt-get update
$ sudo apt-get upgrade
```

#### Create the Application User
Create a shoal user, setting an appropriate password and accepting all the defaults.

```
$ sudo adduser shoal
```

#### Create the Deployment and Log Directories
Create directories to hold the application files and set ownership to the shoal user.

```
$ sudo mkdir /opt/shoal
$ sudo mkdir /opt/shoal/properties
$ sudo touch /opt/shoal/properties/override.properties
$ sudo touch /opt/shoal/properties/flyway.conf
$ sudo chown -R shoal:shoal /opt/shoal
```

#### Create the Log Directory
Create the directory to hold the application log files and set ownership to the shoal user.

```
$ sudo mkdir /var/log/shoal
$ sudo chown shoal:shoal /var/log/shoal
```

#### Install build tools, TCL and unzip
These are needed later for building Redis.

```
$ sudo apt-get install build-essential
$ sudo apt-get install tcl8.5
$ sudo apt-get install unzip
```

#### Install Oracle Java 8
You will need to accept the Oracle licence terms

```
$ sudo add-apt-repository ppa:webupd8team/java
$ sudo apt-get update
$ sudo apt-get install oracle-java8-installer
```
If you get a checksum error on the downloaded JDK jdk-8u66-linux-x64.tar.gz file (or alternate version), delete it from within /var/cache/oracle-jdk8-installer (use 'sudo rm') and try again.

Check that Java has been successfully installed and the correct version is in use.

```
$ java -version
java version "1.8.0_66"
Java(TM) SE Runtime Environment (build 1.8.0_66-b17)
Java HotSpot(TM) 64-Bit Server VM (build 25.66-b17, mixed mode)
```

#### Install and Configure nginx
```
$ sudo apt-get install nginx
$ sudo mkdir /etc/nginx/ssl
```
Place the SSL certificate into /etc/nginx/ssl or if using a self-signed certificate generate one.

Copy the shoal.conf file from src/main/resources in the deployment module into /etc/nginx/conf.d

Remove the default configuration from /etc/nginx/sites-enabled

```
$ sudo rm /etc/nginx/sites-enabled/default
```
Restart nginx to pick up the configuration changes.

```
$ sudo service nginx restart
```


#### Generating a self-signed SSL certificate
If you are using a self-signed certificate with nginx...

```
sudo openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout /etc/nginx/ssl/nginx.key -out /etc/nginx/ssl/nginx.crt
```
Fill out the prompts appropriately. The most important line is the one that requests the Common Name - for that enter the domain name to be associated with the server (e.g. demo-shoal.cloudapp.net).



#### Install and Configure PostgreSQL
Install PostgreSQL

```
$ sudo apt-get install postgresql postgresql-contrib
```

Run psql as the postgres user

```
$ sudo -i -u postgres psql
```
Create the new database and a login role with appropriate password and grant permissions on the new database to it.

```
postgres=# create database shoal;
CREATE DATABASE
postgres=# create role shoal password 'DB_PASSWORD';
CREATE ROLE
postgres=# alter role shoal with login;
ALTER ROLE
postgres=# grant all on database shoal to shoal;
GRANT
```
Quit psql

```
postgres=# \q
```
Edit the override.properties file to add connection details for the database.

```
$ sudo vi /opt/shoal/properties/override.properties
```
Add the following to the file, substituting in the password that was used when creating the shoal role in PostgreSQL:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/shoal
spring.datasource.username=shoal
spring.datasource.password=DB_PASSWORD
```
Edit the flyway.conf file to add connection details for the database and to set the location of the migration scripts.

```
$ sudo vi /opt/shoal/properties/flyway.conf
```
Add the following to the file, substituting in the password that was used when creating the shoal role in PostgreSQL:

```
flyway.url=jdbc:postgresql://localhost:5432/shoal
flyway.user=shoal
flyway.password=DB_PASSWORD
flyway.locations=filesystem:/opt/shoal/current/db-scripts/main
```
If the environment is one in which the test data scripts need to be run to populate the database with test data, the flyway.locations line should be:

```
flyway.locations=filesystem:/opt/shoal/current/db-scripts/main,filesystem:/opt/shoal/current/db-scripts/test
```


#### Install Redis
First download and build Redis from source.

```
$ wget http://download.redis.io/releases/redis-3.0.5.tar.gz
$ tar xzf redis-3.0.5.tar.gz
$ cd redis-3.0.5/deps
$ make hiredis lua jemalloc linenoise
$ cd ..
$ make
$ make test
```

Provided the tests were successful install Redis, accepting the defaults during installation.

```
$ sudo make install
$ cd utils
$ sudo ./install_server.sh
```

#### Install Flyway
```
$ wget https://bintray.com/artifact/download/business/maven/flyway-commandline-3.2.1-linux-x64.tar.gz
$ tar xzf flyway-commandline-3.2.1-linux-x64.tar.gz
$ sudo mv flyway-3.2.1 /opt
$ cd /opt
$ sudo ln -s flyway-3.2.1 flyway
$ sudo chown -R root:root flyway-3.2.1
$ sudo chmod 755 flyway/flyway
```

#### Deploy the Shoal Platform
The environment is now ready for the Shoal Platform to be deployed. Refer to [deployment.md](deployment.md) for deployment instructions.
