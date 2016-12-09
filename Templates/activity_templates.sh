#!/bin/bash
DIR=/Applications/Android\ Studio\ Stable.app/Contents/plugins/android/lib/templates/activities/HeartEyesActivityTmp/
if [ -d "$DIR" ]; then
    printf '%s\n' "Template is exist,deleting it---->($DIR)"
    rm -rf "$DIR"
fi
    cp -r HeartEyesActivityTmp/ "$DIR"

