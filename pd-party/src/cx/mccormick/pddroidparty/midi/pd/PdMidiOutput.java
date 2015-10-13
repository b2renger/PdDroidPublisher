package cx.mccormick.pddroidparty.midi.pd;

import org.puredata.core.PdBase;

import cx.mccormick.pddroidparty.midi.MidiCode;
import cx.mccormick.pddroidparty.midi.MidiOutput;

public class PdMidiOutput implements MidiOutput
{
	/**
	 * Receiver name used in clock abstraction to get MIDI input message.
	 * Receiver is used in replacement of "midiin" object to allow block message
	 * (list message containing the whole MIDI message).
	 * This avoid complex Pd packing and risks of interleaved messages.
	 */
	private final static String PD_MIDIIN_RECEIVER = "midiin";
	
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
		send(port, message);
	}

	@Override
	public String getName() {
		return "Midi " + String.valueOf(port + 1);
	}

	public static void send(int port, byte[] message) 
	{
		if(message.length == 1)
		{
			int first = message[0] & 0xFF;
			if(MidiCode.isRealtime(first))
			{
				PdBase.sendSysRealTime(port, first);
			}
			else
			{
				System.err.println("Unsupported non realtime one byte message : " + first);
			}
		}
		else if(message.length == 3)
		{
			PdBase.sendList(PD_MIDIIN_RECEIVER,
				Integer.valueOf(message[0] & 0xFF), 
				Integer.valueOf(message[1] & 0xFF),
				Integer.valueOf(message[2] & 0xFF));
		}
		else
		{
			System.err.println("Unsupported message length : " + message.length);
		}
	}

}
