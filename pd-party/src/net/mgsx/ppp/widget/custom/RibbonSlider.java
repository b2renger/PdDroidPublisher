package net.mgsx.ppp.widget.custom;

import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.core.Slider;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Display slider with filled rectangle (from minimum to current value) instead of a cursor. 
 */
public class RibbonSlider extends Slider 
{
	public RibbonSlider(PdDroidPatchView app, String[] atomline, boolean horizontal) {
		super(app, atomline, horizontal);
	}
	
	public void draw(Canvas canvas) 
	{
		paint.setColor(bgcolor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(dRect, paint);

		paint.setColor(fgcolor);
		paint.setStyle(Paint.Style.FILL);
		
		if(horizontal)
		{
			float offset = dRect.width() * (val - min) / (max - min);
			canvas.drawRect(dRect.left, dRect.top, dRect.left + offset, dRect.bottom, paint);
		}
		else
		{
			float offset = dRect.height() * (val - min) / (max - min);
			canvas.drawRect(dRect.left, dRect.bottom - offset, dRect.right, dRect.bottom, paint);
		}

		paint.setColor(fgcolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		canvas.drawRect(dRect, paint);
	}
	
}
