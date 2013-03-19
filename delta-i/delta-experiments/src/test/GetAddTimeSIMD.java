package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GetAddTimeSIMD {
	public static void main(String[] args) throws InterruptedException, IOException {
		int arraySize,j,maxArraySize = 10;
		long timeSIMD,startTotalTime,endTotalTime;
		BufferedWriter writer = new BufferedWriter(new FileWriter(Command.getLocalInfo() + "-getAddTimeSIMD.txt",true)) ;

		startTotalTime = System.currentTimeMillis();
		for(arraySize = 1; arraySize <= maxArraySize;arraySize++){
			int[] a = new int[arraySize];
			int[] b = new int[arraySize];

			for(j = 0; j < arraySize; j++){
				a[j] = 6;
				b[j] = 7;
			}

			timeSIMD = Command.addUsingSIMD(a,b);
			writer.write(arraySize + " " + timeSIMD + "\n");
			writer.flush();
		}
		endTotalTime = System.currentTimeMillis();
		
		writer.write("TestTime = " + (endTotalTime - startTotalTime));
		writer.flush();
		writer.close();
	}
}