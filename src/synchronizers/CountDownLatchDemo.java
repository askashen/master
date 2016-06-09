package synchronizers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo {
	final static int NTHREADS = 10;

	public static void main(String[] args) {
		final CountDownLatch startSignal = new CountDownLatch(1);
		final CountDownLatch doneSignal = new CountDownLatch(NTHREADS);
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					report("entered run()");
					startSignal.await();
					report("doing work");
					Thread.sleep((int) (Math.random() * 1000));
					doneSignal.countDown();

				} catch (InterruptedException ie) {
					System.err.println(ie);
				}
			}

			void report(String s) {
				System.out.println(System.currentTimeMillis() + ": " + Thread.currentThread() + ": " + s);
			}
		};
		ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
		for (int i = 0; i < NTHREADS; i++)
			executor.execute(r);
		try {
			System.out.println("main thread doing something");
			Thread.sleep(5000); 
			startSignal.countDown(); 
			System.out.println("main thread doing something else");
			doneSignal.await(); 
			executor.shutdownNow();
		} catch (InterruptedException ie) {
			System.err.println(ie);
		}
	}
}