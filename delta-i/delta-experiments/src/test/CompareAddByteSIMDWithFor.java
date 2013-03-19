package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CompareAddByteSIMDWithFor {

	public static void main(String[] args) throws InterruptedException, IOException {
		compareAddByteSIMDWithFor();
	}

	public static void compareAddByteSIMDWithFor() throws IOException{
		byte arraySize,j,maxArraySize;
		long timeSIMD,timeFor,startTotalTime,endTotalTime;
		BufferedWriter writer = new BufferedWriter(new FileWriter(Command.getLocalInfo() + "-compareAddByteSIMDWithFor.txt",true)) ;

		maxArraySize = 127;

		startTotalTime = System.currentTimeMillis();
		for(arraySize = 1; arraySize < maxArraySize;arraySize++){
			byte[] a = new byte[arraySize];
			byte[] b = new byte[arraySize];

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