package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GetAddTimeJavaPure {
	public static void main(String[] args) throws InterruptedException, IOException {
		long t1,t2,t3,t4;
		int arraySize,j,maxArraySize = 10000;
		BufferedWriter writer = new BufferedWriter(new FileWriter(Command.getLocalInfo() + "-getAddTimeJavaPure.txt",true)) ;

		t3 = System.nanoTime();
		for(arraySize = 1; arraySize < maxArraySize;arraySize++){
			t1 = System.nanoTime();
			int[] a = new int[arraySize];
			int[] b = new int[arraySize];
			int[] c = new int[arraySize];

			for(j = 0; j < arraySize; j++){
				a[j] = 6;
				b[j] = 7;
			}			

			for(int i = 0;i<a.length;i++){	
				c[i]=a[i]+b[i];
			}
			t2 = System.nanoTime();
			writer.write(arraySize + " " + (t2 - t1) + "\n");
			writer.flush();
		}
		t4 = System.nanoTime();
		writer.write("Total = " + (t4 - t3) + "\n");
		writer.close();		
	}
}