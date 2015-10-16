package net.mgsx.ppp.view;

import net.mgsx.ppp.R;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

public class NumberSelector extends TextView
{
	public static interface OnValueChangeListener
	{
		public void onValueChange(float oldValue, float newValue);
	}
	
	private OnValueChangeListener onValueChangeListener;
	
	private float previousX;
	private float currentValue, previousValue;
	private float valuePerPixel;
	private Float min, max;
	private int digits, decimal;
	private int pid = -1;
	private String unit;

	public NumberSelector(Context context) {
		super(context);
		
		setPadding(4, 2, 4, 2); // XXX
		setBackgroundResource(R.drawable.textback);
		
		setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		
		setValuePerInch(1);
		
		// default value
		updateText();
	}
	
	public void setDigits(int digits) {
		this.digits = digits;
		updateTextSize();
	}
	public void setValuePerInch(float value) {
		this.valuePerPixel = value / getResources().getDisplayMetrics().xdpi;
	}
	
	public void setDecimal(int decimal) {
		this.decimal = decimal;
		updateTextSize();
	}
	
	private void changeValue(float newValue)
	{
		float scale = (float)Math.pow(10, decimal);
		newValue = Math.round(newValue * scale) / scale;
		if(min != null)
		{
			newValue = Math.max(min, newValue);
		}
		if(max != null)
		{
			newValue = Math.min(max, newValue);
		}
		if(newValue != currentValue)
		{
			if(onValueChangeListener != null)
			{
				onValueChangeListener.onValueChange(currentValue, newValue);
			}
			currentValue = newValue;
			updateText();
		}
	}
	
	public void setMin(Float min) {
		this.min = min;
	}
	public void setMax(Float max) {
		this.max = max;
	}
	
	private void updateTextSize()
	{
		int size = digits;
		if(decimal > 0) size += 1 + decimal;
		if(unit != null) size += 1 + unit.length();
		float measureText = getPaint().measureText("0") * size;
		setWidth(getPaddingLeft() + getPaddingRight() + (int) measureText);
	}
	
	private void updateText()
	{
		String text = String.format("%." + decimal + "f", currentValue);
		if(unit != null) text += " " + unit;
		setText(text);
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
		updateTextSize();
	}
	public void setValue(float value)
	{
		changeValue(value);
	}

	public void setOnValueChangedListener(OnValueChangeListener onValueChangeListener) {
		this.onValueChangeListener = onValueChangeListener;
	}
	
	@Override
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if(isEnabled())
		{
			int index = event.getActionIndex();
			switch(event.getActionMasked())
			{
			case MotionEvent.ACTION_DOWN:
				performClick();
				if(pid < 0)
				{
					pid = event.getPointerId(index);
					previousX = event.getX(index);
					previousValue = currentValue;
					return true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(event.getPointerId(index) == pid)
				{
					float x = event.getX(index);
					changeValue(previousValue + (x - previousX) * valuePerPixel);
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				if(event.getPointerId(index) == pid)
				{
					pid = -1;
					return true;
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}
}
