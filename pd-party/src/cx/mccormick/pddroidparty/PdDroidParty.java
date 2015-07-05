package cx.mccormick.pddroidparty;

import java.io.IOException;
import java.util.List;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.service.PdService;
import org.puredata.core.PdBase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import cx.mccormick.pddroidparty.midi.MidiManager;
import cx.mccormick.pddroidparty.midi.UsbMidiHandler;
import cx.mccormick.pddroidparty.midi.UsbMidiManager;
import cx.mccormick.pddroidparty.net.NetworkHelper;
import cx.mccormick.pddroidparty.pd.PdHelper;
import cx.mccormick.pddroidparty.pd.PdParser;
import cx.mccormick.pddroidparty.pd.PdPatch;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.view.PdPartyClockControl;
import cx.mccormick.pddroidparty.widget.LoadSave;
import cx.mccormick.pddroidparty.widget.MenuBang;
import cx.mccormick.pddroidparty.widget.Widget;

public class PdDroidParty extends Activity {
	public PdDroidPatchView patchview = null;
	public static final String INTENT_EXTRA_PATCH_PATH = "PATCH";
	private static final String PD_CLIENT = "PdDroidParty";
	private static final String TAG = "PdDroidParty";
	public static final int DIALOG_NUMBERBOX = 1;
	public static final int DIALOG_SAVE = 2;
	public static final int DIALOG_LOAD = 3;
	
	private UsbMidiManager usbMidiManager;
	private MidiManager midiManager;
	
	private PdPartyClockControl clockControl;
	
	private PdDroidPartyConfig config;
	private PdPatch patch;
	
	private PdService pdService = null;
	Widget widgetpopped = null;
	
