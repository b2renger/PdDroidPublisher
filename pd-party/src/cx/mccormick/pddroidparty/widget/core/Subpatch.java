package cx.mccormick.pddroidparty.widget.core;

import java.util.ArrayList;
import java.util.List;

import org.puredata.core.PdBase;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.Widget;

public class Subpatch extends Widget
{
	public static class Array
	{
		public final static int DRAWTYPE_POLYGON = 0;
		public final static int DRAWTYPE_POINTS = 1;
		public final static int DRAWTYPE_BEZIER = 2;
		
		
		String name;
		int length;
		String type;
		/** 0 : polygon, 1 : points, 2 : bezier */
		int drawType; 
		boolean save;
		float [] buffer;
	}
	
	WImage background = new WImage();
	
	private Array array;
	
	// TODO use dRect instead
	private int top, bottom, left, right, zoneWidth, zoneHeight, HMargin, VMargin;
	private int x, y;
	boolean graphOnParent;
	
	public List<Widget> widgets = new ArrayList<Widget>();
	
	public Subpatch(PdDroidPatchView app, String[] atomline) {
		super(app);
	}
	
	@Override
	public boolean touchmove(int pid, float x, float y) 
	{
		if(x < this.x || x > this.x + this.zoneWidth) return false;
		if(y < this.y || y > this.y + this.zoneHeight) return false;
		
		if(array != null)
		{
			int index = (int)(left + (float)(right - left) * (x - this.x) / (float)(this.zoneWidth));
			if(index >= 0 && index < array.length)
			{
				float value = ((y - this.y) / (float)(zoneHeight)) * (top - bottom) + bottom;
				array.buffer[index] = value;
				PdBase.writeArray(array.name, index, array.buffer, index, 1);
				return true;
			}
		}
		return false;
	}

	public void parse(String[] atomline) 
	{
		if(atomline.length >= 2)
		{
			if(atomline[1].equals("array"))
			{
				array = new Array();
				array.name = atomline[2];
				array.length = Integer.parseInt(atomline[3]);
				array.type = atomline[4];
				int options = Integer.parseInt(atomline[5]);
				array.drawType = options >> 1;
				array.save = (options & 1) != 0;
				array.buffer = new float [array.length];
				
				background.load("Array", "background", array.name);
			}
			else if(atomline[1].equals("coords"))
			{
				left = Integer.parseInt(atomline[2]);
				bottom = Integer.parseInt(atomline[3]);
				right = Integer.parseInt(atomline[4]);
				top = Integer.parseInt(atomline[5]);
				zoneWidth = Integer.parseInt(atomline[6]);
				zoneHeight = Integer.parseInt(atomline[7]);
				graphOnParent = Integer.parseInt(atomline[8]) != 0;
				// TODO optional margin h/v
				
				// optional margin h/v
				if(atomline.length >= 11)
				{
					HMargin = Integer.parseInt(atomline[9]);
					VMargin = Integer.parseInt(atomline[10]);
				}
				
				dRect.right += zoneWidth;
				dRect.bottom += zoneHeight;
			}
			else if(atomline[1].equals("restore")) 
			{
				x = Integer.parseInt(atomline[2]);
				y = Integer.parseInt(atomline[3]);
				dRect.left += x;
				dRect.right += x;
				dRect.top += y;
				dRect.bottom += y;
			}
			// TODO handle array definition (saved)
		}
	}
	
	@Override
	public void updateData() 
	{
		if(array != null)
		{
			PdBase.readArray(array.buffer, 0, array.name, 0, array.buffer.length);
		}
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		if(!graphOnParent) return;
		
		if(background.draw(canvas))
		{
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(bgcolor);	
			canvas.drawRect(x, y, x + zoneWidth, y + zoneHeight, paint);
		}
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fgcolor);
		paint.setStrokeWidth(1);
		canvas.drawRect(x, y, x + zoneWidth, y + zoneHeight, paint);

		if(array != null)
		{
			if(array.drawType == Array.DRAWTYPE_POINTS)
			{
				float ppx = 0, ppy = 0;
				if(array.length < zoneWidth)
				{
					for(int i=0 ; i<array.buffer.length+1 ; i++)
					{
						float px = x + (float)i * (float)zoneWidth / (float)array.buffer.length;
						if(i > 0){
							float value = array.buffer[i-1];
							float py = y + zoneHeight * (float)(value - bottom) / (float)(top - bottom);
							canvas.drawLine(ppx, py, px, py, paint);
						}
						ppx = px;
					}
				}
				else
				{
					for(int i=0 ; i<zoneWidth ; i++)
					{
						int index = (int)((float)array.buffer.length * (float)i / (float)(zoneWidth));
						float value = array.buffer[index];
						float px = x + (float)i;
						float py = y + zoneHeight * (float)(value - bottom) / (float)(top - bottom);
						if(i > 0){
							canvas.drawLine(ppx, ppy, px, py, paint);
						}
						ppx = px;
						ppy = py;
					}
				}
				
			}
			// TODO handle bezier drawing
			else
			{
			
				float ppx = 0, ppy = 0;
				if(array.length < zoneWidth)
				{
					for(int i=0 ; i<array.buffer.length ; i++)
					{
						float value = array.buffer[i];
						float px = x + (float)i * (float)zoneWidth / (float)array.buffer.length;
						float py = y + zoneHeight * (float)(value - bottom) / (float)(top - bottom);
						if(i > 0){
							canvas.drawLine(ppx, ppy, px, py, paint);
						}
						ppx = px;
						ppy = py;
					}
				}
				else
				{
					for(int i=0 ; i<zoneWidth ; i++)
					{
						int index = (int)((float)array.buffer.length * (float)i / (float)(zoneWidth));
						float value = array.buffer[index];
						float px = x + (float)i;
						float py = y + zoneHeight * (float)(value - bottom) / (float)(top - bottom);
						if(i > 0){
							canvas.drawLine(ppx, ppy, px, py, paint);
						}
						ppx = px;
						ppy = py;
					}
				}
			}
			
			paint.setStrokeWidth(0);
			paint.setColor(labelcolor);
			paint.setTextSize(fontsize);
			paint.setTypeface(font);
			canvas.drawText(array.name, dRect.left, dRect.top - paint.descent() - 2, paint);

		}
		// sub patch mode !
		else
		{
			canvas.save();
			Matrix matrix = new Matrix();
			matrix.postTranslate(x - left - HMargin, y - top - VMargin);
			canvas.concat(matrix);
			for(Widget widget : widgets)
			{
				widget.draw(canvas);
			}
			canvas.restore();
		}

	}

}
