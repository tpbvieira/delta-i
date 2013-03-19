package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CompareAddIntSIMDWithForAvg {

	public static void main(String[] args) throws InterruptedException, IOException {
		compareAddIntSIMDWithFor();
	}

	public static void compareAddIntSIMDWithFor() throws IOException{
		int arraySize,k,j,avgSize,maxArraySize;
		long timeSIMD,timeFor,avgTimeSIMD,avgTimeFor,startTotalTime,endTotalTime;
		BufferedWriter writer = new BufferedWriter(new FileWriter(Command.getLocalInfo() + "-compareAddIntSIMDWithForAvg.txt",true)) ;

		avgSize = 11999200;
		maxArraySize = 12000000;

		startTotalTime = System.currentTimeMillis();
		for(arraySize = 400; arraySize < maxArraySize;arraySize+=4){
			int[] a = new int[arraySize];
			int[] b = new int[arraySize];

			for(j = 0; j < arraySize; j++){
				a[j] = 6;
				b[j] = 7;
			}

			timeFor = 0;
			timeSIMD = 0;
			for(k = 0;k<avgSize;k++){
				timeFor+= Command.addUsingFor(a,b);
				timeSIMD+= Command.addUsingSIMD(a,b);
			}

			avgTimeFor = timeFor/avgSize;
			avgTimeSIMD = timeSIMD/avgSize;

			writer.write(arraySize + " " + avgTimeSIMD + " " + avgTimeFor + "\n");
			writer.flush();
		}
		endTotalTime = System.currentTimeMillis();

		writer.write("TestTime = " + (endTotalTime - startTotalTime));
		writer.flush();
		writer.close();
	}
}