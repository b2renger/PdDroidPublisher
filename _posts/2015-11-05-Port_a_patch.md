---
layout: post
title:  "Port a patch ! (big recap)"
tags: dev-guide
order: 070
---

## Getting Ready

In this post we assume that you have completed all the steps of the 'How to setup your publishing stack' post. At the last step name your new app "hillageizer", you'll probably  want to change the name of the main class in **src** folder to 'bubbles' using *refactor/rename*, and finally change the name of the folder and the patch in th **assets** folder, within your system explorer this time. (if it has not change in eclipse, just click on the project and hit F5 - refresh).

Your workspace should look like this :
![workspace]({{site.baseurl}}/img/port_a_patch/workspace.png)

and we are now ready to get started. 

Remember your source code is where you put the zip you extracted, your project files should be in here this is your code, and the eclipse workspace is something else, that helps eclipse remembering where you were.

## Pd actually means Pd Vanilla
This post is not about how to code in pd, so if you are a complete noob to pd you may want to build up your pd skills a bit before continuing. We will see how to port a patch to our system and for this we will use an example from the amazing **Andy Farnell** ! Check out [this](http://obiwannabe.co.uk/html/toys/hillageizer/hillageizer.html) ressource ! if you don't know this website yet, I suggest you take a look around :)

So just download the pd file, at the end of the page. We are lucky this patch a vanilla-friendly, if you are not familiar with the difference beetween 'extended' and 'vanilla' just look it up. We want 'vanilla' as libpd tracks 'vanilla', hopefully you'll find help on the web if you need to port patches from extended to vanilla.



## Setup you files and paths

Now we are going to integrate the patch in our project to start using the abstractions, to rig the clock, the saving system and the several views.

So just copy the **hillgeizer.pd** file in your *assets/hillageizer/* directory. And rename **hillageizer.pd** to **hillageizer-core.pd**. Now open a new patcher window and save it as **hillageizer-launcher.pd** at the root of your project, add a path by creating an object **[declare -path assets/hillageizer/]** (this is to access the abstractions of your patch), and an object **[declare -path ../pd-party/droidparty-abstractions]** (this to access the project abstractions, like the clock), then save close the patch and reopen it.

You should now be able to create an abstraction called **[clock-gui]**, and create the **[hillageizer-core]** object in your launcher patch.

## Preparing the patch

We have a great sounding patch with a lot of parameters, we will want to organize everything in several views, make it compliant to with our clock, and add our own saving system.

If you look a bit at the patch we have three main parts :

* eventshaker : the clock.
* 3 scale elements : which do scaling operations on the count.
* phasors : the synth.

The first thing we need to do is to separate the gui from the logic. Here everything is built in GOPs, we want the sliders to be alone in their own abstractions. That also means that we need to get rid of all the '$0' from the send and receive names of each gui ... We also get rid of the tempo element in the subpatch 'eventshaker' to replace it with a [clock] object.

Here is what he can look like once it's done :

![hillageizer-clean]({{site.baseurl}}/img/port_a_patch/hillageizer-clean.png)

Now have a patch conforming to the best practives we have described.

## Setting the views

The pd side of things as already been done before. We now just have to tell our android launcher which are which.

First we have to declare a config variable :

{% highlight java %} 
PdDroidPartyConfig config = new PdDroidPartyConfig();
{% endhighlight %}

the line is likely to be marked as an error, you can click on the error icon and select the *import* proposition to fix it. (It will import the PdDroidPartyConfig class from our base code).

This being done, you can move on to add your views, first the **gui**-view, and then the **core**-patch.

{% highlight java %} 
config.guiPatches.put("Sequencer", "hillageizer/hillageizer-sequencer.pd");
config.guiPatches.put("Effects", "hillageizer/hillageizer-effects.pd");
config.corePatches.add("hillageizer/hillageizer-core.pd");
{% endhighlight %}

At this point you can launch you app, by right-clicking on the project and select *Runs as/Android Application*. It should run with two views.

The aspect ratio is probably a little off.

## Presets saving

## Gui cutsomization

## Publishing

To publish you'll need a developper account for the play store.
