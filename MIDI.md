
# MIDI clock

Midi clock implementation is based on http://www.blitter.com/~russtopia/MIDI/~jglatt/tech/midispec/seq.htm

MIDI SPP (Song position pointer) implementation is based on http://www.blitter.com/~russtopia/MIDI/~jglatt/tech/midispec/ssp.htm

SPP is used to jump in sequence, not to resync. But in MIDI over the network (UDP) case, ticks may not be received.
To avoid clock drift between devices because of tick loss, an SPP message is sent every 96 ticks (4 bars) to resync all devices.

# MIDI and Pure Data

MIDI is not well implemented in pure data, limitations exists on some OS :

* *midirealtimein* seems to be implemented only for Windows (but work on Linux)
* *midiout* seems to be implemented only on GNU/Linux but it's not always working.
* *midiin* only accept single byte messages making MIDI message routing difficult.

Current implementation use midirealtimein for true realtime MIDI messages. It works
on android because our implementation use corresponding libPD methods.
With Pd for Desktop, this cannot directly work.

To bypass *midiin* limitation, our implementation use a receiver named "midiin".
So list message are easily routed inside clock.pd. 

To connect Pure Data (Desktop or Android) to a DAW supporting MIDI clock or to
connect Pure Data Desktop and Android, a network bridge is required (like qmidinet for linux).
