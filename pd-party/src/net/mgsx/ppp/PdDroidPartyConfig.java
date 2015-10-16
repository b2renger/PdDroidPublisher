package net.mgsx.ppp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.mgsx.ppp.theme.Theme;
import net.mgsx.ppp.widget.Widget;
import net.mgsx.ppp.widget.WidgetFactory;

/**
 * Pure Data Activity Configuration. 
 */
@SuppressWarnings("serial")
public class PdDroidPartyConfig implements Serializable
{
	/**
	 * In MultiPatch mode, defines patches to be loaded but not to be displayed.
	 * Patches are opened is the same order than this list.
	 * Item is the patch path relative to asset directory.
	 */
	public List<String> corePatches = new ArrayList<String>();
	
	/**
	 * In MultiPatch mode, defines patches to be displayed.
	 * Patches are display in Tab view, order of tabs is kept according to
	 * how you put it in the map.
	 * The key is the tab label displayed and the value is the patch path
	 * relative to asset directory.
	 */
	public Map<String, String> guiPatches = new LinkedHashMap<String, String>();
	
	/**
	 * Path to factory-presets directory relative to assets folder.
	 * These directories will be copied to application external storage at
	 * when application external storage not exists yet.
	 * (typically at first application launch).
	 */
	public List<String> presetsPaths = new ArrayList<String>();
	
	/**
	 * Audio sample rate.
	 */
	public int audioSampleRate = 44100;
	
	/**
	 * Number of audio inputs channels to open.
	 */
	public int audioInputs = 1;
	
	/**
	 * Number of audio outputs channels to open.
	 */
	public int audioOutputs = 2;
	
	/**
	 * Control layout behavior : 
	 * false means that GUI will be stretched to fit the screen.
	 * true means that the GUI won't be stretched but centered instead.
	 */
	public boolean guiKeepAspectRatio = true;
	
	/** global theme : default is PdTheme 
	 * @see Theme
	 */
	public Theme theme = Theme.pdTheme;
	
	/** time in milliseconds between 2 array refresh 
	 * default is 1 seconds. Low values may impact performances.
	 */
	public long arrayRefreshTimeMS = 1000;
	
	/**
	 * Override registry for typed widgets.
	 * Use to set custom widget implementation to a Pd object type.
	 */
	public Map<Class<? extends Widget>, Class<? extends Widget>> typeOverrides = new HashMap<Class<? extends Widget>, Class<? extends Widget>>();
	
	/**
	 * Override registry for named widgets.
	 * Use to set custom widget implementation to a named Pd object.
	 * The name is based on object label.
	 */
	public Map<String, Class<? extends Widget>> objectOverrides = new HashMap<String, Class<? extends Widget>>();
	
	/**
	 * Set this class with your own WidgetFactory.
	 */
	public Class<? extends WidgetFactory> factory = WidgetFactory.class;
}
