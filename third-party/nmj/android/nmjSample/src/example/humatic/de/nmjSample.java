package example.humatic.de;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import de.humatic.nmj.NMJConfig;
import de.humatic.nmj.NMJSystemListener;
import de.humatic.nmj.NetworkMidiInput;
import de.humatic.nmj.NetworkMidiListener;
import de.humatic.nmj.NetworkMidiOutput;
import de.humatic.nmj.NetworkMidiSystem;

/**
 * Basic nmj sample application for Android.
 * This shows how to initialize the system, create input
 * and output ports, read and write MIDI etc.
 * 
 * You will need to make sure that the nmj.jar file (Android version !!) is accessible 
 * and gets exported. In Eclipse see Project / Properties / Java Build Path / Libraries.
 * 
 * With nmj >= 0.86 the Android API level needs to be set to 12 or greater
 * (in Eclipse / Project / Properties / Android) to build things or you may get compiler errors due to missing USB classes.
 * Deployment is still possible on devices with lower API levels.
 */

public class nmjSample extends Activity implements NetworkMidiListener, NMJSystemListener {

	private NetworkMidiInput midiIn;
	private NetworkMidiOutput midiOut;

	private byte[] myNote = new byte[]{(byte)0x90, (byte)0x24, 0};

	private MidiLogger midiLogger;

	private NetworkMidiSystem nmjs;
	
	private MidiClock clock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        clock = new MidiClock();
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	if (prefs.getLong("firstRun", -1) == -1) {
        	
    		try {
				
    			/** 
	        	 * Configure another channel on top of the default 3.
	        	 * This is just to demonstrate the basic principle of creating a default configuration
	        	 * after installation and is not bound to adb channels: Channels are all virtual and only defined by
	        	 * preferences set and read via methods in NMJConfig. 
	        	 */
    			
    			NMJConfig.edit(this, true);
	
	        	int chIdx = NMJConfig.addChannel();
	        	
	        	/** Only really need to set the mode. Other parameters will use mode
	        	 * dependent default values unless you have "hard set" your own preferences
	        	 * for a given channel.
	        	 * If no mode is set, newly created channels will repeat the multicast out / multicast
	        	 * in / RTP pattern.
	        	*/
	        	NMJConfig.setMode(chIdx, NMJConfig.ADB); 
	        	     	
	        	SharedPreferences.Editor editor = prefs.edit();
	        	editor.putLong("firstRun", System.currentTimeMillis());
	        	editor.commit();
    		
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
 
        
        /** 
         * Initializing stuff that calls network related methods in an AsyncTask 
         * This is to play nicely with StrictMode in Android API version 11+ and
         * avoids "network on main thread" errors. Can make it a bit more work
         * to have UIs reflect the system's state correctly. In this demo we just build 
         * the UI when the task returns...
         **/
        
        midiSystemLoad(this);
        
    }
    
    private void buildUI() {
    	
    	NMJConfig.addSystemListener(this); 
    	   
    	setContentView(R.layout.main);

        final Button button = (Button) findViewById(R.id.Button01);

        /** Can't use "this" in the below event handler. Quick and dirty demo code, don't copy this... */
        final NetworkMidiListener ml = this;

        Spinner spinner = (Spinner) findViewById(example.humatic.de.R.id.Spinner01);
        int numCh = NMJConfig.getNumChannels();
	    String[] channelArray = new String[numCh];
	    for (int i = 0; i < numCh; i++) channelArray[i] = NMJConfig.getName(i);
	    ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, channelArray);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(
		    new OnItemSelectedListener() {
		        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
		        	button.setEnabled(true);
		        	/** To also avoid doing network related stuff on the main thread here **/
		        	Runnable r = new Runnable() {
		        		public void run() {
						     /** using null instead of a specific client or listener removes all 
				        	 * eventually attached clients and closes the port. if you
				        	 * remove listeners separately the port will be closed when the
				        	 * last client is detached
				        	 */
		        			clock.stop();
		        			
				        	try{ midiIn.close(null); } catch (NullPointerException ne){}
				        	try{ midiOut.close(null); } catch (NullPointerException ne){}
				        	
			        	   	try{
				        		midiIn = nmjs.openInput(position, ml);
				        	} catch (Exception e){/** channel probably is output only */
				        		Log.i("nmjSample", e.getMessage());
				        	}
				            try{
				        		midiOut = nmjs.openOutput(position, ml);
				        	} catch (Exception e){
				        		/** channel is input only, disable send button. Must be done on main thread. */
				        		Message msg = Message.obtain();
				        		msg.what = 1;
				        		midiLogger.sendMessage(msg);
				        	}
		        		}
		        	};
		        	new Thread(r).start();
		        }

		        public void onNothingSelected(AdapterView<?> parent) {}
		    });
  
