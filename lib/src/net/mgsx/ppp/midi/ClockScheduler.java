package net.mgsx.ppp.midi;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ClockScheduler
{
	private static class MyDelayed implements Delayed
	{
		private long futureTime;
		
		public MyDelayed() {
			futureTime = System.nanoTime();
		}
		
		public void nextAt(long delay, TimeUnit unit)
		{
			futureTime += unit.toNanos(delay);
		}
		
		@Override
		public int compareTo(Delayed o) {
			return 0;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(futureTime - System.nanoTime(), TimeUnit.NANOSECONDS) ;
		}
		
	}
	
	volatile private boolean shouldRun;
	volatile private long period;
	private Semaphore semaphore;
	
	public void setPeriod(long time, TimeUnit unit)
	{
		period = unit.toNanos(time);
	}
	public void start(final Runnable task)
	{
		semaphore = new Semaphore(0);
		shouldRun = true;
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				try{
					DelayQueue<Delayed> dq = new DelayQueue<Delayed>();
					MyDelayed dl = new MyDelayed();
					
					while(shouldRun)
					{
						task.run();
						dl.nextAt(period, TimeUnit.NANOSECONDS);
						dq.add(dl);
						try {
							dq.take();
						} catch (InterruptedException e) {
							// Thread interruption is fine.
						}
					}
				}finally{
					semaphore.release();
				}
				
			}
		});
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	public void stop()
	{
		shouldRun = false;
	}
	public void stopAndWait() throws InterruptedException 
	{
		stop();
		semaphore.acquire();
	}
}