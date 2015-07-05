package cx.mccormick.pddroidparty.midi;

public interface UsbMidiHandler 
{
	/**
	 * @param message
	 */
	void onStatusMessage(String message);
}