	    button.setOnTouchListener(new View.OnTouchListener() {
           public boolean onTouch(View v, MotionEvent me) {
            	try{
            		/** Uses NetworkMIDIOutput.sendMIDIOnThread to avoid "network on main
            		 * thread" complaints. The method uses a simple wait / notify mechanism and
            		 * should fire messages without any noticeable delay. If you want more control
            		 * over this, implement your own nonUI thread and use sendMidi(..) from there instead. 
            		 * Alternatively ignore Android's "fluid UI first" paradigm and either disable StrictMode or
            		 * soften the penalty for "network on main thread". After all there may be good 
            		 * reasons to prefer having MIDI sent directly on a UI event.
            		 */
            		if (me.getAction() == MotionEvent.ACTION_DOWN) {
               			myNote[2] = (byte)100;
                		midiOut.sendMidiOnThread(myNote);
                    } else if (me.getAction() == MotionEvent.ACTION_UP) {
                    	myNote[2] = 0;
                		midiOut.sendMidiOnThread(myNote);
                    }
                } catch (Exception ex){
                	ex.printStackTrace();
                }
            	return true;
           	}
         });
	    
	    final SeekBar slider = (SeekBar)findViewById(R.id.BPMSlider);
	    
	    Button startButton = (Button) findViewById(R.id.ButtonStart);
	    startButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int bpm = (int)((float)slider.getProgress() * (240 - 60) + 60);
				clock.start(midiOut, bpm);
			}
		});
	    
	    Button stopButton = (Button) findViewById(R.id.ButtonStop);
	    stopButton.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clock.stop();
			}
		});
	    
	    slider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int bpm = (int)((float)progress / 100 * (240 - 60) + 60);
				clock.setBPM(bpm);
			}
		});
 
	    final TextView tv = (TextView) findViewById(R.id.TextView01);

        midiLogger = new MidiLogger(tv);

    } 
    
    @Override  
    public void onDestroy() {
    	super.onDestroy();
    	try{
    		/** The exit code takes care of leaving the main thread internally **/
    		nmjs.exit();
    		
    		/** 
    		 * This removes all remote channels with an index higher than the first argument
    		 * from the configuration, cleaning up eventually discovered services. The first
			 * argument will normally be set to an app's highest default channel index, ie: 
			 * the number of channels it defined itself and expects to find on every run -1. 
    		 * If your app has opened any remote channels, you can pass their IDs 
    		 * in the int[] argument. They will then be kept but may be relocated.
    		 * The method would return an int[] of equal length with the new channel IDs,
    		 * so you can update your storage. 
    		 * cleanup() only operates on SharedPreferences, no network on main thread trouble to be expected.
    		 */
    		NMJConfig.cleanup(3, null);
    		
    	} catch (Exception e){e.printStackTrace();}
    }
     
    public boolean onCreateOptionsMenu(Menu menu) { 
    	MenuInflater inflater = getMenuInflater(); 
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.item01:
    			/**
				 * Starts nmj's control panel activity. This will not return anything meaningful even
				 * if it was started with startActivityForResult, but NMJConfig will reflect
				 * changes made. 
				 * The control panel is for developer use only. Do not show it to users...
    			 */
    			final Intent si = new Intent((Context)this, de.humatic.nmj.NMJConfigDialog.class);
    			startActivity(si);
    			return true;
    		case R.id.item02:
    			/** Get out of here. The MIDI system will be torn down in onDestroy() **/
    			finish();
    			return true;  
    		default:
    			return super.onOptionsItemSelected(item);  
		}
    }

    private void midiSystemLoad(final Context ctx) {
    	AsyncTask at = new AsyncTask() {
      
			@Override
			protected Object doInBackground(Object... params) {
				
				try {
				
					nmjs = NetworkMidiSystem.get(ctx);
					
					/** 
					 * Set a port base that local rtp sessions will have their ports calculated from (long as no
					 * preference has been "hard-set" with setLocalPort(..))
					 * This is to avoid collisions when multiple apps deploying the library on a per app basis
					 * run at the same time. Before publishing anything please request a portbase for your project!
					 */
					
					NMJConfig.setBasePort(NMJConfig.RTP, 6700);
					
					return nmjs;  
					
				} catch (Exception e) {
					e.printStackTrace();    
				}         
				return null;            
			}                            
    	      	            
			@Override
			protected void onPostExecute(Object result) {       
				 buildUI();  
	    	}
	    	protected void onProgressUpdate(Integer... prg) { 
	    		
	        }                  
	     };
	     at.execute(new Object[1]); 
    }
    
    
	@Override
	public void midiReceived(int channel, int ssrc, byte[] data, long timestamp) {
		/**
		 * As MIDI does not come in on the UI thread, it needs to be off-loaded in
		 * order to be displayed. Android's Handler class is one way to do this.
		 * Note: If you need to work with large numbers of simultaneous MIDI events 
		 * or high rate streams, consider copying the incoming data into your own 
		 * arrays before sending them via Handlers. nmj will internally reuse memory
		 * and you may risk having data overwritten before you see it appearing in
		 * Handler.handleMessage() otherwise.
		*/ 
		Message msg = Message.obtain();
		msg.what = 0;
    	Bundle b = new Bundle();
    	b.putByteArray("MIDI", data);
    	b.putInt("CH", channel);
    	msg.setData(b);

    	midiLogger.sendMessage(msg);

	}

	private class MidiLogger extends android.os.Handler {

    	private StringBuffer sb = new StringBuffer();
    	private TextView tv;

    	private MidiLogger(TextView tv) {
    		super();
    		this.tv = tv;
    	}

    	public void handleMessage(android.os.Message msg) { 
    		switch (msg.what) {
	    		case 0:
	    			Bundle b = msg.getData();
		    		sb.delete(0, sb.length()); 
		    		byte[] data = b.getByteArray("MIDI");
		    		
		    		sb.append("MIDI received: ");
		    		for (int i = 0; i < data.length; i++) sb.append(String.format("%X", (data[i] & 0xFF))+" ");
		    		sb.append("\n");
		    		tv.setText(sb.toString());
		    		break;
	    		case 1:
	    			findViewById(R.id.Button01).setEnabled(false);
	    			break;
	    		
    		}
    	}
    }
  
	@Override
	public void systemChanged(int channel, int property, int value) {

		Log.i("nmjSample", " System changed "+channel+" "+property+" "+value);

		if (property == NMJConfig.CH_STATE && value == NMJConfig.RTPA_CH_DISCOVERED) {
			/**
			 * Given multicast works on your device then DNS might 
			 * uncover more RTP and MWS channels and call this for notification. 
			 * Newly found USB host & ADB channels will also be announced here.
			 * Time to update the spinner, which is not done in this sample 
			 * (added channels can be seen in the control panel activity, though). 
			 */
		}

	} 

	@Override
	public void systemError(int channel, int err, String description) {
		
		/** 
		 * Make sure to override this method to be informed about eventual errors on
		 * lower levels. Much of what the library does runs on port specific threads that
		 * nmj can not throw exception that would be catchable in application code 
		 * from.    
		 */
		Log.e("nmjSample", " Error on ch "+channel+", code: "+err+", desc: "+description);
		
	}
}