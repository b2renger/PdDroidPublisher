package cx.mccormick.pddroidparty.midi.nmj;

import cx.mccormick.pddroidparty.midi.MidiOutput;
import de.humatic.nmj.NetworkMidiOutput;

public class NMJMidiOutput implements MidiOutput
{
	private NMJMidiDevice device;
	int port;
	
	private NetworkMidiOutput midiOut = null;
	
	
	String name;
	
	public NMJMidiOutput(NMJMidiDevice device, int port, String name) 
	{
		this.device = device;
		this.port = port;
		this.name = name;
	}


	@Override
	public void open() 
	{
		try {
			midiOut = device.nmjs.openOutput(port, device);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	@Override
	public void close() 
	{
		if(midiOut != null)
		{
			midiOut.close(device);
		}
	}

	@Override
	public void send(byte[] message) 
	{
		try {
			midiOut.sendMidi(message);
		} catch (Exception e) {
			throw new Error(e);
		}
	}


	@Override
	public String getName() {
		return name;
	}

}
