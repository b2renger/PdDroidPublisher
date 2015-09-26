package cx.mccormick.pddroidparty.widget.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import cx.mccormick.pddroidparty.pd.PdGUI;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.Widget;

public class Radio extends Widget
{
	private static final String TAG = "Radio";

	WImage bgCell = new WImage();
	WImage bg = new WImage();
	WImage button = new WImage();

	protected boolean horizontal;
	protected int size, count;
	
	public Radio(PdDroidPatchView app, String[] atomline, boolean horizontal) 
	{
		super(app);
		
		this.horizontal = horizontal;
		
		// parse
		float x = Float.parseFloat(atomline[2]) ;
		float y = Float.parseFloat(atomline[3]) ;
		
		size = Integer.parseInt(atomline[5]);
		count = Integer.parseInt(atomline[8]);
		
		dRect.left = x;
		dRect.top = y;
		if(horizontal)
		{
			dRect.right = x + size * count;
			dRect.bottom = y + size;
		}
		else
		{
			dRect.right = x + size;
			dRect.bottom = y + size * count;
		}
		
		// TODO 6 => ? (always 1 ??? type ???)
		
		init = Integer.parseInt(atomline[7]);
		
		
		
		sendname = app.replaceDollarZero(atomline[9]);
		receivename = atomline[10];
		label = setLabel(atomline[11]);
		labelpos[0] = Float.parseFloat(atomline[12]) ;
		labelpos[1] = Float.parseFloat(atomline[13]) ;
		labelfont = Integer.parseInt(atomline[14]);
		labelsize = (int)(Float.parseFloat(atomline[15]));
		
		bgcolor = PdGUI.getColor(Integer.parseInt(atomline[16]));
		fgcolor = PdGUI.getColor(Integer.parseInt(atomline[17]));
		labelcolor = PdGUI.getColor(Integer.parseInt(atomline[18]));
		
		val = Integer.parseInt(atomline[19]);
		
		if (horizontal) {
			bg.load(TAG, "horizontal", label, sendname);
		} else {
			bg.load(TAG, "vertical", label, sendname);
		}
		bgCell.load(TAG, "background", label, sendname);
		button.load(TAG, "button", label, sendname);
		
		setupreceive();
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		if (bg.draw(canvas)) 
		{
			if(!bgCell.none())
			{
				RectF cellRect = new RectF();
				cellRect.left = dRect.left;
				cellRect.top = dRect.top;
				cellRect.right = cellRect.left + size;
				cellRect.bottom = cellRect.top + size;
				for(int i=0 ; i<count ; i++)
				{
					bgCell.draw(canvas, cellRect);
					if(horizontal)
					{
						cellRect.left += size;
						cellRect.right += size;
					}
					else
					{
						cellRect.top += size;
						cellRect.bottom += size;
					}
				}
			}
			else
			{
			
				// PD style
				paint.setColor(bgcolor);
				paint.setStyle(Paint.Style.FILL);
				canvas.drawRect(dRect,paint);
	
				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(fgcolor);
				paint.setStrokeWidth(1);
				RectF cellRect = new RectF();
				cellRect.left = dRect.left;
				cellRect.top = dRect.top;
				cellRect.right = cellRect.left + size;
				cellRect.bottom = cellRect.top + size;
				for(int i=0 ; i<count ; i++)
				{
					canvas.drawRect(cellRect,paint);
					if(horizontal)
					{
						cellRect.left += size;
						cellRect.right += size;
					}
					else
					{
						cellRect.top += size;
						cellRect.bottom += size;
					}
				}
			}
		}
		RectF sRect = new RectF();
		int index = (int)val;
		if(horizontal)
		{
			sRect.left = dRect.left + size * index;
			sRect.top = dRect.top;
		}
		else
		{
			sRect.left = dRect.left;
			sRect.top = dRect.top + size * index;
		}
		sRect.right = sRect.left + size;
		sRect.bottom = sRect.top + size;
		
		if(button.draw(canvas, sRect))
		{
			// button PD style
			paint.setColor(fgcolor);
			paint.setStyle(Paint.Style.FILL);

			RectF cellRect = new RectF();
			if(horizontal)
			{
				cellRect.left = dRect.left + size * (index + 0.25f);
				cellRect.top = dRect.top + size * 0.25f;
			}
			else
			{
				cellRect.left = dRect.left + size * 0.25f;
				cellRect.top = dRect.top + size * (index + 0.25f);
				
			}
			cellRect.right = cellRect.left + size * 0.5f;
			cellRect.bottom = cellRect.top + size * 0.5f;
			
			canvas.drawRect(cellRect,paint);
		}
		
		drawLabel(canvas);
	}
	
	@Override
	public boolean touchdown(int pid, float x, float y) 
	{
		if(dRect.contains(x, y))
		{
			int index;
			if(horizontal)
			{
				index = (int)(count * (x - dRect.left) / dRect.width());
			}
			else
			{
				index = (int)(count * (y - dRect.top) / dRect.height());
			}
			setval(index);
			send(String.valueOf((int)val));
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
	
	@Override
	public void setval(float v) 
	{
		int index = (int)v;
		if(v >= 0 && v <count)
		{
			val = index;
		}
	}

}
