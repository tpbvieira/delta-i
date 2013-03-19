package simdMapper;


public class simdArithmatic
{
	public native void addIntegers(
		int[] vector1, int[] vector2, int[] vector3);

	public native void multIntegers(
		int[] vector1, int[] vector2, long [] vector3);
	static
	{
		System.load("bin\\mapper.dll");
	}
}