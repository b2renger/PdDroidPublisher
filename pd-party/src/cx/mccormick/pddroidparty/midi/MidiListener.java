package cx.mccormick.pddroidparty.midi;

public interface MidiListener 
{
	public void onMidiMessage(byte [] message);
}
