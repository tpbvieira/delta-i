package test;

import java.io.IOException;

import simdMapper.NativeLibrary;

public class GetAddTimeFor {

	public static void main(String[] args) throws InterruptedException, IOException {
		getAddTimeFor();
	}

	public static void getAddTimeFor() throws IOException{
		long t1,t2, temp = 0, tempo_total = 0;
		int j,arraySize = 10000000;
		int contador;

		//		for(contador=0;contador<100;contador++){
		//		t1 = System.currentTimeMillis();
		//		int[] a = new int[arraySize];
		//		int[] b = new int[arraySize];
		//		int[] c = new int[arraySize];
		//
		//		for(j = 0; j < arraySize; j++){
		//			a[j] = 6;
		//			b[j] = 7;
		//		}
		//
		//		for(int i = 0;i<a.length;i++){	
		//			c[i]=a[i]+b[i];
		//		}
		//		t2 = System.currentTimeMillis();
		//		
		//		temp += t2-t1;
		//		
		//		}
		//		tempo_total= temp/100;
		//		System.out.println("##TotalJava = "+ tempo_total);


		//		t1 = System.currentTimeMillis();
		//		NativeLibrary.addIntegerSIMD(arraySize);
		//		t2 = System.currentTimeMillis();
		//		System.out.println("##TotalSIMD = "+ (t2 - t1));

		//		contador=0;
		//		temp=0;
		//		tempo_total=0;
		//		for(contador=0;contador<100;contador++){
		//		t1 = System.currentTimeMillis();
		//		NativeLibrary.addIntegerCTotal(arraySize);
		//		t2 = System.currentTimeMillis();		
		//		temp += t2-t1;
		//		}
		//		tempo_total= temp/100;
		//		System.out.println("##TotalC = "+ tempo_total);

		contador=0;
		temp=0;
		tempo_total=0;
		for(contador=0;contador<100;contador++){
			t1 = System.currentTimeMillis();
			NativeLibrary.addIntegerCThread();
			t2 = System.currentTimeMillis();
			temp += t2-t1;
		}
		tempo_total= temp/100;
		System.out.println("##TotalCThread = "+ tempo_total);
	}
}