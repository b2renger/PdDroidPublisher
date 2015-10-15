package cx.mccormick.pddroidparty;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NumberboxDialog extends Dialog 
{
	private float defaultValue;
	private Float selectedValue;
	
	public NumberboxDialog(Context context, float defaultValue) {
		super(context);
		this.defaultValue = defaultValue;
	}
	
	public Float getSelectedValue() {
		return selectedValue;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.numberbox_dialog);
		
		//Context context = getApplicationContext();
		final EditText number = (EditText)findViewById(R.id.number);
		number.setSelectAllOnFocus(true);
		number.setInputType(2);
		number.setText(String.valueOf(defaultValue));
		
		Button ok = (Button)findViewById(R.id.ok);
		ok.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedValue = Float.parseFloat(number.getText().toString());
				dismiss();
			}
		});
		
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedValue = null;
				dismiss();
			}
		});
	}
}
