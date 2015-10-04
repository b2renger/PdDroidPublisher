package cx.mccormick.pddroidparty.test.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import cx.mccormick.pddroidparty.widget.core.Subpatch;

public class CustomSubpatch extends Subpatch
{

	public CustomSubpatch(Subpatch subpatch) {
		super(subpatch);
	}
	
	@Override
	protected void drawEdges(Canvas canvas) {
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fgcolor);
		paint.setStrokeWidth(5);
		canvas.drawRect(x, y, x + zoneWidth, y + zoneHeight, paint);
	}

}
