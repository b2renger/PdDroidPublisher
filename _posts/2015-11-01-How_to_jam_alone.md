---
layout: post
title:  "How To Jam alone"
tags: user-guide
order: 020
---

This video may be self-explanatory, but you can also read on ...

<div>
        <center>
         <iframe width="420" height="315" src="https://www.youtube.com/embed/9U19fb3_Koc" frameborder="0" allowfullscreen></iframe>
        </center>
</div>

## Jamming alone in local mode

It is actually pretty trivial, but if you are alone, you still use our system to share the clock beetween several instance of one app, or several apps.

You will have to choose one app as master and all other apps you will launch will be slaves.

###Slave configuration

You need to create an **input** configuration, with a port to listen on.

So you just have to tap the "+" icon next to the **MIDI INPUT** line: ![add midi]({{site.baseurl}}/img/addmidi-icon.png)

When you do that another dialog appears :

![add midi input]({{site.baseurl}}/img/MidiAddInput.png)

For the slaves you just have to specify a different port for each application : '21269' for the first one, '21270' for the next etc. 

###Master configuration

The one and master app has to reference each one of the other app you want to use in synch, by referencing the port to send to and the 'local' ip address.

For each app you want to forward beats to, you need to create and activitate a new output by tapping the "+" icon next to the **MIDI OUTPUT** label : ![add midi]({{site.baseurl}}/img/addmidi-icon.png)

This will open this dialog :
![add midi output]({{site.baseurl}}/img/MidiAddOutput.png)

You have to reference the *local* ip address ( that is to says leave the default '127.0.0.1' value for the ip address), and change the port to match the port of one of your slave application as you have configured it. Then tap on **Create**.

A new entry will be created in the list of available outputs, you just to check it :
![add midi output]({{site.baseurl}}/img/MidiOutputSelected.png)

You have to follow the same procedure as described before for each application. 

In a local jam you don't have to worry about audio latency, it's probably the simplest way to get started but it's more fun with at least one buddy.

Have Fun !