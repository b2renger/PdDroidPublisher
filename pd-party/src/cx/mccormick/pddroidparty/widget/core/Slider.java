package cx.mccormick.pddroidparty.widget.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region.Op;
import cx.mccormick.pddroidparty.pd.PdGUI;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.Widget;

public class Slider extends Widget {
	private static final String TAG = "Slider";
	
	protected float min, max;
	protected boolean log;
	
	int pid0=-1;			// pointer id,
	float x0,y0,val0 ; 	// position of pointer, and value when pointer down.
	
	boolean orientation_horizontal = true;
	boolean down = false;
	protected boolean steady = true;
	
	WImage bg = new WImage();
	WImage slider = new WImage();
	WImage fg = new WImage();
	
	RectF sRect = new RectF();
	
	public Slider(PdDroidPatchView app, String[] atomline, boolean horizontal) {
		super(app);
		
		orientation_horizontal = horizontal;
		
		float x = Float.parseFloat(atomline[2]) ;
		float y = Float.parseFloat(atomline[3]) ;
		float w = Float.parseFloat(atomline[5]) ;
		float h = Float.parseFloat(atomline[6]) ;
		
		min = Float.parseFloat(atomline[7]);
		max = Float.parseFloat(atomline[8]);
		log = Integer.parseInt(atomline[9]) != 0;
		init = Integer.parseInt(atomline[10]);
		sendname = app.replaceDollarZero(atomline[11]);
		receivename = atomline[12];
		label = setLabel(atomline[13]);
		labelpos[0] = Float.parseFloat(atomline[14]) ;
		labelpos[1] = Float.parseFloat(atomline[15]) ;
		labelfont = Integer.parseInt(atomline[16]);
		labelsize = (int)(Float.parseFloat(atomline[17]));
		bgcolor = PdGUI.getColor(Integer.parseInt(atomline[18]));
		fgcolor = PdGUI.getColor(Integer.parseInt(atomline[19]));
		labelcolor = PdGUI.getColor(Integer.parseInt(atomline[20]));
		steady = Integer.parseInt(atomline[22]) != 0;

		setval((float)(Float.parseFloat(atomline[21]) * 0.01 * (max - min) / ((horizontal ? Float.parseFloat(atomline[5]) : Float.parseFloat(atomline[6])) - 1) + min), min);
		
		// listen out for floats from Pd
		setupreceive();
		
		// send initial value if we have one
		//initval();
		
		// graphics setup
		dRect = new RectF(Math.round(x), Math.round(y), Math.round(x + w), Math.round(y + h));
		
		// load up the images to use and cache all positions
		if (horizontal) {
			bg.load(TAG, "horizontal", label, sendname);
			fg.load(TAG, "horizontal-foreground", label, sendname);
			slider.load(TAG, "widget-horizontal", label, sendname);
		} else {
			bg.load(TAG, "vertical", label, sendname);
			fg.load(TAG, "vertical-foreground", label, sendname);
			slider.load(TAG, "widget-vertical", label, sendname);
		}
		
		if ( (!bg.none()) && (!slider.none()) ) {
			// create the slider rectangle thingy
			if (orientation_horizontal) {
				float ratio = slider.getHeight() / h;
				int rel = (int)(slider.getWidth() / ratio);
				sRect = new RectF(x, y, x + rel, y + h);
			} else {
				float ratio = slider.getWidth() / w;
				int rel = (int)(slider.getHeight() / ratio);
				sRect = new RectF(x, y, x + w, y + rel);
			}
		}
		
		setval(val);
	}
	
	protected float getNormalizedPosition()
	{
		return getNormalizedPosition(val);
	}
	
