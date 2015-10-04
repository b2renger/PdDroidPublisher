package cx.mccormick.pddroidparty.test.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.core.Comment;

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
