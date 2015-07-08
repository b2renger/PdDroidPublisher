package cx.mccormick.pddroidparty.midi;

import java.util.concurrent.TimeUnit;

import org.puredata.core.PdBase;

import de.humatic.nmj.NetworkMidiOutput;

public class MidiClock
{
	private volatile boolean shouldSendClock = false;
	private volatile int bpm;
	private NetworkMidiOutput out;
	private byte[] buffer = new byte[1];
	private ClockScheduler timer;
	
	private void dispatchRealTimeMessage(final NetworkMidiOutput out, int message)
	{
		if(out != null) 
		{
			buffer[0] = (byte)message;
			try {
				out.sendMidi(buffer);
			} catch (Exception e) {
				throw new Error(e);
			}
		}
		PdBase.sendSysRealTime(0, message);
	}
	
	public void start(final NetworkMidiOutput out, final int startBpm)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				start(out, startBpm, true);
			}
		}).start();
	}
	private void start(final NetworkMidiOutput out, int startBpm, final boolean sendStartMessage)
	{
		if(!shouldSendClock)
		{
			this.out = out;
			this.bpm = startBpm;
			shouldSendClock = true;
			
			if(sendStartMessage)
			{
				dispatchRealTimeMessage(out, MidiCode.MIDI_REALTIME_CLOCK_START);
			}
			else
			{
				dispatchRealTimeMessage(out, MidiCode.MIDI_REALTIME_CLOCK_RESUME);
			}
			
			long period = 60000000000L / (long)(bpm * 24);
			
			Runnable command = new Runnable() {
				@Override
				public void run() {
					dispatchRealTimeMessage(out, MidiCode.MIDI_REALTIME_CLOCK_TICK);
				}
			};
			
			timer = new ClockScheduler();
			timer.setPeriod(period, TimeUnit.NANOSECONDS);
			timer.start(command);
		}
	}
	
	public void stop()
	{
		if(shouldSendClock)
		{
			new Thread(new Runnable() {
				
				@Override
				public void run() 
				{
					try
					{
					try {
						timer.stopAndWait();
					} catch (InterruptedException e) {
						throw new Error(e);
					}
					
					dispatchRealTimeMessage(out, MidiCode.MIDI_REALTIME_CLOCK_STOP);
					}
					finally{
						shouldSendClock = false;
					}
					
				}
			}).start();
		}
	}
	
	public void setBPM(int value)
	{
		bpm = value;
		
		if(timer != null)
		{
			long period = 60000000000L / (long)(bpm * 24);
			timer.setPeriod(period, TimeUnit.NANOSECONDS);
		}
	}

	public void resume(final NetworkMidiOutput out, final int startBpm) 
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				start(out, startBpm, false);
			}
		}).start();
	}
	
	

}
