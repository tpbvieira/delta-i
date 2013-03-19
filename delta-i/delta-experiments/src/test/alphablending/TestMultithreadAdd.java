package test.alphablending;

import jSIMD.MultiThreadAdd;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class TestMultithreadAdd {

	private static final int BLOCK_SIZE = 2;
	private static final int NUM_THREADS = 4;
	private static Semaphore threadMutex = new Semaphore(NUM_THREADS);
	private static int[] results = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, dataa = {
			0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, datab = { 0, 1, 2, 3, 4, 5, 6, 7,
			8, 9 };
	static ThreadGroup workers = new ThreadGroup("Workers");

	public static void main(String[] args) {
		// assign work to any available thread until no more
		// threads or no more work
		long startTime = System.currentTimeMillis();
		for (int j = 0; j < 1000; j++) {
			int startIndex = 0;
			int extra = results.length % BLOCK_SIZE == 0 ? 0 : 1;
			int totalLength = ((results.length / BLOCK_SIZE) + extra);
			for (int blocks = 0; blocks < totalLength; blocks++) 
			{
				int endIndex = startIndex + BLOCK_SIZE;
				if (endIndex > results.length)
					endIndex = results.length;
				try {
					// System.out.println("Active threads = "+workers.activeCount());
					threadMutex.acquire();
//					MultiThreadAdd grunt = new MultiThreadAdd(threadMutex,
//							dataa, datab, results, startIndex, endIndex);
//					new Thread(workers, grunt).start();
				} catch (InterruptedException e) {
					System.out.println("FAILED TO GET THREAD GOING!!");
					e.printStackTrace();
				}
				startIndex += BLOCK_SIZE;
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Exec time (ms) = " + (endTime - startTime));
		for (int i : results) {
			System.out.print(i + " ");
		}
	}
}
