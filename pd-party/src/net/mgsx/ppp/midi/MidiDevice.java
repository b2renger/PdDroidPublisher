package net.mgsx.ppp.midi;

import java.util.List;

import android.content.Context;

public interface MidiDevice 
{
	public void init(Context ctx);
	public List<MidiInput> getInputs();
	public List<MidiOutput> getOutputs();
	public String getName();
	
}
