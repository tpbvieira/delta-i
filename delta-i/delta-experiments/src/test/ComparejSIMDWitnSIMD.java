package test;

import java.io.IOException;

public class ComparejSIMDWitnSIMD {
	public static void main(String[] args) throws InterruptedException, IOException {
		comparejSIMDWithSIMD();
	}	
	
	public static void comparejSIMDWithSIMD(){
		int arraySize = 4;
		long timeSIMD = 0,timejSIMD = 0;

		for(;arraySize < 160;arraySize++){

			int[] a = new int[arraySize];
			int[] b = new int[arraySize];

			for(int i = 0; i < arraySize; i = i + 2){
				b[i] = 7;
			}
			
			timejSIMD = Command.addUsingjSIMD(a,b);
			timeSIMD = Command.addUsingSIMD(a,b);

			System.out.println("ArraySize = " + arraySize + " . [" + timejSIMD/timeSIMD + "][" + timejSIMD + "][" + timeSIMD + "]");
		}
	}
}
