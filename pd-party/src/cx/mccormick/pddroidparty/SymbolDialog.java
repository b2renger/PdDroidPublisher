package cx.mccormick.pddroidparty;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SymbolDialog extends Dialog 
{
	private String defaultValue;
	private String selectedValue;
	
	public SymbolDialog(Context context, String defaultValue) {
		super(context);
		this.defaultValue = defaultValue;
	}
	
	public String getSelectedValue() {
		return selectedValue;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.symbol_dialog);
		
		final EditText number = (EditText)findViewById(R.id.number);
		number.setSelectAllOnFocus(true);
		number.setInputType(InputType.TYPE_CLASS_TEXT);
		number.setText(String.valueOf(defaultValue));
		
		Button ok = (Button)findViewById(R.id.ok);
		ok.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedValue = number.getText().toString();
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
