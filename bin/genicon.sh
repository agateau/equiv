#!/bin/sh -e

cd $(dirname $0)/..

EXTRA_DIR=extra
RES_DIR=app/src/main/res

SVG_FILE=icons/ic_launcher.svg

NAME=ic_launcher.png

EXPORT_AREA=0:0:24:24

while read size dir ; do
    echo
    echo "# $size => $dir"
    echo
    size_args="--export-width $size --export-height $size"
    mkdir -p $dir
    inkscape --export-area $EXPORT_AREA $size_args --export-png $dir/$NAME $SVG_FILE
done <<EOF
 48 $RES_DIR/mipmap-mdpi
 72 $RES_DIR/mipmap-hdpi
 96 $RES_DIR/mipmap-xhdpi
144 $RES_DIR/mipmap-xxhdpi
EOF
