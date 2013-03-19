package simdMapper;

import jSIMD.ExecutionList;

public class NativeLibrary {

	public static native void Execute(ExecutionList el);
	public static native void voidNativeCall();
	public static native int intNativeCall();
	
	public static native void addIntegerSIMD(int i);
	public static native void addIntegerCTotal(int i);
	public static native int [] addInteger(int [] a, int [] b);
	public static native int [] addIntegerC(int [] a, int [] b);
	public static native void addConstArrayC(int a, int [] b);
	public static native short [] addShort(short [] a, short [] b);
	public static native byte [] addByte(byte [] a, byte [] b);
	
	public static native void addIntegerSIMDThread();
	public static native void addIntegerCThread();
	public static native void addIntegerCThreads(int numThread, int arraySize);
	public static native void addIntegerArrayCThreads(int [] a, int [] b,int numThread);
	public static native void addIntegerArrayCThreads1(int [] a, int [] b,int numThread);
	public static native void addIntegerArrayCThreads2(int [] a, int [] b,int numThread);
	
	public static native void nativeReferenceCopyTest1(Object obj);// Only test the call with object as parameter
	public static native void nativeReferenceCopyTest2(Object obj);// Test the call and get array from object
	public static native void nativeReferenceCopyTest3(Object obj);// Test iteration on array 
	public static native void nativeReferenceCopyCompareReferenceTest(Object obj1,Object obj2);
	public static native void nativeReferenceCopyCompareCloneTest(Object obj1,Object obj2);
	
	static{
		System.load("/home/thiago/java/projects/delta-i/trunk/delta-experiments/lib/NativeLibrary.so");
	}

}