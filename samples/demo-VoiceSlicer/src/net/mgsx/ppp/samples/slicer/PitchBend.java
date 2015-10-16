package net.mgsx.ppp.samples.slicer;

import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.core.Slider;
import android.graphics.Canvas;
import android.graphics.Paint;

public class PitchBend extends Slider
{

	public PitchBend(PdDroidPatchView app, String[] atomline, boolean horizontal) {
		super(app, atomline, horizontal);
	}

	@Override
	public boolean touchup(int pid, float x, float y) {
		if(super.touchup(pid, x, y))
		{
			setval(0);
			sendFloat(0);
			return true;
		}
		return false;
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		paint.setColor(bgcolor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(dRect,paint);

		paint.setColor(fgcolor);
		paint.setStrokeWidth(1);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(dRect,paint);
		
		paint.setColor(fgcolor);
		paint.setStyle(Paint.Style.FILL);
		
		if(val > 0)
		{
			float v = val / max;
			if(horizontal)
				canvas.drawRect(dRect.left + dRect.width()/2, dRect.top, dRect.left + (1 + v) * dRect.width()/2, dRect.bottom,paint);
			else
				canvas.drawRect(dRect.left, dRect.top + (1 - v) * dRect.height()/2, dRect.right, dRect.top + dRect.height()/2, paint);
		}
		else if(val < 0)
		{
			float v = val / min;
			if(horizontal)
				canvas.drawRect(dRect.left + (1 - v) * dRect.width()/2, dRect.top, dRect.left + dRect.width()/2, dRect.bottom,paint);
			else
				canvas.drawRect(dRect.left, dRect.top + dRect.height()/2, dRect.right, dRect.top + (1 + v) * dRect.height()/2, paint);
		}
		else
		{
			if(horizontal)
				canvas.drawRect(dRect.left + dRect.width()/2 - 1, dRect.top, dRect.left + dRect.width()/2 + 1, dRect.bottom,paint);
			else
				canvas.drawRect(dRect.left, dRect.top + dRect.height()/2-1, dRect.right, dRect.top + dRect.height()/2 + 1, paint);
		}
		
		drawLabel(canvas);
	}
}
