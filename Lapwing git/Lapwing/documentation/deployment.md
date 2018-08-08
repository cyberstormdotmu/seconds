# Deploying the Shoal Platform to an Environment

If this is the first deployment to the environment, ensure it has been provisioned according to the instructions in [provision-environment.md](provision-environment.md)

#### Build the Release


#### Stage the Files
SCP the deployment zip file (e.g. deployment-0.0.1-SNAPSHOT-demo.zip) onto the server, then extract it to /opt/shoal

```
$ sudo -u shoal unzip -d /opt/shoal deployment-0.0.1-SNAPSHOT-demo.zip
```


#### Stop the Existing Service
```
$ /opt/shoal/current/bin/shoalapp.sh stop
```

#### Switch Over the Symbolic Link
```
$ cd /opt/shoal
$ sudo -u shoal rm current
$ sudo -u shoal ln -s shoal-platform-0.0.1-SNAPSHOT current
```
(In the above, replace shoal-platform-0.0.1-SNAPSHOT with the name of the new deployment)
#### Make the scripts executable
```
$ chmod +x /opt/shoal/current/bin/flyway.sh
$ chmod +x /opt/shoal/current/bin/shoalapp.sh
```

#### Migrate the Database
```
$ /opt/shoal/current/bin/flyway.sh migrate
```

#### Start the Service
```
$ /opt/shoal/current/bin/shoalapp.sh start
```
