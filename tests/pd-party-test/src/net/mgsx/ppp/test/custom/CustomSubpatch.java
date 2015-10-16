package net.mgsx.ppp.test.custom;

import net.mgsx.ppp.widget.core.Subpatch;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class CustomSubpatch extends Subpatch
{
	float alinea = 6;
	protected float labelWidth;
	
	public CustomSubpatch(Subpatch subpatch) {
		super(subpatch);
		
		Rect bounds = new Rect();
		paint.getTextBounds(label, 0, label.length(), bounds);
		labelWidth = bounds.width();
	}
	
	@Override
	protected void drawEdges(Canvas canvas) {
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fgcolor);
		paint.setStrokeWidth(1);
		canvas.drawLines(new float[]{
				x + labelWidth + alinea + 5, y,
				x + zoneWidth, y,
				x + zoneWidth, y,
				x + zoneWidth, y + zoneHeight,
				x + zoneWidth, y + zoneHeight,
				x, y + zoneHeight,
				x, y + zoneHeight,
				x, y,
				x, y,
				x + alinea, y
		}, paint);
	}
	
	@Override
	public void drawLabel(Canvas canvas) 
	{
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(labelcolor);
		paint.setTextSize(fontsize);
		paint.setTypeface(font);
		canvas.drawText(label, dRect.left + alinea + 1, dRect.top + paint.descent(), paint);
	}

}
