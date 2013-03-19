package test;

import java.io.IOException;

public class GetAddTimeC {
	public static void main(String[] args) throws InterruptedException, IOException {
		int arraySize,j,maxArraySize = 30;
		long timeC,timeJava;

		for(arraySize = 1; arraySize <= maxArraySize;arraySize++){
			int a = Integer.MAX_VALUE-10;
			int[] b = new int[arraySize];

			for(j = 0; j < arraySize; j++){
				b[j] = 7;
			}

			timeC = Command.addConstArrayUsingC(a,b);
			timeJava = addConstArrayUsingJava(a,b);
			if(timeC < timeJava)
				System.out.println("## Size="+arraySize);
		}
	}
	
	private static long addConstArrayUsingJava(int a, int[] b){		
		long startTime,endTime;

		startTime = System.nanoTime();
		for (int i = 0; i < b.length; i++) {
			b[i] = a + b[i];
		}
		endTime = System.nanoTime();

		return (endTime - startTime);
	}
	
}