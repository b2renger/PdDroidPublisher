This folder contains a shell script to populate project folders with an icon. It exports all sizes from 36x36 to 512x512.
(http://stackoverflow.com/questions/12768128/android-launcher-icon-size)


For instance to populate the sample Acid-box with the ppp-icon.svg file just type :

```
./populate_with_icons.sh ppp-icon.svg ../../samples/demo-AcidBox/
```

if you are on a fat32 drive
```
bash populate_with_icons.sh ppp-icon.svg ../../samples/demo-AcidBox/
```

If you can't run shell script you can do it manually by export each icon to a png of the right size and copy it to the appropriate folder.