---
layout: post
title:  "List of available overrides"
categories: How to, pd developer
published : true
---

The system of override is a way to customize a gui component from pd and transform it into something else. You can just override the **graphics**, or you can re-code the **comportement**. We have built a few overrides as 'out of the box' features to customise your apps.

You can use those features by configuring them in the launcher, you can check the [How to configure your launcher]({{site.baseurl}}/_post/2015-11-01-How_to_configure_your_launcher#factory-components) post.


# FilledToggle 
The FilledToggle is a **graphic** override.
It replaces the basic toggle from pd, to a rounded rectangle, no more cross when active, instead the rectangle is filled.

![FilledToggle]({{site.baseurl}}/img/overrides/filledToggle.png)



{% highlight  java%} 

{% endhighlight %}


# PopupTaplist
The PopupTaplist is a **comportement** override.

The comportement aspect, is advanced, as it uses android layout to display a browsable list of elements, you just have to click to select the new value.


![Popup]({{site.baseurl}}/img/overrides/popup.png)


# RibbonSlider
The RibbonSlider is a **graphic** override. The slider now has nice rounded angles, and the level selected is clearly represented as filled rectangle, instead of just a simple line.


![vslider]({{site.baseurl}}/img/overrides/vslider.png)
![hslider]({{site.baseurl}}/img/overrides/hslider.png)


# SimpleBang
The SimpleBang is a **graphic** override. No more rectangle framing the circle, it's now nice and round with a thick edge.

![bang]({{site.baseurl}}/img/overrides/bang.png)

# SimpleRadio
The SimpleRadio is a **graphic** override. The index of the radio is now marked with a filled rectangle, and buttons are separated. The displayed widget is half the height of its pd implementation, this way if you put a label, with the top part of your font aligned to the top line of the radio, the label is just above it.

![vradio]({{site.baseurl}}/img/overrides/vradio.png)
![hradio]({{site.baseurl}}/img/overrides/hradio.png)


# SwitchToggle
The SwitchToglle is a **graphic** and a **comportement** override. As its name state your toggle will now look like a switch. As for the radio, the height will be half the one of the classic version.

It will also act like a switch : slide right to activate it, and slide left to de-activate. It's probably a good place to look first if you want to write your own overrides.

![stoggle]({{site.baseurl}}/img/overrides/switchToggle.png)