package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import simdMapper.NativeLibrary;

public class ThreadCreationTest {
	public static void main(String[] args) throws IOException {		
		BufferedWriter writer = new BufferedWriter(new FileWriter(Command.getLocalInfo() + "-AddIntegerThreads2.txt",true)) ;
		int numThreads=1,a[],b[],avg,avgSize = 100;
		long startTime,endTime;

		int arraySize = 60000;	

		a = new int[arraySize];
		b = new int[arraySize];

		for (int i = 0; i < arraySize; i++) {
			a[i] = 6;
			b[i] = 7;
		}

		avg = 0;
		numThreads = 10;
		//		for (numThreads = 1; numThreads < 11; numThreads++) {
		startTime = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			
//			NativeLibrary.addIntegerArrayCThreads2(a, b, 10);
							teste(a,b);
		
		}
		endTime = System.currentTimeMillis();
		writer.write(numThreads + " " + (endTime - startTime) + "\n");
		writer.flush();
		//		}
		writer.flush();
		writer.close();
	}

	private static int[] teste(int []a, int[]b){
		int [] c = new int[a.length];

		for (int i = 0; i < c.length; i++) {
			c[i] = a[i] + b[i];
		}

		return c;

	}
}