package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import simdMapper.NativeLibrary;

public class GetAddTimeSIMDPure {
	public static void main(String[] args) throws InterruptedException, IOException {
		long t1,t2,t3,t4;
		int arraySize,maxArraySize = 10000;
		BufferedWriter writer = new BufferedWriter(new FileWriter(Command.getLocalInfo() + "-getAddTimeSIMDPure.txt",true)) ;

		t3 = System.nanoTime();
		for(arraySize = 1; arraySize < maxArraySize;arraySize++){
			t1 = System.nanoTime();
			NativeLibrary.addIntegerSIMD(arraySize);
			t2 = System.nanoTime();
			writer.write(arraySize + " " + (t2 - t1) + "\n");
			writer.flush();
		}
		t4 = System.nanoTime();
		writer.write("Total = " + (t4 - t3) + "\n");
		writer.close();
	}
}
