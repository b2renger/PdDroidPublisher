package cx.mccormick.pddroidparty.pd;

/**
 * Pure Data Clock Abstraction encapsulation. 
 */
public class PdClock 
{

	/**
	 * Receiver name used in clock abstraction to enable/disable audio
	 * metronome.
	 */
	public static final String AudioClockEnableReceiver = "clock.audio";
	
	/**
	 * Receiver name used in clock abstraction to delay sequence counter.
	 * This is used to adjust sync between multiple devices.
	 */
	public static final String ClockDelayReceiver = "midiclock.delay";
	
	/**
	 * Receiver name used in clock abstraction to get MIDI input message.
	 * Receiver is used in replacement of "midiin" object to allow block message
	 * (list message containing the whole MIDI message).
	 * This avoid complex Pd packing and risks of interleaved messages.
	 */
	public final static String ClockMidiInputReceiver = "midiin";

}
