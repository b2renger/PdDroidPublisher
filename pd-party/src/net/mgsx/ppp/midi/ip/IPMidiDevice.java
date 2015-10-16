package net.mgsx.ppp.midi.ip;

import java.util.ArrayList;
import java.util.List;

import net.mgsx.ppp.midi.MidiDevice;
import net.mgsx.ppp.midi.MidiInput;
import net.mgsx.ppp.midi.MidiOutput;
import android.content.Context;

public class IPMidiDevice implements MidiDevice
{
	List<MidiInput> inputs = new ArrayList<MidiInput>();
	List<MidiOutput> outputs = new ArrayList<MidiOutput>();
	
	@Override
	public void init(Context ctx) 
	{
	}

	@Override
	public List<MidiInput> getInputs() 
	{
		return inputs;
	}

	@Override
	public List<MidiOutput> getOutputs() 
	{
		return outputs;
	}

	@Override
	public String getName() 
	{
		return "UDP";
	}
	
}