	protected float getNormalizedPosition(float v)
	{
		if(log)
		{
			return (float)((Math.log10(v) - Math.log10(min)) / (Math.log10(max) - Math.log10(min)));
		}
		else
		{
			return (v - min) / (max - min);
		}
	}
	
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.clipRect(dRect.left, dRect.top, dRect.right, dRect.bottom + 1, Op.INTERSECT);
		if (bg.draw(canvas)) {
			paint.setColor(bgcolor);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawRect(dRect,paint);

			paint.setColor(fgcolor);
			paint.setStrokeWidth(1);
			canvas.drawLine(dRect.left /*+ 1*/, dRect.top, dRect.right, dRect.top, paint);
			canvas.drawLine(dRect.left /*+ 1*/, dRect.bottom, dRect.right, dRect.bottom, paint);
			canvas.drawLine(dRect.left, dRect.top /*+ 1*/, dRect.left, dRect.bottom, paint);
			canvas.drawLine(dRect.right, dRect.top /*+ 1*/, dRect.right, dRect.bottom, paint);
			paint.setColor(fgcolor);
			paint.setStrokeWidth(3);
			if (orientation_horizontal) {
				canvas.drawLine(Math.round(dRect.left + getNormalizedPosition() * dRect.width()), Math.round(dRect.top /*+ 2*/), Math.round(dRect.left + getNormalizedPosition() * dRect.width()), Math.round(dRect.bottom /*- 2*/), paint);
			} else {
				canvas.drawLine(Math.round(dRect.left /*+ 2*/), Math.round(dRect.bottom - getNormalizedPosition() * dRect.height()), Math.round(dRect.right /*- 2*/), Math.round(dRect.bottom - getNormalizedPosition() * dRect.height()), paint);
			}

		} else if (!slider.none()) {
			if (orientation_horizontal) {
				sRect.offsetTo(getNormalizedPosition() * (dRect.width() - sRect.width()) + dRect.left, dRect.top);
			} else {
				sRect.offsetTo(dRect.left, (1 - getNormalizedPosition()) * (dRect.height() - sRect.height()) + dRect.top);
			}
			slider.draw(canvas,sRect);
			fg.draw(canvas);
		}
		canvas.restore();
		drawLabel(canvas);
	}

	public void setval(float v) {
		val = Math.min(max, Math.max(min, v));
	}
	
	public float get_horizontal_val(float x) {
		if(log)
			return (float)Math.pow(10, (((x - dRect.left) / dRect.width()) * (Math.log10(max) - Math.log10(min)) + Math.log10(min)));
		else
			return (((x - dRect.left) / dRect.width()) * (max - min) + min);
	}
	
	public float get_vertical_val(float y) {
		if(log)
			return (float)Math.pow(10, ((dRect.height() - (y - dRect.top)) / dRect.height()) * (Math.log10(max) - Math.log10(min)) + Math.log10(min));
		else
			return (((dRect.height() - (y - dRect.top)) / dRect.height()) * (max - min) + min);
	}
	public float get_horizontal_val(float x, float x0) {
		float xBase = dRect.left + getNormalizedPosition(val0) * dRect.width();
		return get_horizontal_val(xBase + x - x0);
	}
	
	public float get_vertical_val(float y, float y0) {
		float yBase = dRect.bottom - getNormalizedPosition(val0) * dRect.height();
		return get_vertical_val(yBase + y - y0);
	}
	
	public boolean touchdown(int pid,float x,float y)
	{
		if (dRect.contains(x, y)) {
			val0=val;
			x0=x;
			y0=y;
			pid0=pid;
			if(!steady) {
				if (orientation_horizontal) val = get_horizontal_val(x);
				else val = get_vertical_val(y);
			}
			send("" + val);
			return true;
		}
		return false;
	}

	public boolean touchup(int pid,float x,float y)
	{
		if(pid0 == pid) {
			pid0 = -1;
			return true;
		}
		return false;
	}

	public boolean touchmove(int pid,float x,float y)
	{
		if(pid0 == pid) {
			if (orientation_horizontal) {
				if(steady)
				{
					val = get_horizontal_val(x, x0);
				}
				else
				{
					val = get_horizontal_val(x);
				}
			} else {
				if(steady)
				{
					val = get_vertical_val(y, y0);
				}
				else
				{
					val = get_vertical_val(y);
				}
			}
			// clamp the value
			setval(val);
			// send the result to Pd
			send("" + val);
			return true;
		}
		return false;
	}
	
	public void receiveMessage(String symbol, Object... args) {
		if(widgetreceiveSymbol(symbol,args)) return;
		if (args.length > 0 && args[0].getClass().equals(Float.class)) {
			receiveFloat((Float)args[0]);
		}
	}
	
	public void receiveList(Object... args) {
		if (args.length > 0 && args[0].getClass().equals(Float.class)) {
			receiveFloat((Float)args[0]);
		}
	}
	
	public void receiveFloat(float v) {
		setval(v);
	}
}


