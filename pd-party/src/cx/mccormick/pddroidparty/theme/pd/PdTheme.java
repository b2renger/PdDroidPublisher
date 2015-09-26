package cx.mccormick.pddroidparty.theme.pd;

import cx.mccormick.pddroidparty.theme.Theme;
import cx.mccormick.pddroidparty.widget.Widget;

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
