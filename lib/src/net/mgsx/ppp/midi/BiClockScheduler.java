package net.mgsx.ppp.midi;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class BiClockScheduler
{
	private static class MyDelayed implements Delayed
	{
		private long oldOffset;
		private long offset;
		private long futureTime;
		private Runnable task;
		
		public MyDelayed(Runnable task) {
			futureTime = System.nanoTime();
			this.task = task;
		}
		
		public void nextAt(long delay, TimeUnit unit)
		{
			futureTime += unit.toNanos(delay) + offset - oldOffset;
			oldOffset = offset;
		}
		
		@Override
		public int compareTo(Delayed o) {
			return Long.compare(futureTime, ((MyDelayed)o).futureTime);
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(futureTime - System.nanoTime(), TimeUnit.NANOSECONDS) ;
		}

		public void offset(long time, TimeUnit unit) 
		{
			offset = unit.toNanos(time);
		}
		
	}
	
	volatile private boolean shouldRun;
	volatile private long period;
	private Semaphore semaphore;
	private MyDelayed dlA, dlB;
	
	public void setPeriod(long time, TimeUnit unit)
	{
		period = unit.toNanos(time);
	}
	
	public void setOffsetA(long time, TimeUnit unit)
	{
		dlA.offset(time, unit);
	}
	public void setOffsetB(long time, TimeUnit unit)
	{
		dlB.offset(time, unit);
	}
	
	public void start(final Runnable taskA, final Runnable taskB)
	{
		dlA = new MyDelayed(taskA);
		dlB = new MyDelayed(taskB);

		semaphore = new Semaphore(0);
		shouldRun = true;
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() 
			{
				try{
					DelayQueue<MyDelayed> dq = new DelayQueue<MyDelayed>();
					
					dlA.nextAt(period, TimeUnit.NANOSECONDS);
					dq.add(dlA);
					dlB.nextAt(period, TimeUnit.NANOSECONDS);
					dq.add(dlB);
					
					while(shouldRun)
					{
						try {
							MyDelayed dl = dq.take();
							dl.task.run();
							dl.nextAt(period, TimeUnit.NANOSECONDS);
							dq.add(dl);
						} catch (InterruptedException e) {
							throw new Error(e);
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