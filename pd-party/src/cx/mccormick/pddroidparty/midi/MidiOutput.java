package cx.mccormick.pddroidparty.midi;

public interface MidiOutput extends MidiPort
{
	public void open();
	public void close();
	public void send(byte [] message);
}
