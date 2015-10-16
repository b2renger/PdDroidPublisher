package net.mgsx.ppp.view;

import net.mgsx.ppp.midi.MidiDevice;
import net.mgsx.ppp.midi.MidiPort;

public class MidiPortAdapter 
{
	protected MidiDevice midiDevice;
	protected MidiPort midiPort;

	public MidiPortAdapter() {
	}
	
	public MidiPortAdapter(MidiDevice midiDevice, MidiPort midiPort) {
		super();
		this.midiDevice = midiDevice;
		this.midiPort = midiPort;
	}

	@Override
	public String toString() 
	{
		if(midiDevice == null || midiPort == null)
		{
			return "<none>";
		}
		return midiDevice.getName() + " " + midiPort.getName();
	}
	
}
