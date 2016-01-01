package net.mgsx.ppp.samples.wobble;

import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.core.Radio;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class TrackLed extends Radio
{

	public TrackLed(PdDroidPatchView app, String[] atomline) {
		super(app, atomline, true);
	}
	
	@Override
	public boolean touchdown(int pid, float x, float y) {
		// Cancel interactions
		return false;
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		int index = (int)val;
		paint.setColor(fgcolor);

		for(int i=0 ; i<count ; i++)
		{
			paint.setStyle(Paint.Style.STROKE);
			RectF cellRect = new RectF();
			cellRect.left = dRect.left + size * (i + 0.25f);
			cellRect.top = dRect.top + size * 0.25f;
			cellRect.right = cellRect.left + size * 0.5f;
			cellRect.bottom = cellRect.top + size * 0.25f;
			canvas.drawRect(cellRect,paint);
			
			if(i == index)
			{
				paint.setStyle(Paint.Style.FILL);
				canvas.drawRect(cellRect,paint);
			}
		}
		
	}

}
