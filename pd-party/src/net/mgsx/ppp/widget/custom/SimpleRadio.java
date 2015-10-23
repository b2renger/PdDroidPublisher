package net.mgsx.ppp.widget.custom;

import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.core.Radio;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class SimpleRadio extends Radio {

	public SimpleRadio(PdDroidPatchView app, String[] atomline, boolean horizontal) {
		super(app, atomline, horizontal);
	}

	@Override
	public void draw(Canvas canvas) {

		int index = (int) val;
		paint.setColor(fgcolor);
		if (horizontal) {

			for (int i = 0; i < count; i++) {
				paint.setStyle(Paint.Style.STROKE);
				RectF cellRect = new RectF();
				cellRect.left = dRect.left + size * (i);
				cellRect.top = dRect.top + size;
				cellRect.right = cellRect.left + size * 0.90f;
				cellRect.bottom = cellRect.top - size * 0.5f;
				canvas.drawRect(cellRect,  paint);

				if (i == index) {
					paint.setStyle(Paint.Style.FILL);
					canvas.drawRect(cellRect, paint);
				}
				paint.setStyle(Paint.Style.FILL);
				drawLabel(canvas);
			}
		}
		else {
			for (int i = 0; i < count; i++) {
				paint.setStyle(Paint.Style.STROKE);
				RectF cellRect = new RectF();
				cellRect.left = dRect.left ;
				cellRect.top = dRect.top + size* i;
				cellRect.right = cellRect.left + size * 0.50f;
				cellRect.bottom = cellRect.top + size * 0.9f;
				canvas.drawRect(cellRect, paint);

				if (i == index) {
					paint.setStyle(Paint.Style.FILL);
					canvas.drawRect(cellRect,  paint);
				}
				paint.setStyle(Paint.Style.FILL);
				drawLabel(canvas);
			}
			
		}

	}

}
