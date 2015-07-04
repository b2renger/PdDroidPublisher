nmj 0.894
copyright np, humatic, Berlin 2010-15

Released under a Creative Commons non-commercial, share-alike, no derivative works license.
(http://creativecommons.org/licenses/by-nc-nd/3.0/de/deed.en_GB). 
You are free to use nmj binaries in non-commercial context, given you add credits and reproduce the library's copyright and licensing notes. Should you wish to use nmj in commercial projects please get in touch with us.

=====================================================================================================

nmj has originally been created in context of humatic's artwork to run MIDI from Android devices. Part of the code dates back to a Wifi MIDI based robot control system created for the Dead Chickens' (Berlin machinery art and performance ensemble) appearance at Europe's cultural capital Lille in 2004.
In today's form the library provides MIDI functionality to TouchDAW, one of the few MIDI apps available on Google Play. As such it has been used by significant numbers of people over the past couple of years and lots of user feedback has been soaked up. As a result nmj now is not really just "network midi" anymore, but there's also Bluetooth and wired USB connectivity.  

Along with version 0.89 we have released a native Windows & OS X MIDI driver that supports all of nmj's connection methods into public beta: www.humatic.de/htools/mnet . This will from now on take the part of nmj's desktop java implementation, which has been deprecated and will not see any more updates.

======================================================================================================

nmj can provide a theoretically unlimited number of channels configureable in application code. Apart from USB_HOST mode all channels are purely virtual and only defined by a number of settings persisted in (Shared)Preferences. The main programming interface for this is in the NMJConfig class. You get instances of MIDI in- and outputs from the NetworkMidiSystem class, which is statically initialized on application startup.

Both desktop and Android versions include a small configuration UI accessible via NMJConfig.showConfigDialog(...) or - on Android - as a seperate Activity. Double-clicking the desktop version's jar will also launch the configuration interface. The dialogs are however not designed to win the annual "nice interface" award and should primarily be seen as utilities for developers to quickly check out things during programming.

The (deprecated) desktop version implements the javax.sound.midi Service Provider Interface (SPI) and will instantly supply additional javasound “MIDI devices” to java applications when the library is placed on the classpath. Besides that both Android and desktop versions share a small public API (see javadocs).

Please be aware that the library is implemented in seperate jar files for Android and J2SE. Find them in the resp. subfolders. There's also seperate javadoc editions now and those hopefully contain sufficiant information to get you going. 

A sample Android app is included. Some brief and abstract samplecode that can be used on both platforms is in the samples folder. Overall this will still be underdocumented, but resources are limited. 

======================================================================================================

If you have questions regarding nmj use, feel free to contact us at nmj@humatic.de

======================================================================================================

Change log:

0.894:
Contains first adaptions to Android M's changed permission system: 
Connectivity lookups will now take permissions into account and if a permission is not given then the corresponding connectivity will be marked as unavailable. A new connectivity field (CONNECTIVITY_MULTICAST, 0x80) has been added, because
multicast inputs depend on a user unsetable and by default not granted permission (CHANGE_WIFI_MULTICAST_STATE) under Android M.
Added boolean parameter to NMJConfig.edit(..) to optionally wipe out all existing preferences, because Android M does not seem to properly clean up all preference files on app uninstalls. Old version is deprecated and will be removed in 0.9.
Unsetting global USB_ATTACHMENT_LISTEN flag disabled BroadcastReceivers but did not skip all Usb device lookups, fixed.
Internal Qualcomm Usb modems hardwired to the external Usb bus are now ignored
Fixes bug in merge part of direct input to output linking functionality (NetworkMidiInput.setLink(..)) that could trigger stack overflows
Updated and cleaned the samplecode a bit

0.893:
Will now send RTPA_CONNECTION_REFUSED events when the remote side refuses to accept invitations. This may for example occur when trying to open a remote rtpMIDI or OS X session that is set to only accept connections from "computers in my directory".
Fixes use of "Ssrc" parameter in midiReceived callback with local RTP sessions which always sent their own source identifier instead of those of connected clients.
Control panel would open outputs on the main thread, changed. 

0.892:
Fixes a problem with some class-compliant Usb interfaces whose descriptors have a completely empty interface defined where the MIDI interface should be.
