#!/bin/bash
# create icons from svg to png
echo 'starting export'

mkdir -p $2/res/drawable-ldpi
inkscape -f $1 -w 36 -h 36 -e $2/res/drawable-ldpi/ic_launcher.png
echo 'ldpi done'

mkdir -p $2/res/drawable-mdpi
inkscape -f $1 -w 48 -h 48 -e $2/res/drawable-mdpi/ic_launcher.png
echo 'mdpi done'

mkdir -p $2/res/drawable-tvdpi
inkscape -f $1 -w 64 -h 64 -e $2/res/drawable-tvdpi/ic_launcher.png
echo 'tvdpi done'

mkdir -p $2/res/drawable-hdpi
inkscape -f $1 -w 72 -h 72 -e $2/res/drawable-hdpi/ic_launcher.png
echo 'hdpi done'

mkdir -p $2/res/drawable-xhdpi
inkscape -f $1 -w 96 -h 96 -e $2/res/drawable-xhdpi/ic_launcher.png
echo 'xhdpi done'

mkdir -p $2/res/drawable-xxhdpi
inkscape -f $1 -w 144 -h 144 -e $2/res/drawable-xxhdpi/ic_launcher.png
echo 'xxhdpi done'

mkdir -p $2/res/drawable-xxxhdpi
inkscape -f $1 -w 192 -h 192 -e $2/res/drawable-xxxhdpi/ic_launcher.png
echo 'xxxhdpi done'

mkdir -p $2/web
inkscape -f $1 -w 512 -h 512 -e $2/web/ic_launcher.png
echo 'web done'

echo 'all done!'	