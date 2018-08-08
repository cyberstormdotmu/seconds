#!/bin/bash

case "${1:-''}" in
    'start')
        if test -f /tmp/start_virtual_display.pid
        then
            echo "Start Virtual Display (Xvfb) is already running."
        else

            Xvfb :99 -ac -screen 0 1920x1200x8 > /var/log/start_virtual_display/output.log 2> /var/log/start_virtual_display/error.log & echo $! > /tmp/start_virtual_display.pid

            error=$?
            if test $error -gt 0
            then
                echo "${bon}Error $error! Couldn't start Virtual Display (Xvfb)!${boff}"
            fi
        fi
    ;;
    'stop')
        if test -f /tmp/start_virtual_display.pid
        then
            echo "Stopping Virtual Display (Xvfb)..."
            PID=`cat /tmp/start_virtual_display.pid`
            kill -3 $PID
            if kill -9 $PID ;
                then
                    sleep 2
                    test -f /tmp/start_virtual_display.pid && rm -f /tmp/start_virtual_display.pid
                else
                    echo "Virtual Display (Xvfb) could not be stopped..."
                fi
        else
            echo "Virtual Display (Xvfb) is not running."
        fi
        ;;
    'restart')
        if test -f /tmp/start_virtual_display.pid
        then
            kill -HUP `cat /tmp/start_virtual_display.pid`
            test -f /tmp/start_virtual_display.pid && rm -f /tmp/start_virtual_display.pid
            sleep 1

            Xvfb :99 -ac -screen 0 1920x1200x8 > /var/log/start_virtual_display/output.log 2> /var/log/start_virtual_display/error.log & echo $! > /tmp/start_virtual_display.pid

            echo "Restart Virtual Display (Xvfb)..."
        else
            echo "Virtual Display (Xvfb) isn't running..."
        fi
        ;;
    *)      # no parameter specified
        echo "Usage: $SELF start|stop|restart"
        exit 1
    ;;
esac