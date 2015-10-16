package net.mgsx.ppp.midi;

public interface MidiInput extends MidiPort
{
	public void open(MidiListener listener);
	public void close();
}
