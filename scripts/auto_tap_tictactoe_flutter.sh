#!/bin/bash

while true; do
    for cmd in \
        "adb shell input tap 200 900" \
        "adb shell input tap 200 1200" \
        "adb shell input tap 200 1500" \
        "adb shell input tap 500 900" \
        "adb shell input tap 500 1200" \
        "adb shell input tap 500 1500" \
        "adb shell input tap 800 900" \
        "adb shell input tap 800 1200" \
        "adb shell input tap 800 1500"
    do
        eval "$cmd"
        sleep 0.6
    done
done