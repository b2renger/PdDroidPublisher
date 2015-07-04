package cx.mccormick.pddroidparty.midi;

import java.util.concurrent.TimeUnit;

public class ClockSchedulerTest {

	private static long prev;
	
	private static double errorAve = 0;
	private static long tick = 0;
	
	private static final int BUFFER_SIZE = 100;
	private static final double[] BUFFER  = new double [BUFFER_SIZE];
	private static int BUFFER_POS = 0;
	private static int BUFFER_LEN = 0;
	
	public static void main(String[] args) 
	{
		final long DISPLAY_PERIOD = 1000000000L;
		final long base = 10000000L; // 10ms
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				long time = System.nanoTime();
				long delta = time - prev;
				prev = time;
				double error = Math.abs((double)delta / (double)base - 1);
				
				errorAve -= BUFFER[BUFFER_POS];
				BUFFER[BUFFER_POS] = error;
				errorAve += error;
				if(BUFFER_LEN < BUFFER_SIZE) BUFFER_LEN++;
				BUFFER_POS = (BUFFER_POS + 1) % BUFFER_SIZE;
				
				tick++;
				if(tick % (DISPLAY_PERIOD / base) == 0)
					System.out.println(String.format("%.2f", errorAve * 100 / (double)tick));
			}
		};
		
		ClockScheduler cs = new ClockScheduler();
		cs.setPeriod(base, TimeUnit.NANOSECONDS);
		prev = System.nanoTime();
		cs.start(task);
		
	}
}
