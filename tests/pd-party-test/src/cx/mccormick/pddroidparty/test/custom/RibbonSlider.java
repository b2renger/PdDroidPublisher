package cx.mccormick.pddroidparty.test.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.core.Slider;

public class RibbonSlider extends Slider 
{
	public RibbonSlider(PdDroidPatchView app, String[] atomline) {
		super(app, atomline, false);
	}
	
	public void draw(Canvas canvas) 
	{
		paint.setColor(bgcolor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(dRect, paint);

		paint.setColor(fgcolor);
		paint.setStyle(Paint.Style.FILL);
		float offset = dRect.height() * (val - min) / (max - min);
		canvas.drawRect(dRect.left, dRect.bottom - offset, dRect.right, dRect.bottom, paint);

		paint.setColor(fgcolor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		canvas.drawRect(dRect, paint);
	}
	
}
