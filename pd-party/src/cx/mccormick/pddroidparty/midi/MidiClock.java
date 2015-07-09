package cx.mccormick.pddroidparty.midi;

import java.util.concurrent.TimeUnit;

public class MidiClock
{
	private volatile boolean shouldSendClock = false;
	private volatile int bpm;
	private volatile long offset;
	private byte[] buffer = new byte[1];
	private BiClockScheduler timer;
	private MidiOutput[] externals = new MidiOutput[]{};
	private MidiOutput[] internals = new MidiOutput[]{};
	
	synchronized public void setInternals(MidiOutput[] array) 
	{
		internals = array;
	}
	synchronized public void setExternals(MidiOutput[] array) 
	{
		externals = array;
	}

	private synchronized void dispatchRealTimeMessage(int message)
	{
		dispatchRealTimeMessageInternal(message);
		dispatchRealTimeMessageExternal(message);
	}
	private synchronized void dispatchRealTimeMessageInternal(int message)
	{
		buffer[0] = (byte)message;
		
		for(MidiOutput output : internals)
		{
			output.send(buffer);
		}
	}
	private synchronized void dispatchRealTimeMessageExternal(int message)
	{
		buffer[0] = (byte)message;
		
		for(MidiOutput output : externals)
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
			
			Runnable commandInternal = new Runnable() {
				@Override
				public void run() {
					
					dispatchRealTimeMessageInternal(MidiCode.MIDI_REALTIME_CLOCK_TICK);
				}
			};
			Runnable commandExternal = new Runnable() {
				@Override
				public void run() {
					
					dispatchRealTimeMessageExternal(MidiCode.MIDI_REALTIME_CLOCK_TICK);
				}
			};
			
			timer = new BiClockScheduler();
			timer.setPeriod(period, TimeUnit.NANOSECONDS);
			timer.start(commandInternal, commandExternal);
			timer.setOffsetA(offset, TimeUnit.NANOSECONDS);
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
	public void setOffset(long nanos) 
	{
		offset = nanos;
		if(timer != null)
		{
			timer.setOffsetA(nanos, TimeUnit.NANOSECONDS);
		}
	}

}
