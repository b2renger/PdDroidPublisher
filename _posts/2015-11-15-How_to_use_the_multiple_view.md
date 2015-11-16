---
layout: post
title:  "How to use multiple views"
tags: advanced-design
order : 010
---

First in the launcher you have to create a config variable :

{% highlight java %} 
PdDroidPartyConfig config = new PdDroidPartyConfig();
{% endhighlight %}

right after the line :
{% highlight java %} 
super.onCreate(savedInstanceState);
{% endhighlight %}

then we will use this variable to call methods like this *config.myCustomisationMethod*.

To use the multi-view support we strongly advise to follow the **pd best practices** mentionned in the **Pders Guide**.

The best way to benefit from the multi-view feature, is to break your patch into several abstractions. You will probably want to seperate the gui from the audio core, and probably break the gui into pannels as hinted in the post mentionned above.

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