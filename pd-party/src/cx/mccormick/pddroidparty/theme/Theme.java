package cx.mccormick.pddroidparty.theme;

import java.io.Serializable;

import cx.mccormick.pddroidparty.theme.mono.MonochromeTheme;
import cx.mccormick.pddroidparty.theme.pd.PdTheme;
import cx.mccormick.pddroidparty.widget.Widget;

/**
 * GUI Theme.
 * Known theme : {@link PdTheme} (default), {@link MonochromeTheme} 
 */
public interface Theme extends Serializable
{
	public static final Theme pdTheme = new PdTheme();
	
	public int getForegroundColor(Widget widget);
	public int getBackgroundColor(Widget widget);
	public int getLabelColor(Widget widget);
	public int getBackgroundColor();
	
}
