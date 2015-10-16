package net.mgsx.ppp.test.custom;

import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.core.Comment;
import android.graphics.Canvas;
import android.graphics.Color;

public class CustomComment extends Comment
{

	public CustomComment(PdDroidPatchView app, String[] atomline) {
		super(app, atomline);
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		paint.setColor(labelcolor);
		paint.setTextSize(labelsize);
		paint.setTypeface(font);
		paint.setShadowLayer(1, 1, 1, Color.GRAY);
		// convert from middle-left to baseline-left
		canvas.drawText(label, dRect.left + labelpos[0], dRect.top + labelpos[1] - paint.ascent(), paint);
	}

}
