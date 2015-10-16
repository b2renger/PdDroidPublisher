package net.mgsx.ppp.theme.pd;

import net.mgsx.ppp.theme.Theme;
import net.mgsx.ppp.widget.Widget;

/**
 * Default Pure Data Theme.
 * 
 * This theme mimics Pure Data GUI.
 */
@SuppressWarnings("serial")
public class PdTheme implements Theme
{

	@Override
	public int getForegroundColor(Widget widget) {
		return widget.fgcolor;
	}

	@Override
	public int getBackgroundColor(Widget widget) {
		return widget.bgcolor;
	}

	@Override
	public int getLabelColor(Widget widget) {
		return widget.labelcolor;
	}

	@Override
	public int getBackgroundColor() {
		return 0xFFFFFFFF;
	}

}
