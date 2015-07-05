package cx.mccormick.pddroidparty;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PdDroidPartyConfig implements Serializable
{
	public int audioSampleRate = 44100;
	public int audioInputs = 1;
	public int audioOutputs = 2;
	public int midiClockMinBPM = 60;
	public int midiClockMaxBPM = 180;
	public int midiClockDefaultBPM = 100;
	public boolean guiKeepAspectRatio = true;
}
