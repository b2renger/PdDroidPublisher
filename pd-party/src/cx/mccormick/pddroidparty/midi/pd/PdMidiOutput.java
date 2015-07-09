package cx.mccormick.pddroidparty.midi.pd;

import org.puredata.core.PdBase;

import cx.mccormick.pddroidparty.midi.MidiOutput;

public class PdMidiOutput implements MidiOutput
{
	private int port;
	
	public PdMidiOutput(int port) {
		super();
		this.port = port;
	}

	@Override
	public void open() {
		// nothing to do
	}

	@Override
	public void close() {
		// nothing to do
	}

	@Override
	public void send(byte[] message) 
	{
		if(message.length > 0)
		{
			int first = message[0] & 0xFF;
			if((first & 0xF8) == 0xF8)
			{
				PdBase.sendSysRealTime(port, first);
			}
		}
	}

	@Override
	public String getName() {
		return "Midi " + String.valueOf(port + 1);
	}

}
