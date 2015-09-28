package cx.mccormick.pddroidparty.widget.abs;

import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.Widget;

public class Touch extends Widget {
	private static final String TAG = "Touch";
	
	WImage on = new WImage();
	WImage off = new WImage();
	
	boolean down = false;
	int pid0 = -1; //pointer id when down
	
	public Touch(PdDroidPatchView app, String[] atomline) {
		super(app);
		
		float x = Float.parseFloat(atomline[2]) ;
		float y = Float.parseFloat(atomline[3]) ;
		float w = Float.parseFloat(atomline[5]) ;
		float h = Float.parseFloat(atomline[6]) ;
		
		// graphics setup
		dRect = new RectF(Math.round(x), Math.round(y), Math.round(x + w), Math.round(y + h));
		
		sendname = app.replaceDollarZero(atomline[7]);
		
		// try and load images
		on.load(TAG, "on", sendname);
		off.load(TAG, "off", sendname);
	}
	
	public void draw(Canvas canvas) 
	{
		if(down)
		{
			if(on.draw(canvas))
			{
				paint.setStyle(Style.FILL);
				paint.setColor(bgcolor);
				canvas.drawRect(dRect, paint);
				
				paint.setStyle(Style.STROKE);
				paint.setStrokeWidth(2);
				paint.setColor(fgcolor);
				canvas.drawRect(dRect, paint);
			}
		}
		else
		{
			if(off.draw(canvas))
			{
				paint.setStyle(Style.FILL);
				paint.setColor(bgcolor);
				canvas.drawRect(dRect, paint);
				
				paint.setStyle(Style.STROKE);
				paint.setStrokeWidth(1);
				paint.setColor(fgcolor);
				canvas.drawRect(dRect, paint);
			}
		}
	}
	
	public void Sendxy(float x, float y)
	{
		send(((x - dRect.left) / dRect.width()) + " "
				+ ((y - dRect.top) / dRect.height()));
	}
	
	public boolean touchdown(int pid, float x, float y)
	{
		if (dRect.contains(x, y)) {

			down = true;
			pid0 = pid;
			Sendxy(x,y);
			return true;
		}
		
		return false;
	}
	
	public boolean touchup(int pid, float x, float y)
	{
		if(pid == pid0) {
			down = false;
			pid0 = -1;
			Sendxy(x,y);
			//send("-1 -1");
		}
		return false;
	}
	
	public boolean touchmove(int pid, float x, float y)
	{
		if(pid == pid0) {
			Sendxy(x,y);		
			return true;
		}
		return false;	
	}

}
