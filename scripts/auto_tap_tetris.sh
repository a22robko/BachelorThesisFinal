#!/bin/bash

counter=0

while true; do
    keycode=$((RANDOM % 4 + 19))

    adb shell input keyevent $keycode

    if (( counter % 10 == 0 )); then
        adb shell input tap 820 1340
    fi

    ((counter++))

    sleep 0.6
done
