package net.mgsx.ppp.widget.custom;

import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.core.Bang;
import android.graphics.Canvas;
import android.graphics.Paint;

public class SimpleBang extends Bang
{

	public SimpleBang(PdDroidPatchView app, String[] atomline) 
	{
		super(app, atomline);
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(bgcolor);
		canvas.drawCircle(dRect.centerX(), dRect.centerY(), Math.min(dRect.width(), dRect.height()) / 2, paint);
		
		paint.setColor(fgcolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(4);
		canvas.drawCircle(dRect.centerX(), dRect.centerY(), Math.min(dRect.width(), dRect.height()) / 2, paint);

		if (bang) 
		{
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(fgcolor);
			canvas.drawCircle(dRect.centerX(), dRect.centerY(), Math.min(dRect.width(), dRect.height()) / 3, paint);
		}
		drawLabel(canvas);
	}

}
