package net.mgsx.ppp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SaveDialog extends Dialog 
{
	private String defaultFilename, selectedFilename;
	
	public SaveDialog(Context context, String defaultFilename) {
		super(context);
		this.defaultFilename = defaultFilename;
	}
	
	public String getSelectedFilename() {
		return selectedFilename;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_dialog);
		
		final EditText filename = (EditText)findViewById(R.id.filename);
		filename.setText(defaultFilename);
		
		Button ok = (Button)findViewById(R.id.ok);
		ok.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String newname = filename.getText().toString();
				if (!newname.isEmpty()) {
					// TODO: check if the file exists and prompt to confirm overwrite first
					selectedFilename = newname;
				}
				dismiss();
			}
		});
		
		Button cancel = (Button)findViewById(R.id.cancel);
		cancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedFilename = null;
				dismiss();
			}
		});
	}
}
