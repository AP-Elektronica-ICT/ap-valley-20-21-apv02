#!/usr/bin/env bash

# Build APK Files
./ap-valley-20-21-apv02 gradlew assembleDebug
echo "Starting emulator"

# Start emulator in background
./AppData/Local/Android/Sdk/emulator emulator -avd Pixel_3_XL_API_29
echo "Emulator started"

# Install apk-package
./ap-valley-20-21-apv02 adb install app/build/outputs/apk/debug/app-debug.apk
echo "Success"
