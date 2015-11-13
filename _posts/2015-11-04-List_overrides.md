---
layout: post
title:  "List of available overrides"
tags: dev-guide
order: 070
---

The system of override is a way to customize a gui component from pd and transform it into something else. You can just override the **graphics**, or you can re-code the **behavior**. We have built a few overrides as 'out of the box' features to customise your apps.

You can use those features by configuring them in the launcher, you can check the [How to configure your launcher]({{site.baseurl}}/_post/2015-11-01-How_to_configure_your_launcher#factory-components) post.

* [Filled Toggle](#ftgl)<br>
* [Popup Taplist](#ptap)<br>
* [Ribbon Slider](#rslider)<br>
* [Simple Bang](#sbang)<br>
* [Simple Radio](#sradio)<br>
* [Switch Toggle](#stgl)<br>


<a name="ftgl"/>

# FilledToggle 
The FilledToggle is a **graphic** override.
It replaces the basic toggle from pd, to a rounded rectangle, no more cross when active, instead the rectangle is filled.

![FilledToggle]({{site.baseurl}}/img/overrides/filledToggle.png)


To use it you need to import those classes to your launcher :
{% highlight  java%} 
import net.mgsx.ppp.widget.core.Toggle;
import net.mgsx.ppp.widget.custom.FilledToggle;
{% endhighlight %}

If you want to override all components in your gui you can do it by adding this line of code to your launcher :

{% highlight  java%} 
config.typeOverrides.put(Toggle.class, FilledToggle.class);
{% endhighlight %}

If you want to override only the toggle labelled 'tgl', this line :

{% highlight  java%} 
config.objectOverrides.put("tgl", FilledToggle.class);
{% endhighlight %}

<a name="ptap"/>

# PopupTaplist
The PopupTaplist is a **behavior** override.

The comportement aspect, is advanced, as it uses android layout to display a browsable list of elements, you just have to click to select the new value.


![Popup]({{site.baseurl}}/img/overrides/popup.png)

To use it you need to import those classes to your launcher :
{% highlight  java%} 
import net.mgsx.ppp.widget.abs.Taplist;
import net.mgsx.ppp.widget.custom.PopupTaplist;
{% endhighlight %}

If you want to override all components in your gui you can do it by adding this line of code to your launcher :

{% highlight  java%} 
config.typeOverrides.put(Taplist.class, PopupTaplist.class);
{% endhighlight %}

If you want to override only the toggle labelled 'taplist', this line :

{% highlight  java%} 
config.objectOverrides.put("taplist", PopupTaplist.class);
{% endhighlight %}

<a name="rslider"/>

# RibbonSlider
The RibbonSlider is a **graphic** override. The slider now has nice rounded angles, and the level selected is clearly represented as filled rectangle, instead of just a simple line. It applies to vertical and horizontal sliders.


![vslider]({{site.baseurl}}/img/overrides/vslider.png)
![hslider]({{site.baseurl}}/img/overrides/hslider.png)

To use it you need to import those classes to your launcher :
{% highlight  java%} 
import net.mgsx.ppp.widget.core.Slider;
import net.mgsx.ppp.widget.custom.RibbonSlider;
{% endhighlight %}

If you want to override all components in your gui you can do it by adding this line of code to your launcher :

{% highlight  java%} 
config.typeOverrides.put(Slider.class, RibbonSlider.class);
{% endhighlight %}

If you want to override only the toggle labelled 'vsl', this line :

{% highlight  java%} 
config.objectOverrides.put("vsl", RibbonSlider.class);
{% endhighlight %}

<a name="sbang"/>

# SimpleBang
The SimpleBang is a **graphic** override. No more rectangle framing the circle, it's now nice and round with a thick edge.

![bang]({{site.baseurl}}/img/overrides/bang.png)

To use it you need to import those classes to your launcher :
{% highlight  java%} 
import net.mgsx.ppp.widget.core.Bang;
import net.mgsx.ppp.widget.custom.SimpleBang;
{% endhighlight %}

If you want to override all components in your gui you can do it by adding this line of code to your launcher :

{% highlight  java%} 
config.typeOverrides.put(Bang.class, SimpleBang.class);
{% endhighlight %}

If you want to override only the toggle labelled 'bang', this line :

{% highlight  java%} 
config.objectOverrides.put("bang", SimpleBang.class);
{% endhighlight %}

<a name="sradio"/>

# SimpleRadio
The SimpleRadio is a **graphic** override. The index of the radio is now marked with a filled rectangle, and buttons are separated. The displayed widget is half the height of its pd implementation, this way if you put a label, with the top part of your font aligned to the top line of the radio, the label is just above it. As for the slider override it applies regardless of the widget orientation.

![vradio]({{site.baseurl}}/img/overrides/vradio.png)
![hradio]({{site.baseurl}}/img/overrides/hradio.png)

To use it you need to import those classes to your launcher :
{% highlight  java%} 
import net.mgsx.ppp.widget.core.Radio;
import net.mgsx.ppp.widget.custom.SimpleRadio;
{% endhighlight %}

If you want to override all components in your gui you can do it by adding this line of code to your launcher :

{% highlight  java%} 
config.typeOverrides.put(Radio.class, SimpleRadio.class);
{% endhighlight %}

If you want to override only the toggle labelled 'hradio', this line :

{% highlight  java%} 
config.objectOverrides.put("hradio", SimpleRadio.class);
{% endhighlight %}

<a name="stgl"/>

# SwitchToggle
The SwitchToglle is a **graphic** and a **behavior** override. As its name state your toggle will now look like a switch. As for the radio, the height will be half the one of the classic version.

It will also act like a switch : slide right to activate it, and slide left to de-activate. It's probably a good place to look first if you want to write your own overrides.

![stoggle]({{site.baseurl}}/img/overrides/switchToggle.png)

To use it you need to import those classes to your launcher :
{% highlight  java%} 
import net.mgsx.ppp.widget.core.Toggle;
import net.mgsx.ppp.widget.custom.SwitchToggle;
{% endhighlight %}

If you want to override all components in your gui you can do it by adding this line of code to your launcher :

{% highlight  java%} 
config.typeOverrides.put(Toggle.class, SwitchToggle.class);
{% endhighlight %}

If you want to override only the toggle labelled 'stgl', this line :

{% highlight  java%} 
config.objectOverrides.put("stgl", SwitchToggle.class);
{% endhighlight %}