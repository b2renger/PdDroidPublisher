package cx.mccormick.pddroidparty.widget.core;

import java.text.DecimalFormat;

import android.graphics.Canvas;
import android.graphics.Rect;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;

public class Numberbox2 extends Numberbox {
	
	public Numberbox2(PdDroidPatchView app, String[] atomline) {
		super(app);
		
		float x = Float.parseFloat(atomline[2]) ;
		float y = Float.parseFloat(atomline[3]) ;
		Rect tRect = new Rect();
		
		// calculate screen bounds for the numbers that can fit
		numwidth = Integer.parseInt(atomline[5]);
		StringBuffer calclen = new StringBuffer();
		for (int s=0; s<numwidth; s++) {
			if (s == 1) {
				calclen.append(".");
			} else {
				calclen.append("#");
			}
		}
		fmt = new DecimalFormat(calclen.toString());
		paint.getTextBounds(calclen.toString(), 0, calclen.length(), tRect);
		tRect.sort();
		
		float h = Float.parseFloat(atomline[6]) ;
		
		dRect.left = x;
		dRect.top = y;
		dRect.bottom = y + h;
		dRect.right = x + h/2 + tRect.width();
		
		min = Float.parseFloat(atomline[7]);
		max = Float.parseFloat(atomline[8]);
		init = Integer.parseInt(atomline[10]);
		sendname = app.replaceDollarZero(atomline[11]);
		receivename = atomline[12];
		label = setLabel(atomline[13]);
		labelpos[0] = Float.parseFloat(atomline[14]) ;
		labelpos[1] = Float.parseFloat(atomline[15]) ;
		
		// set the value to the init value if possible
		setval(Float.parseFloat(atomline[21]), 0);
		
		// listen out for floats from Pd
		setupreceive();
		
		// send initial value if we have one
		initval();
	}
	
	public void draw(Canvas canvas) 
	{
		paint.setColor(fgcolor);
		
		// edges lines
		canvas.drawLine(dRect.left, dRect.top, dRect.right - 5, dRect.top, paint);
		canvas.drawLine(dRect.left, dRect.bottom, dRect.right, dRect.bottom, paint);
		canvas.drawLine(dRect.left, dRect.top, dRect.left, dRect.bottom, paint);
		canvas.drawLine(dRect.right, dRect.top + 5, dRect.right, dRect.bottom, paint);
		canvas.drawLine(dRect.right - 5, dRect.top, dRect.right, dRect.top + 5, paint);
		
		// cursor lines
		canvas.drawLine(dRect.left, dRect.top, dRect.left + dRect.height()/2, dRect.centerY(), paint);
		canvas.drawLine(dRect.left + dRect.height()/2, dRect.centerY(), dRect.left + 1, dRect.bottom, paint);
		
		// draw value
		canvas.drawText(fmt.format(val), dRect.left + 3 + dRect.height()/2, dRect.centerY() - paint.ascent()/2, paint);
		drawLabel(canvas);
	
	}
}

