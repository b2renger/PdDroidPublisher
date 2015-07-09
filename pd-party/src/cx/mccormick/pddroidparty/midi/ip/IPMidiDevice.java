package cx.mccormick.pddroidparty.midi.ip;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import cx.mccormick.pddroidparty.midi.MidiDevice;
import cx.mccormick.pddroidparty.midi.MidiInput;
import cx.mccormick.pddroidparty.midi.MidiOutput;

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
