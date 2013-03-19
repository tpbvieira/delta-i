package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import simdMapper.NativeLibrary;

public class AddIntegerArrayCThreads2 {

	public static void main(String[] args) throws IOException {		
		BufferedWriter writer = new BufferedWriter(new FileWriter(Command.getLocalInfo() + "-AddIntegerThreads2.txt",true)) ;
		int numThreads=1, numThreadsMax = 11,a[],b[],c[],avg,avgSize = 100;
		long startTime,endTime;
		boolean ok = true;

		int arraySize = 10000000;
		int arraySizeMax = 10000001;		

		//		for (; arraySize < arraySizeMax; arraySize*=10) {
		a = new int[arraySize];
		b = new int[arraySize];
		c = new int[arraySize];

		for (int i = 0; i < arraySize; i++) {
			a[i] = 6;
			b[i] = 7;
			c[i] = 13;
		}

//		for (numThreads = 1; numThreads < numThreadsMax; numThreads++) {
			//				avg = 0;
			//				for (int i = 0; i < avgSize; i++) {
			startTime = System.nanoTime();
			NativeLibrary.addIntegerArrayCThreads2(a, b, 10);
			endTime = System.nanoTime();
			//					avg+= (endTime - startTime);

			// Test if the result is correct
			//			ok = ok && isEqual(a,c);					
			//			for (int j = 0; j < arraySize; j++) {
			//				a[j] = 6;
			//			}
			//				}			
			writer.write(arraySize + " " + 0 + " " + (endTime - startTime) + "\n");
			writer.flush();
//		}			
		//		}
		System.out.println(ok);
		writer.flush();
		writer.close();
	}

	private static boolean isEqual(int[] a, int[] b){
		for (int i = 0; i < b.length; i++) {
			if(a[i] != b[i])
				return false;
		}
		return true;
	}
}