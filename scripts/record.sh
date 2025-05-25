#!/bin/bash

# App Name:
# tetris_flutter, tetris_android, tetris_compose
# tictactoe_flutter, tictactoe_android, tictactoe_compose
APP_NAME="tetris_flutter"
# Package Name:
# com.example.$APP_NAME
# com.robin.bachelor.thesis.$APP_NAME
PACKAGE_NAME="com.example.$APP_NAME"

echo "Start recording: $APP_NAME"

# Process ID
APP_PID=$(adb shell pidof $PACKAGE_NAME)
echo PID: $APP_PID

# User ID
APP_UID_RAW=$(adb shell top -n 1 -b -p $APP_PID | grep $APP_PID | awk '{print $2}')
APP_UID=$(echo $APP_UID_RAW | tr -d '_')
echo UID: $APP_UID

LOG_FILE="tacelog_${APP_NAME}.txt"

# Reset battery
adb shell dumpsys batterystats --reset

for i in {1..100}; do
    CPU=$(adb shell top -n 1 -b -p $APP_PID | grep $APP_PID | awk '{print $9}')
    RAM=$(adb shell dumpsys meminfo $APP_PID | grep "TOTAL PSS" | awk '{print $3}')
    echo "$CPU, $RAM" >> $LOG_FILE
    sleep 1
done

POWER=$(adb shell dumpsys batterystats | grep 'Estimated power use' -A 20 | grep $APP_UID | awk '{print $3}')
echo $POWER >> $LOG_FILE

echo "finish recording"