	// post a 'toast' alert to the Android UI
	private void post(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), PD_CLIENT + ": " + msg, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	// our connection to the Pd service
	private final ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			pdService = ((PdService.PdBinder) service).getService();
			initPd();
			midiManager.init(PdDroidParty.this, usbMidiManager, config.midiClockDefaultBPM);
			runOnUiThread(new Runnable() {
				public void run() {
					clockControl.initMidiLists();
				}
			});
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			// this method will never be called
		}
	};
	
	// called when the app is launched
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String path = intent.getStringExtra(INTENT_EXTRA_PATCH_PATH);
		patch = new PdPatch(path);
		
		config = (PdDroidPartyConfig)intent.getSerializableExtra(PdDroidPartyConfig.class.getName());
		
		usbMidiManager = new UsbMidiManager(this, new UsbMidiHandler() {
			@Override
			public void onStatusMessage(String message) {
				post(message);
			}
		});
		
		midiManager = new MidiManager();
		
		initGui();
		new Thread() {
			@Override
			public void run() {
				bindService(new Intent(PdDroidParty.this, PdService.class), serviceConnection, BIND_AUTO_CREATE);
			}
		}.start();
	}

	// this callback makes sure that we handle orientation changes without audio glitches
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initGui();
	}

	// When the app shuts down
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cleanup();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	// menu launch yeah
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		// TODO menus are no longer displayed ...
		// add the menu bang menu items
		MenuBang.setMenu(menu);
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO menus are no longer displayed ...
		// pass the menu selection through to the MenuBang manager
		MenuBang.hit(item);
		return super.onOptionsItemSelected(item);
	}
	
	// initialise the GUI with the OpenGL rendering engine
	private void initGui() {
		//setContentView(R.layout.main);
		int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN |
			WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; 		
		getWindow().setFlags(flags, flags);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		patchview = new PdDroidPatchView(this, this, patch);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		clockControl = new PdPartyClockControl(this, midiManager, config);
		
		layout.addView(clockControl);
		layout.addView(patchview);
		
		setContentView(layout);
		patchview.requestFocus();
		MenuBang.clear();
	}
	
	// initialise Pd asking for the desired sample rate, parameters, etc.
	private void initPd() {
		Context context = this.getApplicationContext();
		
		// make sure netreceive can receive broadcast UDP packets
		if(NetworkHelper.aquireWifiMulticast(context))
		{
			Log.i(TAG, "Wifi Multicast enabled");
		}
		else
		{
			Log.w(TAG, "unable to activate Wifi Multicast");
		}
		
		// set up the midi stuff
		usbMidiManager.create();
		
		// set a progress dialog running
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("Loading...");
		progress.setCancelable(false);
		progress.setIndeterminate(true);
		progress.show();
		new Thread() {
			@Override
			public void run() {
				int sRate = AudioParameters.suggestSampleRate();
				Log.d(TAG, "suggested sample rate: " + sRate);
				if (sRate < config.audioSampleRate) {
					Log.e(TAG, "warning: sample rate is only " + sRate);
				}
				// clamp it
				sRate = Math.min(sRate, config.audioSampleRate);
				Log.d(TAG, "actual sample rate: " + sRate);
				
				int nIn = Math.min(AudioParameters.suggestInputChannels(), config.audioInputs);
				Log.d(TAG, "input channels: " + nIn);
				if (nIn == 0) {
					Log.w(TAG, "warning: audio input not available");
				}
				
				int nOut = Math.min(AudioParameters.suggestOutputChannels(), config.audioOutputs);
				Log.d(TAG, "output channels: " + nOut);
				if (nOut == 0) {
					Log.w(TAG, "audio output not available");
				}
				
				Resources res = getResources();
				
				PdHelper.init();
				
				try {
					// parse the patch for GUI elements
					// p.printAtoms(p.parsePatch(path));
					// get the actual lines of atoms from the patch
					List<String[]> atomlines = PdParser.parsePatch(patch);
					// some devices don't have a mic and might be buggy
					// so don't create the audio in unless we really need it
					// TODO: check a config option for this
					//if (!hasADC(atomlines)) {
					//	nIn = 0;
					//}
					// go ahead and intialise the audio
					try {
						pdService.initAudio(sRate, nIn, nOut, -1);   // negative values default to PdService preferences
					} catch (IOException e) {
						Log.e(TAG, e.toString());
						finish();
					}
					patch.open();
					patchview.buildUI(atomlines);
					// start the audio thread
					String name = res.getString(R.string.app_name);
					pdService.startAudio(new Intent(PdDroidParty.this, PdDroidParty.class), R.drawable.icon, name, "Return to " + name + ".");
					// tell the patch view everything has been loaded
					patchview.loaded();
					// dismiss the progress meter
					progress.dismiss();
				} catch (IOException e) {
					post(e.toString() + "; exiting now");
					finish();
				}
			}
		}.start();
	}
	
	// close the app and exit
	@Override
	public void finish() {
		cleanup();
		super.finish();
	}
	
	// quit the Pd service and release other resources
	private void cleanup() {
		// let the screen blank again
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			}
		});
		// make sure to release all resources
		if (pdService != null) {
			pdService.stopAudio();
		}
		patch.close();
		
		PdBase.sendMessage("pd", "quit", "bang");
		PdBase.release();
		try {
			unbindService(serviceConnection);
		} catch (IllegalArgumentException e) {
			// already unbound
			pdService = null;
		}
		
		// release midi
		usbMidiManager.destroy();
		
		// release the lock on wifi multicasting
		NetworkHelper.releaseWifiMulticast();
	}
	
	public void launchDialog(Widget which, int type) {
		widgetpopped = which;
		if (type == DIALOG_NUMBERBOX) {
			Intent it = new Intent(this, NumberboxDialog.class);
			it.putExtra("number", which.getval());
			startActivityForResult(it, DIALOG_NUMBERBOX);
		} else if (type == DIALOG_SAVE) {
			Intent it = new Intent(this, SaveDialog.class);
			it.putExtra("filename", ((LoadSave)which).getFilename());
			it.putExtra("directory", ((LoadSave)which).getDirectory());
			it.putExtra("extension", ((LoadSave)which).getExtension());
			startActivityForResult(it, DIALOG_SAVE);
		} else if (type == DIALOG_LOAD) {
			Intent it = new Intent(this, LoadDialog.class);
			it.putExtra("filename", ((LoadSave)which).getFilename());
			it.putExtra("directory", patch.getFile(((LoadSave)which).getDirectory()).getPath());
			it.putExtra("extension", ((LoadSave)which).getExtension());
			startActivityForResult(it, DIALOG_LOAD);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		if(requestCode == PdPartyClockControl.SETUP_ACTIVITY_CODE)
		{
			clockControl.initMidiLists();
		}
		else if (resultCode == RESULT_OK) {
			if (widgetpopped != null) {
				if (requestCode == DIALOG_NUMBERBOX) {
					widgetpopped.receiveFloat(data.getFloatExtra("number", 0));
					widgetpopped.send("" + widgetpopped.getval());
				} else if (requestCode == DIALOG_SAVE) {
					((LoadSave)widgetpopped).gotFilename("save", data.getStringExtra("filename"));
				} else if (requestCode == DIALOG_LOAD) {
					((LoadSave)widgetpopped).gotFilename("load", data.getStringExtra("filename"));
				}
				// we're done with our originating widget and dialog
				widgetpopped = null;
				// force a redraw
				patchview.invalidate();
			}
		}
	}

}
