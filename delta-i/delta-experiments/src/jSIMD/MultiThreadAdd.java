package jSIMD;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class MultiThreadAdd implements Runnable {

	private int[] results, dataa, datab;
	int lowIndex, highIndex;
	private Semaphore threadMutex;

	public MultiThreadAdd(Semaphore lockAvailableIn, int[] dataaIn, int[] databIn, int[] resultsIn, int lowIndexIn, int highIndexIn)
	{
		this.results = resultsIn;
		this.threadMutex = lockAvailableIn;
		this.dataa=dataaIn;
		this.datab=databIn;
		this.lowIndex=lowIndexIn;
		this.highIndex=highIndexIn;
	}
	
	public void run()
	{
		//copy the subarray out
//		int length = highIndex-lowIndex;
//		int[] sub_results = new int[length];
//		int[] sub_a = new int[length];
//		int[] sub_b = new int[length];
//		
//		System.arraycopy(dataa, lowIndex, sub_a, 0, length);
//		System.arraycopy(datab, lowIndex, sub_b, 0, length);
		//compute results
		for(int i =lowIndex;i<highIndex;i++)
		{
//			results.setElementAt(dataa.elementAt(i)+datab.elementAt(i), i);
			results[i]=dataa[i]+datab[i];
		}	
		
		//copy the subarray back
//		System.arraycopy(sub_results, 0, results, lowIndex, length);
		
		//if(!this.QUIET)System.out.println("currently "+threadMutex.availablePermits()+" available");
		//release mutex and die
		threadMutex.release();
	}
}
