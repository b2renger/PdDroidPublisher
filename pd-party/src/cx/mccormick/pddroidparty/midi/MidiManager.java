package cx.mccormick.pddroidparty.midi;

import org.puredata.core.PdBase;

import android.content.Context;
import android.util.Log;
import de.humatic.nmj.NMJConfig;
import de.humatic.nmj.NMJSystemListener;
import de.humatic.nmj.NetworkMidiInput;
import de.humatic.nmj.NetworkMidiListener;
import de.humatic.nmj.NetworkMidiOutput;
import de.humatic.nmj.NetworkMidiSystem;

public class MidiManager  implements NetworkMidiListener, NMJSystemListener
{

	private MidiClock clock;
	private NetworkMidiSystem nmjs;
	private NetworkMidiInput midiIn;
	private NetworkMidiOutput midiOut;
	Context ctx;
	private int bpm = 60; // TODO set by slider initial value ...
	
	public void init(final Context ctx)
	{
		this.ctx = ctx;
        clock = new MidiClock();
 
        /** 
         * Initializing stuff that calls network related methods in an AsyncTask 
         * This is to play nicely with StrictMode in Android API version 11+ and
         * avoids "network on main thread" errors. Can make it a bit more work
         * to have UIs reflect the system's state correctly. In this demo we just build 
         * the UI when the task returns...
         **/
        
        midiSystemLoad(ctx);
	}
	
    private void midiSystemLoad(final Context ctx) {
				
		try {
			nmjs = NetworkMidiSystem.get(ctx);
		} catch (Exception e) {
			throw new Error(e);
		}
		
		/** 
		 * Set a port base that local rtp sessions will have their ports calculated from (long as no
		 * preference has been "hard-set" with setLocalPort(..))
		 * This is to avoid collisions when multiple apps deploying the library on a per app basis
		 * run at the same time. Before publishing anything please request a portbase for your project!
		 */
		
		NMJConfig.setBasePort(NMJConfig.RTP, 6700);
		
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

	@Override
	public void midiReceived(int channel, int ssrc, byte[] data, long timestamp) {
    	
		// TODO check if real time or not ... ?
    	for(byte d : data)
    	{
    		int result = PdBase.sendSysRealTime(0, d & 0xFF);
    		if(result < 0)
    		{
    			System.out.println("no supported message ...");
    		}
    	}
	}

	public void startClock() {
		clock.start(midiOut, bpm); // TODO BPM
	}

	public void stopClock() {
		clock.stop();
	}

	public void setBpm(int bpm) {
		this.bpm = bpm;
		clock.setBPM(bpm);
		
	}

	public void setOut(String name) 
	{
		if(midiOut != null)
		{
			midiOut.close(this);
			midiOut = null;
		}
		for(int i=0 ; i<NMJConfig.getNumChannels() ; i++)
		{
			if(NMJConfig.getName(i).equals(name))
			{
				try {
					midiOut = nmjs.openOutput(i, MidiManager.this);
				} catch (Exception e) {
					e.printStackTrace(); // XXX
				}
				return;
			}
		}
	}

	public void setIn(String name) 
	{
		if(midiIn != null)
		{
			midiIn.close(this);
			midiIn = null;
		}
		if(name != null)
		{
			for(int i=0 ; i<NMJConfig.getNumChannels() ; i++)
			{
				if(NMJConfig.getName(i).equals(name))
				{
					try {
						midiIn = nmjs.openInput(i, MidiManager.this);
					} catch (Exception e) {
						e.printStackTrace(); // XXX
					}
					return;
				}
			}
		}
	}

	public void resumeClock() 
	{
		clock.resume(midiOut, bpm);
	}
}
