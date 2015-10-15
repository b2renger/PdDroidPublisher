package cx.mccormick.pddroidparty.midi;

import java.util.concurrent.TimeUnit;

public class MidiClock
{
	private volatile boolean shouldSendClock = false;
	private volatile int bpm;
	private byte[] buffer = new byte[1];
	private byte[] sppBuffer = new byte[]{(byte)MidiCode.SONG_POSITION_POINTER, 0, 0};
	private ClockScheduler timer;
	private MidiOutput[] externals = new MidiOutput[]{};
	private MidiOutput[] internals = new MidiOutput[]{};
	private volatile int ticksCount;
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
	private synchronized void dispatchSPPMessage(int position)
	{
		// LSB
		sppBuffer[1] = (byte)(position & 0x7F);
		// MSB
		sppBuffer[2] = (byte)((position >> 7) & 0x7F);
		
		for(MidiOutput output : externals)
		{
			output.send(sppBuffer);
		}
		for(MidiOutput output : internals)
		{
			output.send(sppBuffer);
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
				// reset counter
				ticksCount = 0;
				
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
					// send SPP to external devices for resync. every 96 ticks.
					ticksCount++;
					if(ticksCount % 96 == 0)
					{
						// convert ticks to MIDI beat (sixteenth note)
						dispatchSPPMessage(ticksCount / 6);
					}
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
