package cx.mccormick.pddroidparty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.Toast;
import cx.mccormick.pddroidparty.midi.MidiManager;
import cx.mccormick.pddroidparty.midi.UsbMidiHandler;
import cx.mccormick.pddroidparty.midi.UsbMidiManager;
import cx.mccormick.pddroidparty.net.NetworkHelper;
import cx.mccormick.pddroidparty.pd.PdHelper;
import cx.mccormick.pddroidparty.pd.PdParser;
import cx.mccormick.pddroidparty.pd.PdPatch;
import cx.mccormick.pddroidparty.util.FileHelper;
import cx.mccormick.pddroidparty.view.PdDroidPatchView;
import cx.mccormick.pddroidparty.view.PdPartyClockControl;
import cx.mccormick.pddroidparty.widget.Widget;
import cx.mccormick.pddroidparty.widget.abs.LoadSave;

public class PdDroidParty extends Activity {
	public List<PdDroidPatchView> patchviews = new ArrayList<PdDroidPatchView>();
	public static final String INTENT_EXTRA_PATCH_PATH = "PATCH";
	private static final String PD_CLIENT = "PdDroidParty";
	private static final String TAG = "PdDroidParty";
	private TabHost stack;
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
			midiManager.init(PdDroidParty.this, usbMidiManager, PdDroidPartyConfig.midiClockDefaultBPM);
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
		
		// copy necessary abstractions
        FileHelper.unzipResource(this, R.raw.abstractions, patch.getFile().getParentFile());

		
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
	
	public static TabHost createTabHost(Context context) {
	    // Create the TabWidget (the tabs)
	    TabWidget tabWidget = new TabWidget(context);
	    tabWidget.setId(android.R.id.tabs);

	    // Create the FrameLayout (the content area)
	    FrameLayout frame = new FrameLayout(context);
	    frame.setId(android.R.id.tabcontent);
	    LinearLayout.LayoutParams frameLayoutParams = new LinearLayout.LayoutParams(
	        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
	    frameLayoutParams.setMargins(4, 4, 4, 4);
	    frame.setLayoutParams(frameLayoutParams);

	    // Create the container for the above widgets
	    LinearLayout tabHostLayout = new LinearLayout(context);
	    tabHostLayout.setOrientation(LinearLayout.VERTICAL);
	    tabHostLayout.addView(tabWidget);
	    tabHostLayout.addView(frame);

	    // Create the TabHost and add the container to it.
	    TabHost tabHost = new TabHost(context, null);
	    tabHost.addView(tabHostLayout);
	    tabHost.setup();

	    return tabHost;
	}
	
	// initialise the GUI with the OpenGL rendering engine
	private void initGui() {
		//setContentView(R.layout.main);
		int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN |
			WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; 		
		getWindow().setFlags(flags, flags);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		clockControl = new PdPartyClockControl(this, midiManager, config);
		
		layout.addView(clockControl);

		if(config.guiPatches.isEmpty())
		{
			PdDroidPatchView patchview = new PdDroidPatchView(this, patch, config);
			patchviews.add(patchview);
			layout.addView(patchview);
			setContentView(layout);
			patchview.requestFocus();
		}
		else
		{
			stack = createTabHost(this);
			
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			stack.setLayoutParams(params);
			
			for(Entry<String, String> entry : config.guiPatches.entrySet())
			{
				PdPatch patch = new PdPatch(new File(this.patch.getFile().getParentFile().getParentFile(), entry.getValue()).getAbsolutePath());
				final PdDroidPatchView patchview = new PdDroidPatchView(this, patch, config);
				patchviews.add(patchview);
				
				TabSpec sp = stack.newTabSpec(entry.getValue());
				sp.setIndicator(entry.getKey(), getResources().getDrawable(R.drawable.ic_action_usb));
				sp.setContent(new TabContentFactory() {
					
					@Override
					public View createTabContent(String tag) {
						return patchview;
					}
				});
				stack.addTab(sp);
			}
			
			layout.addView(stack);
			
			setContentView(layout);
			
			stack.requestFocus();
		}
		
		
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
					pdService.initAudio(sRate, nIn, nOut, -1);   // negative values default to PdService preferences
				} catch (IOException e) {
					Log.e(TAG, e.toString());
					finish();
				}
				
				for(PdDroidPatchView patchview : patchviews)
				{
					PdPatch patch = patchview.getPatch();
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
						patch.open();
						patchview.buildUI(atomlines);
						// tell the patch view everything has been loaded
						patchview.loaded();
						
					} catch (IOException e) {
						post(e.toString() + "; exiting now");
						finish();
					}
				}
				
				// load core patches if any
				for(String path : config.corePatches)
				{
					PdPatch patch = new PdPatch(new File(PdDroidParty.this.patch.getFile().getParentFile().getParentFile(), path).getAbsolutePath());
					List<String[]> atomlines = PdParser.parsePatch(patch);
					for (String[] line: atomlines) {
						if (line.length >= 5) {
							if (line[4].equals("loadsave")) {
								 // XXX first one
								new LoadSave(patchviews.iterator().next(), line);
							}
						}
					}
					try {
						patch.open();
					} catch (IOException e) {
						throw new Error(e);
					}
				}
				
				// start the audio thread
				String name = res.getString(R.string.app_name);
				pdService.startAudio(new Intent(PdDroidParty.this, PdDroidParty.class), R.drawable.icon, name, "Return to " + name + ".");
				// dismiss the progress meter
				progress.dismiss();
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
		for(PdDroidPatchView view : patchviews)
		{
			view.getPatch().close();
		}
		
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
	
}
