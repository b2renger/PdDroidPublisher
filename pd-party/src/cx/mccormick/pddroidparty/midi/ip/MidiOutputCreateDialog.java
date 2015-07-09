package cx.mccormick.pddroidparty.midi.ip;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MidiOutputCreateDialog extends Dialog
{
	private IPMidiDevice device;
	
	public MidiOutputCreateDialog(Context context, IPMidiDevice device) {
		super(context);
		this.device = device;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setTitle("IP MIDI Output creation");

		LinearLayout view = new LinearLayout(getContext());
		view.setOrientation(LinearLayout.VERTICAL);
		
		RadioGroup rg = new RadioGroup(getContext());
		
		final RadioButton rbMulticast = new RadioButton(getContext());
		rbMulticast.setText("RAW/Multicast");
		
		final RadioButton rbUnicast = new RadioButton(getContext());
		rbUnicast.setText("RAW/Unicast");
		
		rg.addView(rbUnicast);
		rg.addView(rbMulticast);
		
		rbMulticast.setChecked(true);
		
		final EditText tvIP = new EditText(getContext());
		tvIP.setText("225.0.0.37");
		
		final EditText tvPORT = new EditText(getContext());
		tvPORT.setInputType(InputType.TYPE_CLASS_NUMBER);
		tvPORT.setText("21929");
		
		Button btOk = new Button(getContext());
		btOk.setText("Create");
		
		btOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				IPMidiOutput output = new IPMidiOutput();
				output.multicast = rbMulticast.isChecked();
				output.ip = tvIP.getText().toString();
				output.port = Integer.parseInt(tvPORT.getText().toString());
				device.outputs.add(output);
				MidiOutputCreateDialog.this.dismiss();
			}
		});
		
		view.addView(rg);
		view.addView(tvIP);
		view.addView(tvPORT);
		view.addView(btOk);
		
		setContentView(view);
	}
}
