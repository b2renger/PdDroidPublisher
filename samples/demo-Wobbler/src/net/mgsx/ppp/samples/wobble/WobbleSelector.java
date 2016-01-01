package net.mgsx.ppp.samples.wobble;

import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.core.Radio;
import android.graphics.Canvas;

public class WobbleSelector extends Radio
{
	WobbleSelectorGroup group;
	
	public WobbleSelector(PdDroidPatchView app, String[] atomline, boolean horizontal) 
	{
		super(app, atomline, horizontal);
	}

	@Override
	public void draw(Canvas canvas) 
	{
		drawBackground(canvas);
		if(group.current == this)
		{
			drawForeground(canvas);
		}
	}
	
	@Override
	public void setval(float v) {
		super.setval(v);
		group.current = this;
	}
}
