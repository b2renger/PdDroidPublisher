package cx.mccormick.pddroidparty.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClockSettingsDialog extends Dialog
{
	

	public ClockSettingsDialog(Context context) 
	{
		super(context);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setTitle("Clock Settings");
		
		LinearLayout view = new LinearLayout(getContext());
		view.setOrientation(LinearLayout.VERTICAL);
		Button button = new Button(getContext());
		button.setText("Ok");
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ClockSettingsDialog.this.dismiss();
			}
		});
		
		final TextView inLabel = new TextView(getContext());
		inLabel.setText("Clock Settings");

		
		

		LinearLayout inLayout = new LinearLayout(getContext());
		
		inLayout.addView(inLabel);
		view.addView(inLayout);
		
		setContentView(view);
	}
	
	
}
