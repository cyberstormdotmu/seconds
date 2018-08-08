#!/bin/sh

BASE_DIR=/opt/shoal/current
JAR_FILE=${BASE_DIR}/lib/shoal-app-0.0.16.jar

PROPERTIES=${BASE_DIR}/config/application.properties
if [ -f "${BASE_DIR}/../properties/override.properties" ]
then
    PROPERTIES=${PROPERTIES},${BASE_DIR}/../properties/override.properties
fi

name="shoal"
command="/usr/bin/java"
command_args="-jar ${JAR_FILE} --spring.config.location=${PROPERTIES}"
daemon="/usr/bin/daemon"

[ -x "$daemon" ] || exit 0
[ -x "$command" ] || exit 0

daemon_start_args=""

pidfiles="/var/run/shoal"
user="shoal"

mkdir -p $pidfiles
chown $user $pidfiles

chroot=""
chdir=""
umask=""
stdout="daemon.info"
stderr="daemon.err"

case "$1" in
    start)
        if "$daemon" --running --name "$name" --pidfiles "$pidfiles"
        then
            echo "$name is already running."
        else
            echo -n "Starting $name..."
            "$daemon" --respawn $daemon_start_args \
                --name "$name" --pidfiles "$pidfiles" \
                ${user:+--user $user} ${chroot:+--chroot $chroot} \
                ${chdir:+--chdir $chdir} ${umask:+--umask $umask} \
                ${stdout:+--stdout $stdout} ${stderr:+--stderr $stderr} \
                -- \
                "$command" $command_args
            echo done.
        fi
        ;;

    stop)
        if "$daemon" --running --name "$name" --pidfiles "$pidfiles"
        then
            echo -n "Stopping $name..."
            "$daemon" --stop --name "$name" --pidfiles "$pidfiles"
            echo done.
        else
            echo "$name is not running."
        fi
        ;;

    restart|reload)
        if "$daemon" --running --name "$name" --pidfiles "$pidfiles"
        then
            echo -n "Restarting $name..."
            "$daemon" --restart --name "$name" --pidfiles "$pidfiles"
            echo done.
        else
            echo "$name is not running."
            exit 1
        fi
        ;;

    status)
        "$daemon" --running --name "$name" --pidfiles "$pidfiles" --verbose
        ;;

    *)
        echo "usage: $0 <start|stop|restart|reload|status>" >&2
        exit 1
esac

exit 0