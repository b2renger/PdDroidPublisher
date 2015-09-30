package cx.mccormick.pddroidparty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cx.mccormick.pddroidparty.widget.Widget;
import cx.mccormick.pddroidparty.widget.WidgetFactory;
import cx.mccormick.pddroidparty.theme.Theme;

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
	
	/** global theme : default is PdTheme 
	 * @see Theme implementations
	 */
	public Theme theme = Theme.pdTheme;
	
	/** time in milliseconds between 2 array refresh 
	 * default is 1 seconds. Low values may impact performances.
	 */
	public long arrayRefreshTimeMS = 1000;
	
	public Map<Class<? extends Widget>, Class<? extends Widget>> typeOverrides = new HashMap<Class<? extends Widget>, Class<? extends Widget>>();
	public Map<String, Class<? extends Widget>> objectOverrides = new HashMap<String, Class<? extends Widget>>();
	public Class<? extends WidgetFactory> factory = WidgetFactory.class;
}
