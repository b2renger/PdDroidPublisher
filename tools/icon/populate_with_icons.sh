#!/bin/bash
# create icons from svg to png
echo 'starting export'
inkscape -f $1 -w 36 -h 36 -e $2res/drawable-ldpi/ic_launcher.png
echo 'ldpi done'
inkscape -f $1 -w 48 -h 48 -e $2res/drawable-mdpi/ic_launcher.png
echo 'mdpi done'
inkscape -f $1 -w 64 -h 64 -e $2res/drawable-tvdpi/ic_launcher.png
echo 'tvdpi done'
inkscape -f $1 -w 72 -h 72 -e $2res/drawable-hdpi/ic_launcher.png
echo 'hdpi done'
inkscape -f $1 -w 96 -h 96 -e $2res/drawable-xhdpi/ic_launcher.png
echo 'xhdpi done'
inkscape -f $1 -w 144 -h 144 -e $2res/drawable-xxhdpi/ic_launcher.png
echo 'xxhdpi done'
inkscape -f $1 -w 192 -h 192 -e $2res/drawable-xxxhdpi/ic_launcher.png
echo 'xxxhdpi done'
inkscape -f $1 -w 512 -h 512 -e $2res/drawable-web/ic_launcher.png
echo 'web done'
echo 'all done!'	