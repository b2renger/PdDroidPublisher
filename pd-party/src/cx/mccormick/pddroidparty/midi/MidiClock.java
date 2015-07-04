package cx.mccormick.pddroidparty.midi;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.puredata.core.PdBase;

import de.humatic.nmj.NetworkMidiOutput;

public class MidiClock
{
	private volatile boolean shouldSendClock = false;
	private volatile int bpm;
	private NetworkMidiOutput out;
	private byte[] buffer = new byte[1];
	private ScheduledFuture<?> task;
	
	// TODO implements specific executor :
	// schedule 1 tick at a time to handle BPM soft change
	// track time (NANO) to schedule ticks at absolute time
	
	ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) 
		{
			Thread thread = new Thread(r);
			thread.setPriority(Thread.MAX_PRIORITY);
			return thread;
		}
	});
	
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
			
			long period = 60000000 / (bpm * 24);
			
			Runnable command = new Runnable() {
				@Override
				public void run() {
					dispatchRealTimeMessage(out, MidiCode.MIDI_REALTIME_CLOCK_TICK);
				}
			};
			
			task = timer.scheduleAtFixedRate(command, 0, period, TimeUnit.MICROSECONDS);
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
					shouldSendClock = false;
					
					task.cancel(false);
					
					dispatchRealTimeMessage(out, MidiCode.MIDI_REALTIME_CLOCK_STOP);
					
				}
			}).start();
		}
	}
	
	public void setBPM(int value)
	{
		bpm = value;
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
