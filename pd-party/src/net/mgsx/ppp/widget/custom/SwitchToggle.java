package net.mgsx.ppp.widget.custom;

import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.core.Toggle;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class SwitchToggle extends Toggle
{
	protected float cursorX, px;
	protected int pid = -1;

	public SwitchToggle(PdDroidPatchView app, String[] atomline) {
		super(app, atomline);
		dRect.bottom = dRect.top + dRect.width()/2;
		if(val > 0)
		{
			cursorX = dRect.width()/2;
		}
		else
		{
			cursorX = 0;
		}
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		paint.setColor(bgcolor);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(dRect,paint);

		paint.setColor(fgcolor);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText("I", dRect.left + dRect.width()/4, dRect.top + dRect.height()/2 + paint.descent(), paint);
		canvas.drawText("O", dRect.left + 3*dRect.width()/4, dRect.top + dRect.height()/2 + paint.descent(), paint);
		
		canvas.drawRect(dRect.left + cursorX, dRect.top, dRect.left + cursorX + dRect.width()/2, dRect.bottom, paint);
		
		paint.setStrokeWidth(1);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(dRect,paint);
		
		drawLabel(canvas);
	}
	
	@Override
	public boolean touchdown(int pid, float x, float y) {
		if(this.pid < 0 && dRect.contains(x, y))
		{
			this.pid = pid;
			px = x;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean touchup(int pid, float x, float y) {
		if(this.pid == pid)
		{
			float v = cursorX > dRect.width()/4 ? 1 : 0;
			cursorX = v > 0 ? dRect.width()/2 : 0;
			this.pid = -1;
			if(v != val)
			{
				val = v;
				sendFloat(val);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean touchmove(int pid, float x, float y) {
		if(this.pid == pid)
		{
			cursorX = Math.min(dRect.width()/2, Math.max(0, x - px + (val > 0 ? dRect.width()/2 : 0)));
			return true;
		}
		return false;
	}

}
