---
layout: post
title:  "How to save presets"
tags: dev-guide
order: 050
---

For the saving system we choose to developp our own system, mainly because sssad was very slow when saving arrays with a lot of samples (saving 1s of audio was taking too much time for instance). The system is built to be as simple as possible, and allow to deal with several preset banks - that is to say that you can save your sequencer patterns and synth presets separatly

On the pd side you can check the help patches in **/puredata** release folder. It relies on the same mechanism as sssad, and it probably cannot do eveything sssad can, but it has pretty much the same limitation and it's best to know about it ... sssad and persist rely on the fact that a single instance of an abstraction will save a value that is received and sent via the messages given as arguments : that means that if you don't send anything at some point ... well, nothing will be saved, and nothing will be restored on load. So the lesson is : ** if you want to save a value be sure to initiate it with a default value on startup.** 

With something like this for instance : ![pd init]({{site.baseurl}}/img/pd/init.png)
You could also probably create an empty preset, and load it on startup.

That being said there is two main abstractions that you will be interested in to actually save values :

* **[persist-map]** : it takes 3 arguments the first one is the name of the preset bank, the second one is the send value of the gui object you want to save, the third one is the receive value of the object you want to load. If you create an object [persist-map synth decay.s decay.r] : you will save your decay value in a the preset bank called 'synth'.

* **[persist-table]** : it takes 2 arguments, the first one is the name of the preset bank, the second one the name of the table object that you want to save/load (regardless of its size - even if it is not made to save to much audio, play nice !)

Now let take a pragmatic example from the acid_bass sample app. As stated before we want to save our parameters in two separate banks : one for sequencer patterns and one fo synth presets. So the first thing to do is to create two objects **[loadsave synth]** and **[loadsave pattern]**, those act as 'routers' for messages.

In the GUI we have three pannels : two of them are for the sequencer, and only one for the synth. We chose to use a bang to activate the display of dialog pannels one for the save action and one for the load action. That makes for bang that send those messages 'save_pattern', 'load_pattern', 'save_synth', 'load_synth'.

Now we can interface this with persist :
![loadsave interface]({{site.baseurl}}/img/pd/loadsave-gui-binding.png)

1- we receive a message from a bang, and we create a new message sent with the tag 'pattern-load' (two first words), and it sends a path 'presets_pattern' and a file format extension 'cbs'
2- we receive this message and put it in the input of a **[persist-load]** object (or **[persist-save]** depending on the context) with the name of the preset bank as argument.
3- repeat 1 and 2 for other actions, and other banks !

**Note :** the file format 'cbs' will have to be added manually to save on destkop, wether it will be automatically added on android. That is mainly when you want to see a preset saved on a desktop computer in an android application, which will probably be the case of some of factory presets.

For the eclipse part, you just have to reference the path to the preset folder in your configuration :

{% highlight java %} 
config.presetsPaths.add("Acid_Bass/presets_pattern");
config.presetsPaths.add("Acid_Bass/presets_synth");
{% endhighlight %}

Congratulations you have finished your training and can now save presets !