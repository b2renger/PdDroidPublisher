package net.mgsx.ppp.midi;

public interface MidiListener 
{
	public void onMidiMessage(byte [] message);
	public void onStatusMessage(String message);
}
