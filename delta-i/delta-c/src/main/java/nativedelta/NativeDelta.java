package nativedelta;

public class NativeDelta {

	static{
		System.load("/home/thiago/java/projects/delta-i/trunk/delta-c/lib/NativeDelta.so");
	}
	
	public static native void EQIntArray(int[] arg1, int[] arg2, boolean[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	public static native void EQIntArrayConst(int[] arg1, int arg2, boolean[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	public static native void EQIntConstArray(int arg1, int[] arg2, boolean[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	
	public static native void LTIntArray(int[] arg1, int[] arg2, boolean[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	public static native void LTIntArrayConst(int[] arg1, int arg2, boolean[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	public static native void LTIntConstArray(int arg1, int[] arg2, boolean[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	
	public static native void GTIntArray(int[] arg1, int[] arg2, boolean[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	public static native void GTIntArrayConst(int[] arg1, int arg2, boolean[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	public static native void GTIntConstArray(int arg1, int[] arg2, boolean[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	
	public static native void ADDIntArray(int[] arg1, int[] arg2, int[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	public static native void ADDIntArrayConst(int[] arg1, int arg2, int[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	public static native void ADDIntConstArray(int arg1, int[] arg2, int[] result, int[] mask, int maskIndex, int maskSize, int maskType);
	
	
	
	public static native void EQIntArrayContigousMask(int[] arg1, int[] arg2, boolean[] result);
	public static native void EQIntArrayConstContigousMask(int[] arg1, int arg2, boolean[] result);
	public static native void EQIntConstArrayContigousMask(int arg1, int[] arg2, boolean[] result);
	
	public static native void LTIntArrayContigousMask(int[] arg1, int[] arg2, boolean[] result);
	public static native void LTIntArrayConstContigousMask(int[] arg1, int arg2, boolean[] result);
	public static native void LTIntConstArrayContigousMask(int arg1, int[] arg2, boolean[] result);
	
	public static native void GTIntArrayContigousMask(int[] arg1, int[] arg2, boolean[] result);
	public static native void GTIntArrayConstContigousMask(int[] arg1, int arg2, boolean[] result);
	public static native void GTIntConstArrayContigousMask(int arg1, int[] arg2, boolean[] result);
	
	public static native void ADDIntArrayContigousMask(int[] arg1, int[] arg2, int[] result);
	public static native void ADDIntArrayConstContigousMask(int[] arg1, int arg2, int[] result);
	public static native void ADDIntConstArrayContigousMask(int arg1, int[] arg2, int[] result);
}