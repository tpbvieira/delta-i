package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CompareAddIntSIMDWithFor {

	public static void main(String[] args) throws InterruptedException, IOException {
		compareAddIntSIMDWithFor();
	}

	public static void compareAddIntSIMDWithFor() throws IOException{
		int arraySize,j,maxArraySize;
		long timeSIMD,timeFor,startTotalTime,endTotalTime;
		BufferedWriter writer = new BufferedWriter(new FileWriter(Command.getLocalInfo() + "-compareAddIntSIMDWithFor.txt",true)) ;

		maxArraySize = 12000000;

		startTotalTime = System.currentTimeMillis();
		for(arraySize = 400; arraySize < maxArraySize;arraySize+=4){
			int[] a = new int[arraySize];
			int[] b = new int[arraySize];

			for(j = 0; j < arraySize; j++){
				a[j] = 6;
				b[j] = 7;
			}

			timeFor = Command.addUsingFor(a,b);
			timeSIMD = Command.addUsingSIMD(a,b);

			writer.write(arraySize + " " + timeSIMD + " " + timeFor + "\n");
			writer.flush();
		}
		endTotalTime = System.currentTimeMillis();

		writer.write("TestTime = " + (endTotalTime - startTotalTime));
		writer.flush();
		writer.close();
	}
}