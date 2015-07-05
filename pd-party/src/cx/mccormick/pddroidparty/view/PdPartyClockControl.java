package cx.mccormick.pddroidparty.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.midi.MidiManager;
import de.humatic.nmj.NMJConfig;

public class PdPartyClockControl extends LinearLayout 
{
	public static final int SETUP_ACTIVITY_CODE = 666;
	
	private Spinner midiOutSpinner;
	private Spinner midiInSpinner;

	private ArrayAdapter<String> midiOutArrayList;
	private ArrayAdapter<String> midiInArrayList;

	private MidiManager midiManager;
	
	public PdPartyClockControl(final Activity context, MidiManager midiManager, PdDroidPartyConfig config) 
	{
		super(context);
		this.midiManager = midiManager;
		initGUI(context, config);
	}
	
	private void initGUI(final Activity context, final PdDroidPartyConfig config)
	{
		LinearLayout main = this;
		main.setOrientation(LinearLayout.HORIZONTAL);
		
		final LinearLayout masterLayout = new LinearLayout(context);
		masterLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		final LinearLayout slaveLayout = new LinearLayout(context);
		slaveLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		Button btStart = new Button(context);
		btStart.setText("Start");
		btStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				midiManager.startClock();
			}
		});
		
		Button btStop = new Button(context);
		btStop.setText("Stop");
		btStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				midiManager.stopClock();
			}
		});
		
		Button btResume = new Button(context);
		btResume.setText("Resume");
		btResume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				midiManager.resumeClock();
			}
		});
		
		final TextView bpmLabel = new TextView(context);
		bpmLabel.setText(String.valueOf(config.midiClockDefaultBPM));
		
		SeekBar slider = new SeekBar(context);
		slider.setMax(config.midiClockMaxBPM - config.midiClockMinBPM);
		slider.setProgress(config.midiClockDefaultBPM);
		
		// TODO 300 is maybe too huge for some devices ...
		slider.setLayoutParams(new ViewGroup.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int bpm = progress + config.midiClockMinBPM;
				midiManager.setBpm(bpm);
				bpmLabel.setText(String.valueOf(bpm)); 
			}
		});
		
		ToggleButton btSlave = new ToggleButton(context);
		btSlave.setTextOn("Slave");
		btSlave.setTextOff("Master");
		btSlave.setText(btSlave.getTextOff());
		btSlave.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				for(int i=0 ; i<masterLayout.getChildCount() ; i++)
				{
					masterLayout.getChildAt(i).setEnabled(!isChecked);
				}
				for(int i=0 ; i<slaveLayout.getChildCount() ; i++)
				{
					slaveLayout.getChildAt(i).setEnabled(isChecked);
				}
			}
		});
		
		midiOutArrayList = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new ArrayList<String>());

		midiOutSpinner = createSpinner(context, midiOutArrayList);
		midiOutSpinner.setPrompt("Output...");
		midiOutSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				midiManager.setOut(midiOutArrayList.getItem(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
			}
		});
		
		midiInArrayList = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, new ArrayList<String>());
		midiInSpinner = createSpinner(context, midiInArrayList);
		midiInSpinner.setEnabled(false);
		midiInSpinner.setPrompt("Input...");
		midiInSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				midiManager.setIn(midiInArrayList.getItem(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
			}
		});
		

		Button btSetup = new Button(context);
		btSetup.setText("Setup");
		btSetup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent si = new Intent(context, de.humatic.nmj.NMJConfigDialog.class);
				context.startActivityForResult(si, SETUP_ACTIVITY_CODE);
			}
		});
		

		
		masterLayout.addView(btStart);
		masterLayout.addView(btStop);
		masterLayout.addView(btResume);
		masterLayout.addView(slider);
		masterLayout.addView(bpmLabel);
		
		masterLayout.addView(midiOutSpinner);

		slaveLayout.addView(midiInSpinner);

		LinearLayout msLayout = new LinearLayout(context);
		msLayout.setOrientation(LinearLayout.VERTICAL);
		msLayout.addView(masterLayout);
		msLayout.addView(slaveLayout);
		
		LinearLayout ctLayout = new LinearLayout(context);
		ctLayout.setOrientation(LinearLayout.VERTICAL);
		ctLayout.addView(btSlave);
		ctLayout.addView(btSetup);
		
		main.addView(ctLayout);
		main.addView(msLayout);
		
	}
	
	private Spinner createSpinner(Context context, ArrayAdapter<String> adapter)
	{
		Spinner spinner = new Spinner(context, Spinner.MODE_DIALOG);
		spinner.setAdapter(adapter);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return spinner;
	}
	
	public void initMidiLists() 
	{
		midiManager.setOut(null);
		midiManager.setIn(null);
		midiInArrayList.clear();
		midiOutArrayList.clear();
		
		midiInArrayList.add("");
		midiOutArrayList.add("");
		
		for(int i=0 ; i<NMJConfig.getNumChannels() ; i++)
		{
			int io = NMJConfig.getIO(i);
			String name = NMJConfig.getName(i);
			// input
			if(io == 0 || io == -1)
			{
				midiInArrayList.add(name);
			}
			// output
			if(io == 1 || io == -1)
			{
				midiOutArrayList.add(name);
			}
		}
		midiInSpinner.setPrompt("Select Midi In");
		midiOutSpinner.setPrompt("Select Midi Out");
	}


}
