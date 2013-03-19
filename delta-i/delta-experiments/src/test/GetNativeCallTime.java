package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import simdMapper.NativeLibrary;

public class GetNativeCallTime {
	public static void main(String[] args) throws InterruptedException, IOException {
		nativeCallTime();
	}	

	public static void nativeCallTime() throws InterruptedException, IOException{
		long callTime = 0, startTime = 0, endTime = 0,startTimeTotal = 0, endTimeTotal = 0;
		int sleepTimeToNativeCall = 1, testIndex, testSize, avgSize;
		BufferedWriter writer = new BufferedWriter(new FileWriter(Command.getLocalInfo() + "-nativeCallTime.txt",true)) ;

		testSize = 5;
		avgSize = 100;

		startTimeTotal = System.currentTimeMillis();

		for(testIndex = 0; testIndex < testSize; testIndex++,sleepTimeToNativeCall*=10){//Iteration to calculate time after some sleep time
			for(int i = 0; i < avgSize; i++){//Iteration to calculate avgTime, maxTime, minTime, totalTime to avgSize native call
				startTime = System.nanoTime();
				NativeLibrary.intNativeCall();
				endTime = System.nanoTime();				
				callTime = (endTime - startTime);				
				Thread.sleep(sleepTimeToNativeCall);
				writer.write(callTime + " " + avgSize + " " + sleepTimeToNativeCall + "\n");
				writer.flush();
			}			
		}
		endTimeTotal = System.currentTimeMillis();

		writer.write("TestTime = " + (endTimeTotal - startTimeTotal));
		writer.flush();
		writer.close();

		return;
	}
}