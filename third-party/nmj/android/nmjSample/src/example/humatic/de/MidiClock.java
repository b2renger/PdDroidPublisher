package example.humatic.de;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import de.humatic.nmj.NetworkMidiClient;
import de.humatic.nmj.NetworkMidiOutput;

public class MidiClock implements NetworkMidiClient{

	private volatile boolean shouldSendClock = false;
	private volatile int bpm;
	private Semaphore semaphore = new Semaphore(0);
	
	public void start(final NetworkMidiOutput out, int startBpm)
	{
		if(!shouldSendClock)
		{
			this.bpm = startBpm;
			shouldSendClock = true;
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					
					try {
							
							System.out.println("send MIDI start");
							//out.sendMidi(new byte[]{(byte)0xFC});
							out.sendMidi(new byte[]{(byte)0xFA});
							
							while(shouldSendClock)
							{
								// System.nanoTime()
								
								out.sendMidi(new byte[]{(byte)0xF8});
								
								TimeUnit.MICROSECONDS.sleep(60000000 / (bpm * 24));
							}
							
							System.out.println("send MIDI stop");
							out.sendMidi(new byte[]{(byte)0xFC});

							semaphore.release();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}
	}
	
	public void stop()
	{
		if(shouldSendClock)
		{
			shouldSendClock = false;
			try {
				semaphore.acquire();
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void setBPM(int value)
	{
		bpm = value;
	}
	
	

}
