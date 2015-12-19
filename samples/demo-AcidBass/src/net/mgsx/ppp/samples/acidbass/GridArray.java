package net.mgsx.ppp.samples.acidbass;

import net.mgsx.ppp.widget.core.Subpatch;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GridArray extends Subpatch
{

	public GridArray(Subpatch subpatch) {
		super(subpatch);
	}
	
	@Override
	protected void drawBackground(Canvas canvas) 
	{
		//paint.setStyle(Paint.Style.FILL);
		//paint.setColor(bgcolor);	
		//canvas.drawRect(x, y, x + zoneWidth, y + zoneHeight, paint);
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(bgcolor);
		paint.setStrokeWidth(2f);
		
		for(int i=0 ; i < 16 ; i++)
		{
			float lx = x + zoneWidth * (float)i / (float)16;
			canvas.drawLine(lx, y, lx, y + zoneHeight, paint);
		}
		for(int i=0 ; i<12 ; i++)
		{
			float ly = y + zoneHeight * (float)i / (float)12;
			canvas.drawLine(x, ly, x + zoneWidth, ly, paint);
		}
		
	}

}
