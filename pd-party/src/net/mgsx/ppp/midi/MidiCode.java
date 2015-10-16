package net.mgsx.ppp.midi;

/**
 * http://www.cotse.com/dlf/man/midi/realtime.htm 
 */
public class MidiCode {

	public static final int MIDI_REALTIME_MASK = 0xF8;
	public static final int MIDI_REALTIME_CLOCK_START = 0xFA;
	public static final int MIDI_REALTIME_CLOCK_STOP = 0xFC;
	public static final int MIDI_REALTIME_CLOCK_TICK = 0xF8;
	public static final int MIDI_REALTIME_CLOCK_RESUME = 0xFB;
	public static final int SONG_POSITION_POINTER = 0xF2;
	
	public static boolean isRealtime(int message) 
	{
		return (message & MIDI_REALTIME_MASK) == MIDI_REALTIME_MASK;
	}

}
