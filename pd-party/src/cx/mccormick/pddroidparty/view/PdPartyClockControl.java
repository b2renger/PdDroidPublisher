package cx.mccormick.pddroidparty.view;

import org.puredata.core.PdBase;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import cx.mccormick.pddroidparty.PdDroidPartyConfig;
import cx.mccormick.pddroidparty.R;
import cx.mccormick.pddroidparty.midi.MidiManager;

public class PdPartyClockControl extends RelativeLayout
{
	public static final int SETUP_ACTIVITY_CODE = 666;
	
	private MidiManager midiManager;
	
	private ImageButton btStart;
	private ImageButton btReStart;
	private ImageButton btAudio;
	
	private boolean started;
	private boolean startedOnce;
	private boolean audioOn = false;
	
	private MidiConfigDialog dialog;

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
		//PdBase.sendFloat(recv, x)
	}
	
	private void stop()
	{
		started = false;
		midiManager.stopClock();
		btStart.setImageResource(R.drawable.ic_action_play);
	}
	
	private void initGUI(final Activity context, final PdDroidPartyConfig config)
	{
		this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
		
		LinearLayout main = new LinearLayout(context);
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
		
		btAudio = new ImageButton(context);
		btAudio.setImageResource(R.drawable.ic_action_soundoff);
		btAudio.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					return v.performClick();
				}
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{				
					if (audioOn){
						audioOn = false;
						btAudio.setImageResource(R.drawable.ic_action_soundoff);
						PdBase.sendFloat("audioon.s", 0);			
					}
					else {
						audioOn = true;
						btAudio.setImageResource(R.drawable.ic_action_soundon);
						PdBase.sendFloat("audioon.s", 1);
					}
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
				PdBase.sendFloat("localbpm.s", bpm);
				PdBase.sendFloat("localbpm.r", bpm);// mimic in the gui abstraction.
			}
		});
		
		final TextView offsetLabel = new TextView(context);
		offsetLabel.setText("0ms");
		
		final SeekBar offsetSlider = new SeekBar(context);
		offsetSlider.setMax(100);
		offsetSlider.setProgress(50);
		
		// TODO 300 is maybe too huge for some devices ...
		offsetSlider.setLayoutParams(new ViewGroup.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		offsetSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int value = (progress - 50) * 10;
				midiManager.setOffsetMs(value);
				offsetLabel.setText(String.valueOf(value) + "ms"); 
			}
		});
		
		

		
		main.addView(btStart);
		main.addView(btReStart);
		main.addView(btAudio);
		main.addView(slider);
		main.addView(bpmLabel);
		main.addView(offsetSlider);
		main.addView(offsetLabel);
		
		ImageButton btMidiConfig = new ImageButton(context);
		btMidiConfig.setImageResource(R.drawable.ic_action_time);
		btMidiConfig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = new MidiConfigDialog(context, midiManager);
				dialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						boolean master = midiManager.getInput() == null;
						btStart.setEnabled(master);
						btReStart.setEnabled(master);
						slider.setEnabled(master);
						dialog = null;
					}
				});
				dialog.show();
			}
		});
		
		RelativeLayout.LayoutParams params;
		
		addView(main);
		params = (RelativeLayout.LayoutParams)main.getLayoutParams();
		params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		main.setLayoutParams(params);
		
		addView(btMidiConfig);
		params = (RelativeLayout.LayoutParams)btMidiConfig.getLayoutParams();
		params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		btMidiConfig.setLayoutParams(params);
		
	}

	public void updateMidiConfiguration() 
	{
		if(dialog != null)
		{
			dialog.refreshInputList();
			dialog.refreshOutputList();
		}
	}
	
}
