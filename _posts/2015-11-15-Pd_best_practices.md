---
layout: post
title:  "Pd best practices"
tags: dev-guide
order: 030
---

To fully benefit of all the features of the project you will want to use some custom made abstractions, available to all users. Those abstractions are the core of our system to developp apps, you will find stuff to deal with the beat sharing clock, saving presets, and specific abstractions to interact with a touchscreen or display a list of symbols.

They are all located in the */puredata* release folder, and each one has a help patch.

But first let's talk a bit how to workflow to interface pd with the code hierarchy of the eclipse project.

First you should prefer to work in a project folder, that is to say in the **assets** folder of a new project in folder named after you app name.

We advise you to create a pd **'launcher' patch** at the base of your project, to simulate the way it works on android when working on your desktop computer. In the AcidBass example project it is called *AcidBass_launcher_desktop.pd*, and it looks like this :

![pd launcher]({{site.baseurl}}/img/pd_desktop_launcher.png) TODOC : change path to "puredata" folder ? Yes ! you are right :)

This pd patch is only for use in desktop, and in developpement mode, it is not embeded in your application.

It contains a path to our project folder containing the abstractions listed below.

It also contains a path to the *assets/Acid_Bass* folder which contains each **gui-view** of the patch built as an abstraction containing only gui objects. And a **core abstraction** which contains all the audio, the logic, and the saving system implementation.

This setup will help you to build several gui view stacked in tabs for your app. You can check the 'how to configure your launcher post' to learn more about this view system.