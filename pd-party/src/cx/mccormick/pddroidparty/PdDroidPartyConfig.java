package cx.mccormick.pddroidparty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cx.mccormick.pddroidparty.theme.Theme;
import cx.mccormick.pddroidparty.widget.Widget;
import cx.mccormick.pddroidparty.widget.WidgetFactory;

@SuppressWarnings("serial")
public class PdDroidPartyConfig implements Serializable
{
	public static final int midiClockMinBPM = 1; // hard limit
	public static final int midiClockMaxBPM = 360; // hard limit
	public static final int midiClockDefaultBPM = 100; // nominal BPM

	public Map<String, String> patches = new LinkedHashMap<String, String>();
	public List<String> presetsPaths = new ArrayList<String>();
	public int audioSampleRate = 44100;
	public int audioInputs = 1;
	public int audioOutputs = 2;
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
