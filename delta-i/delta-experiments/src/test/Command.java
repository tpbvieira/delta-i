package test;

import jSIMD.PackedInt;
import jSIMD.Processor;

import java.io.IOException;

import simdMapper.NativeLibrary;

public class Command {

	/**
	 * @param args
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		System.out.println(getLocalInfo());
	}	
	
	public static  long addUsingFor(int[] a, int[]b){
		long startTime,endTime;
		int [] c = new int[a.length];

		startTime = System.nanoTime();
		for(int i = 0;i<a.length;i++){	
			c[i]=a[i]+b[i];
		}
		endTime = System.nanoTime();

		return (endTime - startTime);
	}

	public static  long addUsingFor(short[] a, short[]b){
		long startTime,endTime;
		short [] c = new short[a.length];

		startTime = System.nanoTime();
		for(int i = 0;i<a.length;i++){	
			c[i]=(short) (a[i]+b[i]);
		}
		endTime = System.nanoTime();

		return (endTime - startTime);
	}
	
	public static  long addUsingFor(byte[] a, byte[]b){
		long startTime,endTime;
		byte [] c = new byte[a.length];

		startTime = System.nanoTime();
		for(int i = 0;i<a.length;i++){	
			c[i]=(byte) (a[i]+b[i]);
		}
		endTime = System.nanoTime();

		return (endTime - startTime);
	}
	
	public static long addUsingjSIMD(int[] a, int[]b){
		long startTime,endTime;

		Processor processor = Processor.INSTANCE;

		PackedInt p1 = new PackedInt(a);
		PackedInt p2 = new PackedInt(b);
		PackedInt p3 = new PackedInt(a.length);

		startTime = System.nanoTime();
		processor.Add(p1, p2, p3);
		processor.Execute();
		endTime = System.nanoTime();

		return (endTime - startTime);
	}
	
	public static long addUsingSIMD(int[] a, int[]b){
		long startTime,endTime;

		startTime = System.nanoTime();
		NativeLibrary.addInteger(a, b);
		endTime = System.nanoTime();

		return (endTime - startTime);
	}

	public static long addUsingC(int[] a, int[]b){
		long startTime,endTime;

		startTime = System.nanoTime();
		NativeLibrary.addIntegerC(a, b);
		endTime = System.nanoTime();

		return (endTime - startTime);
	}
	
	public static long addConstArrayUsingC(int a, int[]b){
		long startTime,endTime;

		startTime = System.nanoTime();
		NativeLibrary.addConstArrayC(a, b);
		endTime = System.nanoTime();

		return (endTime - startTime);
	}
	
	public static long addUsingSIMD(short[] a, short[]b){
		long startTime,endTime;

		startTime = System.nanoTime();
		NativeLibrary.addShort(a, b);
		endTime = System.nanoTime();

		return (endTime - startTime);
	}
	
	public static long addUsingSIMD(byte[] a, byte[]b){
		long startTime,endTime;

		startTime = System.nanoTime();
		NativeLibrary.addByte(a, b);
		endTime = System.nanoTime();

		return (endTime - startTime);
	}

	public static  long subUsingFor(int[] a, int[]b){
		long startTime,endTime;
		int [] c = new int[a.length];

		startTime = System.nanoTime();
		for(int i = 0;i<a.length;i++){	
			c[i]=a[i]-b[i];
		}
		endTime = System.nanoTime();

		return (endTime-startTime);
	}

	public static long subUsingSIMD(int[] a, int[]b){
		long startTime,endTime;

		Processor processor = Processor.INSTANCE;

		PackedInt p1 = new PackedInt(a);
		PackedInt p2 = new PackedInt(b);
		PackedInt p3 = new PackedInt(a.length);

		startTime = System.nanoTime();
		processor.Sub(p1, p2, p3);
		processor.Execute();
		endTime = System.nanoTime();

		return (endTime - startTime);
	}

	public static  long andUsingFor(int[] a, int b){
		long startTime,endTime;
		int [] c = new int[a.length];

		startTime = System.nanoTime();
		for(int i = 0;i<a.length;i++){	
			c[i]=a[i]&b;
		}
		endTime = System.nanoTime();

		return (endTime - startTime);
	}

	public static long andUsingjSIMD(int[] a, int b){
		long startTime,endTime;

		Processor processor = Processor.INSTANCE;

		PackedInt p1 = new PackedInt(a);
		PackedInt p3 = new PackedInt(a.length);

		startTime = System.nanoTime();
		processor.And(p1, b, p3);
		processor.Execute();
		endTime = System.nanoTime();

		return (endTime - startTime);
	}

	public static  long orUsingFor(int[] a, int b){
		long startTime,endTime;
		int [] c = new int[a.length];

		startTime = System.nanoTime();
		for(int i = 0;i<a.length;i++){	
			c[i]=a[i]|b;
		}
		endTime = System.nanoTime();

		return (endTime - startTime);
	}

	public static long orUsingjSIMD(int[] a, int b){
		long startTime,endTime;

		Processor processor = Processor.INSTANCE;

		PackedInt p1 = new PackedInt(a);
		PackedInt p3 = new PackedInt(a.length);

		startTime = System.nanoTime();
		processor.Or(p1, b, p3);
		processor.Execute();
		endTime = System.nanoTime();

		return (endTime - startTime);
	}

	public static  long addTripleUsingFor(int[] a, int[]b){
		long startTime,endTime;
		
		int [] c = new int[a.length];
		int [] d = new int[a.length];
		int [] e = new int[a.length];

		startTime = System.nanoTime();
		for(int i = 0;i<a.length;i++){	
			c[i]=a[i]+b[i];
		}
		for(int i = 0;i<a.length;i++){	
			d[i]=a[i]+a[i];
		}
		for(int i = 0;i<a.length;i++){	
			e[i]=b[i]+b[i];
		}
		endTime = System.nanoTime();

		return (endTime - startTime);
	}

	public static long addTripleUsingjSIMD(int[] a, int[]b){
		long startTime,endTime;

		Processor processor = Processor.INSTANCE;

		PackedInt p1 = new PackedInt(a);
		PackedInt p2 = new PackedInt(b);
		PackedInt p3 = new PackedInt(a.length);
		PackedInt p4 = new PackedInt(a.length);
		PackedInt p5 = new PackedInt(a.length);

		startTime = System.nanoTime();
		processor.Add(p1, p2, p3);
		processor.Add(p1, p1, p4);
		processor.Add(p2, p2, p5);
		processor.Execute();
		endTime = System.nanoTime();

		return (endTime - startTime);
	}
	
	public static String getLocalInfo(){
		StringBuffer result = new StringBuffer();
		
		result.append(System.getProperty("os.name"));
		result.append("-");
		result.append(System.getProperty("os.arch"));
		result.append("-");
		result.append(System.getProperty("os.version"));

		return result.toString();
	}
}