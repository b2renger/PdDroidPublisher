package cx.mccormick.pddroidparty.midi.nmj;

import cx.mccormick.pddroidparty.midi.MidiInput;
import cx.mccormick.pddroidparty.midi.MidiListener;
import de.humatic.nmj.NetworkMidiInput;
import de.humatic.nmj.NetworkMidiListener;

public class NMJMidiInput implements MidiInput, NetworkMidiListener
{

	private NMJMidiDevice device;
	int port;
	
	private NetworkMidiInput midiIn = null;
	private MidiListener listener;
	String name;
	
	public NMJMidiInput(NMJMidiDevice device, int port, String name) 
	{
		this.device = device;
		this.port = port;
		this.name = name;
	}

	@Override
	public void open(MidiListener listener) 
	{
		this.listener = listener;
		try {
			midiIn = device.nmjs.openInput(port, device);
			midiIn.addMidiListener(this);
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	@Override
	public void close() 
	{
		if(midiIn != null)
		{
			midiIn.removeMidiListener(this);
			this.listener = null;
			midiIn.close(device);
			midiIn = null;
		}
	}

	@Override
	public void midiReceived(int channel, int ssrc, byte[] data, long timestamp) 
	{
		listener.onMidiMessage(data);
	}

	@Override
	public String getName() 
	{
		return name;
	}

}
