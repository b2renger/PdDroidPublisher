package net.mgsx.ppp.theme;

import java.io.Serializable;

import net.mgsx.ppp.theme.mono.MonochromeTheme;
import net.mgsx.ppp.theme.pd.PdTheme;
import net.mgsx.ppp.widget.Widget;

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
