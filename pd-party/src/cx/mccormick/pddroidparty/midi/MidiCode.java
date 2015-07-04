package cx.mccormick.pddroidparty.midi;

/**
 * http://www.cotse.com/dlf/man/midi/realtime.htm 
 */
public class MidiCode {

	public final static int MIDI_REALTIME_CLOCK_START = 0xFA;
	public final static int MIDI_REALTIME_CLOCK_STOP = 0xFC;
	public final static int MIDI_REALTIME_CLOCK_TICK = 0xF8;
	protected static final int MIDI_REALTIME_CLOCK_RESUME = 0xFB;

}
