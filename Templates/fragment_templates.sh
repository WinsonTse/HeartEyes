#!/bin/bash
DIR=/Applications/Android\ Studio\ Stable.app/Contents/plugins/android/lib/templates/other/HeartEyesFragmentTmp/
if [ -d "$DIR" ]; then
    printf '%s\n' "Template is exist,deleting it---->($DIR)"
    rm -rf "$DIR"
fi
    cp -r HeartEyesFragmentTmp/ "$DIR"

