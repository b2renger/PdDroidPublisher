---
layout: post
title:  "How to configure your launcher"
categories: How to, pd developer
published : true
---

This section is about how to use the :

* [Multi-view support](#multi-view)<br>
* [Gui customization](#gui-custom)<br>
	 * [color themes](#color-theme)<br>
	 * [factory components](#factory-components)<br>
	 * [custom overrides](#custom-overrides)<br>
* [Persist saving system](#persist)<br>

We will base our observations on the Acid Bass example which uses all of these features. But first let's discuss best practices a bit.

## Pure Data best practices

We advise you to create a pd 'launcher' file at the base of you project, to simulate the way it works on android when working on your desktop computer. In the AcidBass example project it is called *AcidBass_launcher_desktop.pd*, and it looks like this :

![pd launcher]({{site.baseurl}}/img/pd_desktop_launcher.png)

This pd patch is only for use in desktop, and in developpement mode, it is not embarked in your application.

It contains a path to our project folder containing specific abstractions such as [clock-gui], or [clock] to control the playback of a sequencer via our jamming system, or other specific abstractions usefull for the project (persist, taplist, touch ...)

It also contains a path to the *assets/Acid_Bass* folder which contains each view of the patch built as an abstraction and incidentally the topic of the next section.


<a name="multi-view"/>

## Multi-view support

The best way to benefit from the multi-view feature, is to break your patch into several abstractions. You will probably want to seperate the gui from the audio core, and probably break the gui into pannels.

This is the case in the Acid_Bass project :

* **AcidBass_core.pd** : is the full pd patch. It contains the dsp code, the logic of the sequencer, the clock instanciation, the load and save mechanism instanciation ... well pretty much everything except the gui. 

* **AcidBass_audioControls**, **AcidBass_noteOptions.pd**, **AcidBass_noteSelector.pd** : are each a pannel of the gui views, containing pd gui objects, and custom abstractions. Each object communicates with **AcidBass_core.pd** via the global pd **[send]** and **[receive]** system.

Notice the **[declare -path ../../../../pd-party/droidparty-abstractions]** object again, in each of those abstractions.

Now in eclipse in your launcher file (ie *MainActivity.java*) right after the line

{% highlight java %}  PdDroidPartyConfig config = new PdDroidPartyConfig(); {% endhighlight %}

you can begin the instanciation of your views :

{% highlight java %} 
config.guiPatches.put("Harmony", "Acid_Bass/AcidBass_noteSelector.pd");
config.guiPatches.put("Note Options", "Acid_Bass/AcidBass_noteOptions.pd");
config.guiPatches.put("Audio Controls", "Acid_Bass/AcidBass_audioControls.pd");
{% endhighlight %}

This is the code for the gui view, the first argument passed is the name of the view, the second one is the path to the abstraction to be loaded in this view.

Once it is done, you can instanciate your core patch :

{% highlight java %} 
config.corePatches.add("Acid_Bass/AcidBass_core.pd");
{% endhighlight %}

And finally proceed with the launch :

{% highlight java %} 
PdDroidPartyLauncher.launch(this, config);
{% endhighlight %}

* Note 1 : It is best to load your gui views before your core patches for initializations issues that will be discussed in more details in the [Persist saving system section](#persist).

* Note 2 : Currently we adivse you not to duplicate gui elements from one view to another as tempting as it may be. This stands more specifically for arrays, as pure-data does not allow you to have multiple arrays with the same name. You can report [here](https://github.com/b2renger/PdDroidPublisher/issues/13) if you want to comment on that.

<a name="gui-custom"/>

## Gui-customization

We provide several level of gui customization by default you have some simple pd classic gui, with the default colors from your original patch. By adding a few lines of code to you launcher (ie *MainActivity.java*)

<a name="color-theme"/>

### Apply a color theme

{% highlight java %} 
config.theme = new MonochromeTheme(MonochromeTheme.RED, true);
{% endhighlight %}

We have a few colors already setup for you, in eclipse you can remove the *.RED* part and press *ctrl + space* to activate auto-completion and check which other colors are available.

You can also create a theme with your own colors, you will then have to specify 3 colors, first the main background color, then the background color, and finally the foreground color expressed in hexadecimal form :

{% highlight java %} 
config.theme = new MonochromeTheme(0xff101933, 0xff202943, 0xff004ce6);
{% endhighlight %}

To find the hexadecimal code you can consult a website like this [one](http://www.color-hex.com/). An hexadecimal color code is composed of 6 characters numbers or letters, and you have to add the prefix **0xff**. In the above line the colors are different kinds of blue.

<a name="factory-components"/>

### Use factory components 

Those are ready to be used for slick look, almost out of the box. You can refer to [this](link missing) post to get a list of all possible overrides.

To take a simple example, you can apply a custom look, on all the sliders of your patch by using **typeOverrides**, for instance the line of code below once added to you launcher will make your app draw all sliders as filled rounded rectangles.

{% highlight java %} 
config.typeOverrides.put(Slider.class, RibbonSlider.class);
{% endhighlight %}

You can also apply this lool to only one slider, the slider with the label 'mySlider', set via properties directly in Pure-Data.

{% highlight java %} 
config.objectOverrides.put("mySlider", RibbonSlider.class);
{% endhighlight %}

After adding a line like this one in your launcher you may end up with an error, you just have to use the keyboard shortcut *ctrl + shift + o* to import the missing classes in you package.

FYI this manipulation (for a slider) will just write those two lines of code at the top of the **MainActivity.java** file.

{% highlight java %} 
import net.mgsx.ppp.widget.core.Slider;
import net.mgsx.ppp.widget.custom.RibbonSlider;
{% endhighlight %}

<a name="custom-overrides"/>

### Develop custom overrides of components

You can developp custom overrides of components, those overrides can be graphical, but also on the interaction level. At this level you might want to consult the [wiki](https://github.com/b2renger/PdDroidPublisher/wiki). 

The AcidBass example features a simple override to draw an array with a custom grid. The Wobbler example features a more advanced example combining two horizontal radio buttons linked to a slider to select the wobbling speed !


<a name="persist"/>

## Persist saving system

talk about https://github.com/b2renger/PdDroidPublisher/issues/23