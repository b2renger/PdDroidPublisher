package cx.mccormick.pddroidparty.wobbler;

import android.graphics.Canvas;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.core.Radio;

public class WobbleSelector extends Radio
{
	WobbleSelectorGroup group;
	
	public WobbleSelector(PdDroidPatchView app, String[] atomline) 
	{
		super(app, atomline, true);
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
