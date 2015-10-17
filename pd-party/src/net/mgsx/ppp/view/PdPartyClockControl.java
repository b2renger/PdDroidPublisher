package net.mgsx.ppp.view;

import net.mgsx.ppp.PdDroidPartyConfig;
import net.mgsx.ppp.R;
import net.mgsx.ppp.midi.MidiManager;
import net.mgsx.ppp.pd.PdClock;

import org.puredata.core.PdBase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PdPartyClockControl extends RelativeLayout 
{
	public static final int midiClockMinBPM = 30; // hard limit

	public static final int midiClockMaxBPM = 300; // hard limit

	public static final int midiClockDefaultBPM = 100; // nominal BPM

	private MidiManager midiManager;

	private ImageButton btStart;
	private ImageButton btReStart;
	private ImageButton btAudio;

	private boolean started;
	private boolean startedOnce;
	private boolean audioOn = false;

	private MidiConfigDialog dialog;

	public PdPartyClockControl(final Activity context, MidiManager midiManager, PdDroidPartyConfig config) {
		super(context);
		this.midiManager = midiManager;
		initGUI(context, config);
	}

	private void resume() {
		started = true;
		if (startedOnce)
			midiManager.resumeClock();
		else
			midiManager.startClock();
		startedOnce = true;
		btStart.setImageResource(R.drawable.ic_action_pause);
	}

	private void start() {
		started = true;
		midiManager.startClock();
		btStart.setImageResource(R.drawable.ic_action_pause);
	}

	private void stop() {
		started = false;
		midiManager.stopClock();
		btStart.setImageResource(R.drawable.ic_action_play);
	}

	@SuppressLint("NewApi")
	private void initGUI(final Activity context, final PdDroidPartyConfig config) {
		this.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));

		LinearLayout main = new LinearLayout(context);
		main.setOrientation(LinearLayout.HORIZONTAL);

		btStart = new ImageButton(context);
		btStart.setImageResource(R.drawable.ic_action_play);
		btStart.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					return v.performClick();
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (started) {
						stop();
					} else {
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
				if (event.getAction() == MotionEvent.ACTION_UP) {
					return v.performClick();
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
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
				if (event.getAction() == MotionEvent.ACTION_UP) {
					return v.performClick();
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (audioOn) {
						audioOn = false;
						btAudio.setImageResource(R.drawable.ic_action_soundoff);
						PdBase.sendFloat(PdClock.AudioClockEnableReceiver, 0);
					} else {
						audioOn = true;
						btAudio.setImageResource(R.drawable.ic_action_soundon);
						PdBase.sendFloat(PdClock.AudioClockEnableReceiver, 1);
					}
				}
				return true;
			}
		});

		final TextView bpmLabel = new TextView(context);
		bpmLabel.setText("BPM : ");
		bpmLabel.setGravity(Gravity.CENTER_VERTICAL);
		bpmLabel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

		final NumberSelector bpmControl = new NumberSelector(context);
		bpmControl.setDigits(3);
		bpmControl.setValuePerInch(30f);
		bpmControl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
		bpmControl.setMax((float)PdPartyClockControl.midiClockMaxBPM);
		bpmControl.setMin((float)PdPartyClockControl.midiClockMinBPM);
		bpmControl.setValue(PdPartyClockControl.midiClockDefaultBPM);

		bpmControl.setOnValueChangedListener(new NumberSelector.OnValueChangeListener() {
			
			@Override
			public void onValueChange(float oldVal, float newVal) {
				midiManager.setBpm((int)newVal);
			}
		});
		
		final TextView offsetLabel = new TextView(context);
		offsetLabel.setText("Delay : ");
		offsetLabel.setGravity(Gravity.CENTER_VERTICAL);
		offsetLabel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		NumberSelector delayControl = new NumberSelector(context);
		delayControl.setDigits(4);
		delayControl.setDecimal(1);
		delayControl.setValuePerInch(30f);
		delayControl.setMin(0f);
		delayControl.setUnit("ms");
		delayControl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
		delayControl.setOnValueChangedListener(new NumberSelector.OnValueChangeListener() {
			
			@Override
			public void onValueChange(float oldVal, float newVal) {
				PdBase.sendFloat(PdClock.ClockDelayReceiver, newVal);
			}
		});
		
		
		RadioGroup rg = (RadioGroup)LayoutInflater.from(getContext()).inflate(R.layout.tempo_scale, null);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				onRadioButtonClicked(checkedId);
			}
		});
		
		main.addView(btStart);
		main.addView(btReStart);
		main.addView(btAudio);
		main.addView(bpmLabel);
		main.addView(bpmControl);
		main.addView(offsetLabel);
		main.addView(delayControl);
		main.addView(rg);
		
		ImageButton btMidiConfig = new ImageButton(context);
		btMidiConfig.setImageResource(R.drawable.ic_action_io);
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
						bpmControl.setEnabled(master);
						dialog = null;
					}
				});
				dialog.show();
			}
		});
		

		RelativeLayout.LayoutParams params;

		addView(main);
		params = (RelativeLayout.LayoutParams) main.getLayoutParams();
		params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		main.setLayoutParams(params);

		
		
		addView(btMidiConfig);
		params = (RelativeLayout.LayoutParams) btMidiConfig.getLayoutParams();
		params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		btMidiConfig.setLayoutParams(params);
		
	}

	public void onRadioButtonClicked(int id) {
	    // Is the button now checked?
	    // boolean checked = ((RadioButton) view).isChecked();

	    // Check which radio button was clicked
	    if(R.id.x1 == id) {
	    	PdClock.setClockDivision(1);
	    } else if(R.id.x2 == id) {
	    	PdClock.setClockDivision(2);
	    } else if(R.id.x3 == id) {
	    	PdClock.setClockDivision(3);
	    } else if(R.id.x4 == id) {
	    	PdClock.setClockDivision(4);
	    } else if(R.id.x6 == id) {
	    	PdClock.setClockDivision(6);
	    } else if(R.id.x8 == id) {
	    	PdClock.setClockDivision(8);
	    } else if(R.id.x1_2 == id) {
	    	PdClock.setClockDivision(0.5f);
	    } else if(R.id.x3_4 == id) {
	    	PdClock.setClockDivision(0.75f);
	    }
	}	
	public void updateMidiConfiguration() {
		if (dialog != null) {
			dialog.refreshInputList();
			dialog.refreshOutputList();
		}
	}

}
