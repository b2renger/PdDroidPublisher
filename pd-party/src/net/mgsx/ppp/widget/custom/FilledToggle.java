package net.mgsx.ppp.widget.custom;

import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.core.Toggle;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class FilledToggle extends Toggle
{
	protected float cursorX, px;
	protected int pid = -1;

	public FilledToggle(PdDroidPatchView app, String[] atomline) {
		super(app, atomline);
		dRect.bottom = dRect.top + dRect.width();
		
		
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		paint.setColor(bgcolor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRoundRect(dRect,5,5,paint);

		
		paint.setStrokeWidth(2);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fgcolor);
		canvas.drawRoundRect(dRect,5,5,paint);
		
		
		if (val != 0) {
			
				paint.setColor(fgcolor);
				paint.setStyle(Paint.Style.FILL);
				canvas.drawRoundRect(dRect,5,5,paint);
		}
		
		paint.setStyle(Paint.Style.FILL);
		drawLabel(canvas);
	}
	
	

}
