---
layout: post
title:  "How to customise your gui"
tags: advanced-design
order: 020
---

We will cover :

* [the config variable](#config)<br>
* [aspect ratio](#aspect-ratio)<br>
* [color themes](#color-theme)<br>
* [factory components](#factory-components)<br>
* [custom overrides](#custom-overrides)<br>

We provide several level of gui customization by adding a few lines of code to you launcher (ie *MainActivity.java*). By default you have some simple pd classic gui, with the default colors from your original patch. 


<a name="config"/>

### Create a config variable

First you need to import the config class from ppp, add the following line at the top of your main *.java file the one located in your /src folder in eclipse.

{% highlight java %} 
import net.mgsx.ppp.PdDroidPartyConfig;
{% endhighlight %}

After that you can create a new config variable, right after this line :
{% highlight java %} 
super.onCreate(savedInstanceState);
{% endhighlight %}

in the main java class. You just need to copy this line : 
{% highlight java %} 
PdDroidPartyConfig config = new PdDroidPartyConfig();
{% endhighlight %}
this will give you access to new customization function !

<a name="aspect-ratio"/>

### Aspect ratio

By default ppp will strech you view to match the size of you screen. If you want the gui to keep the same look by not getting stretched away by several screen ratios once you've created the config variable below.

{% highlight java %} 
config.guiKeepAspectRatio = true;
{% endhighlight %}

<a name="color-theme"/>

### Apply a color theme

Here is how to apply one of themes shipped with ppp, again you need to have created the config variable :

{% highlight java %} 
config.theme = new MonochromeTheme(MonochromeTheme.RED, true);
{% endhighlight %}

We have a few colors already setup for you, in eclipse you can remove the *.RED* part and press *ctrl + space* to activate auto-completion and check which other colors are available.

You can also create a theme with your own colors, you will then have to specify 3 colors, first the main background color, then the widgets background color, and finally the widgets foreground color expressed in hexadecimal form :

{% highlight java %} 
config.theme = new MonochromeTheme(0xff101933, 0xff202943, 0xff004ce6);
{% endhighlight %}

To find the hexadecimal code you can consult a website like this [one](http://www.color-hex.com/). An hexadecimal color code is composed of 6 characters numbers or letters, and you have to add the prefix **0xff**. In the above line the colors are different kinds of blue.

TODOC : more about SVG and/or PNG theming. even if SVG is not fully supported it's an experimental feature ...

<a name="factory-components"/>

### Use factory components 

Those are ready to be used for slick look, almost out of the box. You can refer to [this]({{ site.baseurl}}{% post_url 2015-11-04-List_overrides %}) post to get a list of all possible overrides.

To take a simple example, you can apply a custom look, on all the sliders of your patch by using **typeOverrides**, for instance the line of code below once added to you launcher will make your app draw all sliders as filled rounded rectangles.

{% highlight java %} 
config.typeOverrides.put(Slider.class, RibbonSlider.class);
{% endhighlight %}

You can also apply this look to only one slider, the slider with the label 'mySlider', set via properties directly in Pure-Data.

{% highlight java %} 
config.objectOverrides.put("mySlider", RibbonSlider.class);
{% endhighlight %}

After adding a line like this one in your launcher you may end up with an error, you just have to use the keyboard shortcut *ctrl + shift + o* to import the missing classes in you package.

FYI this manipulation (for a slider) will just write those two lines of code at the top of the *MainActivity.java* file.

{% highlight java %} 
import net.mgsx.ppp.widget.core.Slider;
import net.mgsx.ppp.widget.custom.RibbonSlider;
{% endhighlight %}

<a name="custom-overrides"/>

### Develop custom overrides of components

You can developp custom overrides of components, those overrides can be graphical, but also on the interaction level. At this level you might want to consult the [wiki](https://github.com/b2renger/PdDroidPublisher/wiki). 

The AcidBass example features a simple override to draw an array with a custom grid. The Wobbler example features a more advanced example combining two horizontal radio buttons linked to a slider to select the wobbling speed !