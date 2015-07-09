package cx.mccormick.pddroidparty.midi;

public interface MidiInput extends MidiPort
{
	public void open(MidiListener listener);
	public void close();
}
