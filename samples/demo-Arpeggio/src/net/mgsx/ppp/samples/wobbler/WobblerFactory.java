package net.mgsx.ppp.samples.wobbler;

import net.mgsx.ppp.widget.Widget;
import net.mgsx.ppp.widget.WidgetFactory;

public class WobblerFactory extends WidgetFactory
{
	WobbleSelectorGroup wobbleGroup = new WobbleSelectorGroup();
	
	@Override
	public void onNewWidget(Widget widget)
	{
		if(widget instanceof WobbleSelector)
		{
			((WobbleSelector)widget).group = wobbleGroup;
		}
	}
}
