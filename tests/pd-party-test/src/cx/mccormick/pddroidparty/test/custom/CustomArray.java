package cx.mccormick.pddroidparty.test.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import cx.mccormick.pddroidparty.widget.core.Subpatch;

public class CustomArray extends Subpatch
{

	public CustomArray(Subpatch subpatch) {
		super(subpatch);
	}
	
	@Override
	protected void drawBackground(Canvas canvas) 
	{
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(bgcolor);	
		canvas.drawRect(x, y, x + zoneWidth, y + zoneHeight, paint);
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(1f);
		
		for(int i=0 ; i<10 ; i++)
		{
			float lx = x + zoneWidth * (float)i / (float)10;
			canvas.drawLine(lx, y, lx, y + zoneHeight, paint);
		}
		for(int i=0 ; i<10 ; i++)
		{
			float ly = y + zoneHeight * (float)i / (float)10;
			canvas.drawLine(x, ly, x + zoneWidth, ly, paint);
		}
		
	}

}
