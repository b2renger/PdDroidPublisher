---
layout: post
title:  "Anatomy of a project folder"
categories: How to, pd developer
published : true
---

A project folder contains a lot of things. You are mainly interested by a few of the files and folders in here.

#assets
It is the main folder to store your **Pd** patch, abstractions, sound files, presets etc. 

#src
This folder is for the **code**. It contains the *launcher* file which is java file in which you can do all sorts of configurations stunts. This is where you can tell : what pd file to launch and how, if you want to use our midi-clock, if you want to customize graphic display of some components, and where you specify the path to store banks of presets.

This file is located deep in the hierarch and called MainActivity.java
src/net/mgsx/ppp/samples/myproject/MainActivity.java

#res
In this folder you will find :

* in *values* the *strings.xml* which you need to update if you want to change the name of your project.
* all the icons exported at several sizes in each of the *"drawable-."* folders.

You can check this discussion on [stackoverflow](http://stackoverflow.com/questions/12768128/android-launcher-icon-size) about android icon sizes.

As a bonus on the github repository in the [tools/icon/](https://github.com/b2renger/PdDroidPublisher/tree/master/tools/icon) folder you can find a shell script to populate a project with a specific icon.

Note : we do provide an icon, you can of course change it to what suits you best.

#AndroidManifes.xml

This is your android manifest. You need to change it if you want to change your application name.
You can also set versions there when you will need to export signed apk for the android marker.
