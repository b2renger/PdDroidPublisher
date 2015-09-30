package cx.mccormick.pddroidparty.wobbler;

import cx.mccormick.pddroidparty.widget.Widget;
import cx.mccormick.pddroidparty.widget.WidgetFactory;

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
