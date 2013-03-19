package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import simdMapper.NativeLibrary;
public class AddIntegerThread {

	public static void main(String[] args) throws IOException {

		int numThreads, numThreadsMax = 100;
		
		int arraySize = 10;		
		int arraySizeMax = 10000000;
		
		long startTime,endTime;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(Command.getLocalInfo() + "-AddIntegerThreads.txt",true)) ;
		
		for (; arraySize < arraySizeMax; arraySize*=10) {System.out.println("ArraySize = " + arraySize);
			numThreads = 1;
			for (; numThreads < numThreadsMax; numThreads++) {System.out.println("NumThreads = " + numThreads);
				startTime = System.nanoTime();
				NativeLibrary.addIntegerCThreads(numThreads,arraySize);
				endTime = System.nanoTime();
				writer.write(arraySize + " " + numThreads + " " + (endTime - startTime) + "\n");
				writer.flush();
			}
		}
		
		writer.flush();
		writer.close();
	}
}