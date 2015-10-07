package cx.mccormick.pddroidparty.widget.core;

import java.util.ArrayList;
import java.util.List;

import org.puredata.core.PdBase;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region.Op;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.widget.Widget;

public class Subpatch extends Widget
{
	public static class Array
	{
		public final static int DRAWTYPE_POLYGON = 0;
		public final static int DRAWTYPE_POINTS = 1;
		public final static int DRAWTYPE_BEZIER = 2;
		
		
		public String name;
		public int length;
		public String type;
		/** 0 : polygon, 1 : points, 2 : bezier */
		public int drawType; 
		public boolean save;
		public float [] buffer;
	}
	
	protected WImage background = new WImage();
	
	protected Array array;
	
	// TODO use dRect instead
	protected int top, bottom, left, right, zoneWidth, zoneHeight, HMargin, VMargin;
	protected int x, y;
	protected boolean graphOnParent;
	
	public List<Widget> widgets = new ArrayList<Widget>();
	
	/**
	 * Constructor overridden by custom Subpatch.
	 * @param subpatch
	 */
	public Subpatch(Subpatch subpatch) {
		super(subpatch.parent);
		background = subpatch.background;
		array = subpatch.array;
		top = subpatch.top;
		bottom = subpatch.bottom;
		left = subpatch.left;
		right = subpatch.right;
		zoneWidth = subpatch.zoneWidth;
		zoneHeight = subpatch.zoneHeight;
		HMargin = subpatch.HMargin;
		VMargin = subpatch.VMargin;
		x = subpatch.x;
		y = subpatch.y;
		graphOnParent = subpatch.graphOnParent;
		widgets = subpatch.widgets;
		dRect = subpatch.dRect;
		label = subpatch.label;
	}
	
	public Subpatch(PdDroidPatchView app, String[] atomline) {
		super(app);
	}
	
	public boolean isGraphOnParent() {
		return graphOnParent;
	}
	
	@Override
	public boolean touchmove(int pid, float x, float y) 
	{
		if(dRect.contains(x, y))
		{
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
			else
			{
				float localX = x - (this.x - HMargin);
				float localY = y - (this.y - VMargin);
				for(Widget widget : widgets)
				{
					if(widget.touchmove(pid, localX, localY)) return true;
				}
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
				label = array.name = atomline[2];
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
				if(atomline.length >= 6)
				{
					label = atomline[5];
				}
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
		else
		{
			for(Widget widget : widgets)
			{
				widget.updateData();
			}
		}
	}
	
	protected void drawBackground(Canvas canvas)
	{
		if(background.draw(canvas))
		{
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(bgcolor);	
			canvas.drawRect(x, y, x + zoneWidth, y + zoneHeight, paint);
		}
	}
	
	protected void drawEdges(Canvas canvas)
	{
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fgcolor);
		paint.setStrokeWidth(1);
		canvas.drawRect(x, y, x + zoneWidth, y + zoneHeight, paint);
	}
	
	protected void drawArrayCurve(Canvas canvas)
	{
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(fgcolor);
		if(array.drawType == Array.DRAWTYPE_POINTS)
		{
			paint.setStrokeWidth(3);
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
				paint.setStrokeWidth(0);
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
	}
	
	@Override
	public void drawLabel(Canvas canvas)
	{
		paint.setStrokeWidth(0);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(labelcolor);
		paint.setTextSize(fontsize);
		paint.setTypeface(font);
		canvas.drawText(label, dRect.left, dRect.top - paint.descent() - 2, paint);
	}
	
	protected void drawSubpatchContent(Canvas canvas)
	{
		canvas.save();
		Matrix matrix = new Matrix();
		matrix.postTranslate(x - HMargin, y - VMargin);
		canvas.concat(matrix);
		canvas.clipRect(new RectF(HMargin, VMargin, HMargin + dRect.width(), VMargin + dRect.height()), Op.INTERSECT);
		for(Widget widget : widgets)
		{
			widget.draw(canvas);
		}
		canvas.restore();
	}
	
	@Override
	public void draw(Canvas canvas) 
	{
		if(!graphOnParent) return;
		
		drawBackground(canvas);
		drawEdges(canvas);
		
		if(array != null)
		{
			drawArrayCurve(canvas);
		}
		// sub patch mode !
		else
		{
			drawSubpatchContent(canvas);
		}
		drawLabel(canvas);
	}
	
	@Override
	public boolean touchdown(int pid, float x, float y) 
	{
		if(dRect.contains(x, y))
		{
			float localX = x - (this.x - HMargin);
			float localY = y - (this.y - VMargin);
			for(Widget widget : widgets)
			{
				if(widget.touchdown(pid, localX, localY)) return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean touchup(int pid, float x, float y) {
		if(dRect.contains(x, y))
		{
			float localX = x - (this.x - HMargin);
			float localY = y - (this.y - VMargin);
			for(Widget widget : widgets)
			{
				if(widget.touchup(pid, localX, localY)) return true;
			}
		}
		return false;
	}
	
	@Override
	public void receiveAny() {
		for(Widget widget : widgets)
		{
			widget.receiveAny();
		}
	}
	@Override
	public void receiveBang() {
		for(Widget widget : widgets)
		{
			widget.receiveBang();;
		}
	}
	@Override
	public void receiveFloat(float x) {
		for(Widget widget : widgets)
		{
			widget.receiveFloat(x);
		}
	}
	@Override
	public void receiveList(Object... args) {
		for(Widget widget : widgets)
		{
			widget.receiveList(args);
		}
	}
	@Override
	public void receiveMessage(String symbol, Object... args) {
		for(Widget widget : widgets)
		{
			widget.receiveMessage(symbol, args);
		}
	}
	@Override
	public void receiveSymbol(String symbol) {
		for(Widget widget : widgets)
		{
			widget.receiveSymbol(symbol);
		}
	}
	

}
