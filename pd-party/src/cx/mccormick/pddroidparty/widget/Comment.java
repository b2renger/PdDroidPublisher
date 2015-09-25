package cx.mccormick.pddroidparty.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;

public class Comment extends Widget {
	
	public Comment(PdDroidPatchView app, String[] atomline) {
		super(app);
		
		// create the comment string
		StringBuffer buffer = new StringBuffer();
		for (int i=4; i<atomline.length; i++) {
			buffer.append(atomline[i]);
			if (i < atomline.length - 1) {
				buffer.append(" ");
			}
		}
		
		label = buffer.toString();
		labelpos[0] = Float.parseFloat(atomline[2]) ;
		labelpos[1] = Float.parseFloat(atomline[3]) ;
	}
	
	public void draw(Canvas canvas) {
		if (label != null) {
			paint.setStrokeWidth(0);
			paint.setColor(labelcolor);
			paint.setTextSize(labelsize);
			paint.setTypeface(font);
			// convert from middle-left to baseline-left
			canvas.drawText(label, dRect.left + labelpos[0], dRect.top + labelpos[1] - paint.ascent(), paint);
			paint.setTextSize(fontsize);
		}
		paint.setColor(Color.BLACK);
	}
}
