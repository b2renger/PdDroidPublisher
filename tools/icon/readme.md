This folder contains shell scripts to populate project folders with icons. It exports all sizes from 36x36 to 192x192
with an extra 512x512 icon (exported in a web folder) used to publish app on market.
(http://stackoverflow.com/questions/12768128/android-launcher-icon-size)

To populate all samples projects, just launch the batch script

```
./populate_samples_with_icons.sh
```

To populate a specific project with a specific SVG (for instance ppp-icon.svg to Acid-box sample) just type :

```
./populate_with_icons.sh ppp-icon.svg ../../samples/demo-AcidBox/
```

Note : if you are on a fat32 drive or any non executable context, you have to run script with bash :
```
bash populate_with_icons.sh ppp-icon.svg ../../samples/demo-AcidBox/
```

If you can't run shell script you can do it manually by export each icon to a png of the right size and copy it to the appropriate folder.