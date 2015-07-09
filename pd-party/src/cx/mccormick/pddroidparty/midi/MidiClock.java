package cx.mccormick.pddroidparty.midi;

import java.util.concurrent.TimeUnit;

public class MidiClock
{
	private volatile boolean shouldSendClock = false;
	private volatile int bpm;
	private byte[] buffer = new byte[1];
	private ClockScheduler timer;
	private MidiOutput[] outputs = new MidiOutput[]{};
	
	synchronized public void setOutputs(MidiOutput[] array) 
	{
		outputs = array;
	}

	private synchronized void dispatchRealTimeMessage(int message)
	{
		buffer[0] = (byte)message;
		
		for(MidiOutput output : outputs)
		{
			output.send(buffer);
		}
	}
	
	public void start(final int startBpm)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				start(startBpm, true);
			}
		}).start();
	}
	private void start(int startBpm, final boolean sendStartMessage)
	{
		if(!shouldSendClock)
		{
			this.bpm = startBpm;
			shouldSendClock = true;
			
			if(sendStartMessage)
			{
				dispatchRealTimeMessage(MidiCode.MIDI_REALTIME_CLOCK_START);
			}
			else
			{
				dispatchRealTimeMessage(MidiCode.MIDI_REALTIME_CLOCK_RESUME);
			}
			
			long period = 60000000000L / (long)(bpm * 24);
			
			Runnable command = new Runnable() {
				@Override
				public void run() {
					dispatchRealTimeMessage(MidiCode.MIDI_REALTIME_CLOCK_TICK);
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
					
					dispatchRealTimeMessage(MidiCode.MIDI_REALTIME_CLOCK_STOP);
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

	public void resume(final int startBpm) 
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				start(startBpm, false);
			}
		}).start();
	}

	public void init() {

	}

}
