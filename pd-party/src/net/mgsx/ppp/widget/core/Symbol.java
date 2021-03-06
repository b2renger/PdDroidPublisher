package net.mgsx.ppp.widget.core;

import net.mgsx.ppp.SymbolDialog;
import net.mgsx.ppp.view.PdDroidPatchView;
import net.mgsx.ppp.widget.Widget;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class Symbol extends Widget
{
	private static final String TAG = "Symbol";

	// TODO refactor for other widgets
	public static final int POSITION_LEFT = 0;
	public static final int POSITION_RIGHT = 1;
	public static final int POSITION_TOP = 2;
	public static final int POSITION_BOTTOM = 3;
	
	protected float min;
	protected float max;
	protected int numChars;
	protected int labelPosition;
	
	protected String value;
	
	WImage background = new WImage();
	
	protected boolean down = false;
	int pid0 = -1; //pointer id when down


	public Symbol(PdDroidPatchView app, String[] atomline) 
	{
		super(app);
		
		float x = Float.parseFloat(atomline[2]) ;
		float y = Float.parseFloat(atomline[3]) ;
		
		// calculate screen bounds for the numbers that can fit
		numChars = Integer.parseInt(atomline[4]);
		StringBuffer calclen = new StringBuffer();
		for (int s=0; s<numChars; s++) {
				calclen.append("W");
		}
		
		Rect tRect = new Rect();
		paint.getTextBounds(calclen.toString(), 0, calclen.length(), tRect);
		tRect.sort();
		
		float w = tRect.width();
		float h = tRect.height() + 6; // XXX margin v
		
		dRect.left = x;
		dRect.top = y;
		dRect.bottom = y + h;
		dRect.right = x + w;
		
		min = Float.parseFloat(atomline[5]);
		max = Float.parseFloat(atomline[6]);
		sendname = app.replaceDollarZero(atomline[10]);
		receivename = atomline[9];
		if(sendname.endsWith(",")) sendname = sendname.substring(0, sendname.length()-1);
		label = setLabel(atomline[8]);
		
		if(label != null)
		{
			labelPosition = Integer.parseInt(atomline[7]);
			
			Rect labelRect = new Rect();
			paint.getTextBounds(label, 0, label.length(), labelRect);
			
			float marging = 2; // XXX magic number
			
			labelpos[0] = 0; 
			labelpos[1] = 0;
			switch(labelPosition)
			{
			case POSITION_RIGHT:
				labelpos[0] = dRect.width() + marging;
				labelpos[1] = dRect.height()/2;
				break;
			case POSITION_LEFT:
				labelpos[0] = -labelRect.width() - marging - 2; // XXX
				labelpos[1] = dRect.height()/2;
				break;
			case POSITION_BOTTOM:
				labelpos[1] = dRect.height() - paint.ascent()/2 + marging;
				break;
			case POSITION_TOP:
				labelpos[1] = -labelRect.height() - marging;
				break;
			}
		}
		
		background.load(TAG, "back", label, sendname, receivename);
		
		setval(0, 0);
		
		// listen out for floats from Pd
		setupreceive();
	}
	
	public void draw(Canvas canvas) {
		paint.setColor(fgcolor);
		if(background.draw(canvas))
		{
			canvas.drawLine(dRect.left, dRect.top, dRect.right - 5, dRect.top, paint);
			canvas.drawLine(dRect.left, dRect.bottom, dRect.right, dRect.bottom, paint);
			canvas.drawLine(dRect.left, dRect.top, dRect.left, dRect.bottom, paint);
			canvas.drawLine(dRect.right, dRect.top + 5, dRect.right, dRect.bottom, paint);
			canvas.drawLine(dRect.right - 5, dRect.top, dRect.right, dRect.top + 5, paint);
		}
		if(value != null)
		{
			canvas.drawText(value, dRect.left + 3, dRect.centerY() - paint.ascent()/2, paint);
		}
		drawLabel(canvas);
	}

	public boolean touchdown(int pid, float x,float y) {
		if (dRect.contains(x, y)) {
			down = true;
			pid0 = pid;
			return true;
		}
		return false;
	}
	
	public boolean touchup(int pid, float x,float y) {
		if (pid == pid0) {
			openEditDialog();
			down = false;
			pid0 = -1;
			return true;
		}
		return false;
	}
	
	private void openEditDialog()
	{
		final SymbolDialog editDialog = new SymbolDialog(parent.getContext(), value);
		editDialog.setTitle(label == null ? "Edit symbol" : label);
		editDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				String selectedValue = editDialog.getSelectedValue();
				if(selectedValue != null)
				{
					setValue(selectedValue);
					parent.invalidate();
				}
			}
		});
		editDialog.show();
	}

	
	public void touch_(MotionEvent event) {

		int action = event.getAction() & MotionEvent.ACTION_MASK;
		int pid, index;
		float ex;
		float ey;

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			ex = event.getX();
			ey = event.getY();
			if (dRect.contains(ex, ey)) {
				down = true;
			}
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			pid = event.getAction() >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			index = event.findPointerIndex(pid);
			Log.d("dwnNBoxBefore", index+"");
			index=(index==-1)?1:index;
			Log.d("dwnNBoxAfter", index+"");
			ex = event.getX(index);
			ey = event.getY(index);
			if (dRect.contains(ex, ey)) {
				down = true;
			}
			break;

		case MotionEvent.ACTION_UP:
			ex = event.getX();
			ey = event.getY();
			if (dRect.contains(ex, ey)) {
				openEditDialog();
			}
			down = false;
			break;

		case MotionEvent.ACTION_POINTER_UP:
			pid = event.getAction() >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
			index = event.findPointerIndex(pid);
			Log.d("upNBoxBefore", index+"");
			index=(index==-1)?1:index;
			Log.d("NBoxAfter", index+"");
			ex = event.getX(index);
			ey = event.getY(index);
			if (dRect.contains(ex, ey)) {
				openEditDialog();
			}
			down = false;
			break;

		}

		
	}
	
	public void receiveFloat(float v) {
		setValue("float");
	}
	
	protected void setValue(String newValue)
	{
		value = newValue;
		send(value);
	}
	
	public void receiveList(Object... args) {
		if (args.length > 0 && args[0] instanceof String) 
		{
			setValue(args[0].toString());
		}
	}

	
	public void receiveMessage(String symbol, Object... args) {
		if(widgetreceiveSymbol(symbol,args)) return;
		if (args.length >= 2 && args[0] instanceof String && (String)args[0] == "symbol") 
		{
			setValue(args[1].toString());
		}
	}

	

}
