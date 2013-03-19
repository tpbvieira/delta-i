package simdMapper;
public class cpuIdDetect{
	final static int CPU_FEATURE_MMX = 1;
	final static int CPU_FEATURE_SSE = 2;
	final static int _CPU_FEATURE_SSE2 = 3;
	final static int _CPU_FEATURE_SSE3 = 4;
	final static int _CPU_FEATURE_SSSE3 = 5;
	final static int _CPU_FEATURE_SSE41 = 6;
	final static int _CPU_FEATURE_SSE42 = 7;
	final static int _CPU_FEATURE_AVX = 8;

	public static native int mmxIsSupported();
	public static native int sseIsSupported();
	public static native int sse2IsSupported();
	public static native int sse3IsSupported();
	public static native int ssse3IsSupported();
	public static native int sse41IsSupported();
	public static native int sse42IsSupported();
	public static native int avxIsSupported();

	static{
		System.load("/home/thiago/java/projects/delta-i/trunk/delta-experiments/lib/cpuIdLib.so");
		}
}