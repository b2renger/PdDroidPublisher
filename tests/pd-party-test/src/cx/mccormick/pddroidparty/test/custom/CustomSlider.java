package cx.mccormick.pddroidparty.test.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.core.Slider;

public class CustomSlider extends Slider 
{
	protected boolean drag;
	protected float px, py;
	
	public CustomSlider(PdDroidPatchView app, String[] atomline) {
		super(app, atomline, true);
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		paint.setColor(bgcolor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawOval(dRect, paint);

		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(1);
		canvas.drawOval(dRect, paint);
		
		float value = (val - min) / (max - min);
		float angle = (float)Math.PI * 2 * value;
		float radius = dRect.width()/6;
		float cx = (dRect.left + dRect.right) / 2 + (float)Math.cos(angle) * (dRect.width()/2 - radius);
		float cy = (dRect.top + dRect.bottom) / 2 + (float)Math.sin(angle) * (dRect.width()/2 - radius);
		
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(fgcolor);
		paint.setStrokeWidth(3);
		
		canvas.drawCircle(cx, cy, radius, paint);
	}
	
	@Override
	public boolean touchdown(int pid, float x, float y) {
		if(dRect.contains(x, y))
		{
			drag = true;
			px = x;
			py = y;
		}
		return false;
	}
	@Override
	public boolean touchup(int pid, float x, float y) {
		drag = false;
		return false;
	}
	@Override
	public boolean touchmove(int pid, float x, float y) 
	{
		if(drag)
		{
			float dx = x - px;
			float dy = y - py;
			px = x;
			py = y;
			
			val += (dx - dy) * (max - min) / 100; // XXX 100 px speed
			
			// clamp the value
			setval(val);
			// send the result to Pd
			send("" + val);
			return true;
		}
		return false;
	}
	
}
