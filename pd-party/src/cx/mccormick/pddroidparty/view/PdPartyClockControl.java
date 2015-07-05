package cx.mccormick.pddroidparty.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.R;
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
	
	private ImageButton btStart;
	private ImageButton btReStart;
	
	private boolean started;
	private boolean startedOnce;

	public PdPartyClockControl(final Activity context, MidiManager midiManager, PdDroidPartyConfig config) 
	{
		super(context);
		this.midiManager = midiManager;
		initGUI(context, config);
	}
	
	private void resume()
	{
		started = true;
		if(startedOnce)
			midiManager.resumeClock();
		else
			midiManager.startClock();
		startedOnce = true;
		btStart.setImageResource(R.drawable.ic_action_pause);
	}
	
	private void start()
	{
		started = true;
		midiManager.startClock();
		btStart.setImageResource(R.drawable.ic_action_pause);
	}
	
	private void stop()
	{
		started = false;
		midiManager.stopClock();
		btStart.setImageResource(R.drawable.ic_action_play);
	}
	
	private void initGUI(final Activity context, final PdDroidPartyConfig config)
	{
		LinearLayout main = this;
		main.setOrientation(LinearLayout.HORIZONTAL);
		
		btStart = new ImageButton(context);
		btStart.setImageResource(R.drawable.ic_action_play);
		btStart.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					return v.performClick();
				}
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					if(started)
					{
						stop();
					}
					else
					{
						resume();
					}
				}
				return true;
			}
		});
		
		btReStart = new ImageButton(context);
		btReStart.setImageResource(R.drawable.ic_action_replay);
		btReStart.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					return v.performClick();
				}
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					start();
				}
				return true;
			}
		});
		
		final TextView bpmLabel = new TextView(context);
		bpmLabel.setText(String.valueOf(config.midiClockDefaultBPM));
		
		final SeekBar slider = new SeekBar(context);
		slider.setMax(config.midiClockMaxBPM - config.midiClockMinBPM);
		slider.setProgress(config.midiClockDefaultBPM);
		
		// TODO 300 is maybe too huge for some devices ...
		slider.setLayoutParams(new ViewGroup.LayoutParams(100, ViewGroup.LayoutParams.WRAP_CONTENT));
		
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
		

		ImageButton btSetup = new ImageButton(context);
		btSetup.setImageResource(R.drawable.ic_action_network_wifi);
		btSetup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent si = new Intent(context, de.humatic.nmj.NMJConfigDialog.class);
				context.startActivityForResult(si, SETUP_ACTIVITY_CODE);
			}
		});
		
		ImageButton btUsb = new ImageButton(context);
		btUsb.setImageResource(R.drawable.ic_action_usb);
		btUsb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				midiManager.usbMidiManager.chooseMidiDevice();
			}
		});
		
		ToggleButton btSlave = new ToggleButton(context);
		btSlave.setButtonDrawable(R.drawable.ic_action_time);
		btSlave.setTextOn("Master");
		btSlave.setTextOff("Slave");
		btSlave.setText(btSlave.getTextOff());
		btSlave.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
			{
				// TODO stop clock if started and state become slave
				btStart.setEnabled(!isChecked);
				btReStart.setEnabled(!isChecked);
				slider.setEnabled(!isChecked);
				midiOutSpinner.setEnabled(!isChecked);
				
				midiInSpinner.setEnabled(isChecked);
			}
		});

		main.addView(btStart);
		main.addView(btReStart);
		main.addView(slider);
		main.addView(bpmLabel);
		main.addView(midiOutSpinner);
		main.addView(btSlave);
		main.addView(midiInSpinner);
		main.addView(btSetup);
		main.addView(btUsb);
		
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
