package cx.mccormick.pddroidparty.widget.core;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import cx.mccormick.pddroidparty.pd.PdGUI;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.Widget;

public class VUMeter extends Widget
{

	private static final String TAG = "VUMeter";

	WImage bg = new WImage();

	protected boolean displayScale;
	
	protected static float [] dbLabelScale = {-99, -50, -30, -20, -12, -6, -2, 0, 2, 6, 12};
	protected static String [] dbLabel = {"<-99", "-50", "-30", "-20", "-12", "-6", "-2", "-0db", "+2", "+6", ">+12"};
	
	protected static int [] dbColorIndex = {0, 1, 1, 1, 1, 2, 3, 4, 4, 4, 4, 5};
	protected static int [] dbColor = {0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF7F00, 0xFFFF0000, 0xFFFF00FF};
	
	
	public VUMeter(PdDroidPatchView app, String[] atomline) 
	{
		super(app);
		
		// parse
		float x = Float.parseFloat(atomline[2]) ;
		float y = Float.parseFloat(atomline[3]) ;
		
		float w = Integer.parseInt(atomline[5]);
		float h = Integer.parseInt(atomline[6]);
		
		dRect.left = x;
		dRect.top = y;
		dRect.right = x + w;
		dRect.bottom = y + h;
		
		receivename = atomline[7];
		label = setLabel(atomline[8]);
		labelpos[0] = Float.parseFloat(atomline[9]) ;
		labelpos[1] = Float.parseFloat(atomline[10]) ;
		labelfont = Integer.parseInt(atomline[11]);
		labelsize = (int)(Float.parseFloat(atomline[12]));
		
		bgcolor = PdGUI.getColor(Integer.parseInt(atomline[13]));
		labelcolor = PdGUI.getColor(Integer.parseInt(atomline[14]));
		
		displayScale = Integer.parseInt(atomline[15]) != 0;
		
		bg.load(TAG, "background", label, sendname);
		
		setupreceive();
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		if (bg.draw(canvas)) 
		{
			// PD style
			paint.setColor(bgcolor);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawRect(dRect,paint);

			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(fgcolor);
			paint.setStrokeWidth(1);
			canvas.drawRect(dRect,paint);
		}
		
		paint.setStyle(Paint.Style.FILL);
		
		RectF barRect = new RectF();
		barRect.left = dRect.left + dRect.width() / 4;
		barRect.right = dRect.right - dRect.width() / 4;
		
		float barSpace = dRect.height() / (float)(dbLabelScale.length + 1);
		float barHeight = barSpace / 2;

		// draw value
		for(int step=0 ; step<dbLabelScale.length ; step++)
		{
			float lowValue = dbLabelScale[step];
			if(val >= lowValue)
			{
				int color = dbColor[dbColorIndex[step]];
				paint.setColor(color);
				
				barRect.bottom = dRect.bottom - (step + 0.5f) * barSpace;
				barRect.top = barRect.bottom - barHeight;
				
				canvas.drawRect(barRect,paint);
			}
		}
		
		if(displayScale)
		{
			float labSpace = dRect.height() / (float)dbLabelScale.length;

			paint.setTypeface(font);
			paint.setTextSize(labelsize);
			paint.setColor(labelcolor);
			for(int step=0 ; step<dbLabelScale.length ; step++)
			{
				canvas.drawText(dbLabel[step], dRect.right + 3, dRect.bottom - step * labSpace, paint);
			}
		}
		
		drawLabel(canvas);
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
		val = v;
	}
}
