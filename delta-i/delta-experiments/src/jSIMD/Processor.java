package jSIMD;

import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Processor implements MMXFunctions, SSE42Functions
{
//	private final static boolean TEST = true;
	private final static boolean TEST = false;
	
	//declare thread related variables
	static ThreadGroup workers = new ThreadGroup("Workers");
	private static int NUM_THREADS = 8;
//	private static final int BLOCK_SIZE = 100000;
	private static Semaphore threadMutex = new Semaphore(NUM_THREADS);
	
		
	public int getNumThreads(){
		return NUM_THREADS;
	}
	
	public final static Processor INSTANCE = getProcessor();
//	public Vector<Integer> SourceA = new Vector<Integer>();
//	public Vector<Integer> SourceB = new Vector<Integer>();
//	public Vector<Integer> Destination = new Vector<Integer>();
//	public Vector<Integer> ExecutionList = new Vector<Integer>();
//	public Vector<Integer> ArgumentList = new Vector<Integer>();	
	public ExecutionList el;
	//private boolean isUnknownProcessor=true;
	
	protected Processor() {
		el = new ExecutionList();
	}
	
	
	
	private static Processor getProcessor() {
	//.....
	// TODO Add Processor Identification Code
	//TODO: DELETE THE NEXT LINE!!!!
	if(TEST)
    {
		return new Processor(); 
	}
	
	
	if(simdMapper.cpuIdDetect.sse42IsSupported()>0)
	{
		System.out.println("SSE42Processor supported");
		return new SSE42Processor();
	}
	if(simdMapper.cpuIdDetect.sse41IsSupported()>0)
	{
		System.out.println("SSE41Processor supported");
		return new SSE41Processor();
	}
	if(simdMapper.cpuIdDetect.sse3IsSupported()>0)
	{
		System.out.println("SSE3Processor supported");
		return new SSE3Processor();
	}
	if(simdMapper.cpuIdDetect.sse2IsSupported()>0)
	{
		System.out.println("SSE2Processor supported");
		return new SSE2Processor();
	}
	if(simdMapper.cpuIdDetect.sseIsSupported()>0)
	{
		System.out.println("SSEProcessor supported");
		return new SSEProcessor();
	}
	if(simdMapper.cpuIdDetect.mmxIsSupported() > 0)
	{
		System.out.println("MMXProcessor supported");
		return new MMXProcessor();
	}
	return new Processor();
}
	
	
	
	public void Unpack()
	{
		// TODO: Unpack code
	}
	
	// pack functions
	public PackedByte Pack(byte[] array) {
		return new PackedByte(array);
	}

	public PackedChar Pack(char[] array) {
		return new PackedChar(array);
	}

	public PackedDouble Pack(double[] array) {
		return new PackedDouble(array);
	}

	public PackedFloat Pack(float[] array) {
		return new PackedFloat(array);
	}

	public PackedInt Pack(int[] array) {
		return new PackedInt(array);
	}

	public PackedLong Pack(long[] array) {
		return new PackedLong(array);
	}

	public PackedShort Pack(short[] array) {
		return new PackedShort(array);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Add(byte[], byte[])
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Add(byte[], byte[])
	 */
	public void Add(PackedByte A, PackedByte B, PackedByte C) throws LengthException
	{		
		for(int i= 0; i< A.length; i++)
		{
			C.toArray()[i] = (byte)(A.toArray()[i] + B.toArray()[i]);
		}
	}
	
	public void Add(PackedChar A, PackedChar B, PackedChar C) throws LengthException
	{
		for(int i= 0; i< A.length; i++)
		{
			C.toArray()[i] = (char)(A.toArray()[i] + B.toArray()[i]);
		}
	}
	
	public void Add(PackedDouble A, PackedDouble B, PackedDouble C)  throws LengthException
	{
		for(int i= 0; i< A.length; i++)
		{
			C.toArray()[i] = A.toArray()[i] + B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Add(PackedFloat, PackedFloat)
	 */
	public void Add(PackedFloat A, PackedFloat B, PackedFloat C)  throws LengthException
	{
		for(int i= 0; i< A.length; i++)
		{
			C.toArray()[i] = A.toArray()[i] + B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Add(PackedInt, PackedInt)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Add(PackedInt, PackedInt)
	 */
	public void Add(PackedInt A, PackedInt B, PackedInt C)
	{
//		int startIndex = 0;
//		int BLOCK_SIZE = (C.length / NUM_THREADS);
//		int extra = A.length % BLOCK_SIZE == 0 ? 0 : 1;
//		int totalLength = ((C.length / BLOCK_SIZE) + extra);
//		
//		for (int blocks = 0; blocks < totalLength; blocks++) 
//		{
//			int endIndex = (startIndex + BLOCK_SIZE)%(C.length+1);
//		
//			try {
//				// System.out.println("Active threads = "+workers.activeCount());
////				int length = (endIndex-startIndex);
////				int[] a = new int[length];
////				int[] b = new int[length];
////				System.arraycopy(A.toArray(), startIndex, a, 0, length);
////				System.arraycopy(B.toArray(), startIndex, b, 0, length);
//				threadMutex.acquire();
//				MultiThreadAdd grunt = new MultiThreadAdd(threadMutex,
//						A.toArray(), B.toArray(), C.toArray(), startIndex, endIndex);
//				new Thread(workers, grunt).start();
//			} catch (InterruptedException e) {
//				System.out.println("FAILED TO GET THREAD GOING!!");
//				e.printStackTrace();
//			}
//			startIndex += BLOCK_SIZE;
//		}	
		for(int i= 0; i< A.length; i++)
		{
			C.toArray()[i] = A.toArray()[i] + B.toArray()[i];
		}
	}
	
	public void Add(PackedLong A, PackedLong B, PackedLong C) throws LengthException
	{
		for(int i= 0; i< A.length; i++)
		{
			C.toArray()[i] = A.toArray()[i] + B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Add(PackedShort, PackedShort)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Add(PackedShort, PackedShort)
	 */
	public void Add(PackedShort A,PackedShort B, PackedShort C) throws LengthException
	{
		short[] a = A.toArray();
		short[] b = B.toArray();
		for(int i= 0; i< A.length; i++)
		{
			C.toArray()[i] = (short)(a[i] + b[i]);
			System.out.print("");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#AddS(PackedByte, PackedByte)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#AddS(PackedByte, PackedByte)
	 */
	public void AddS(PackedByte A, PackedByte B, PackedByte C) throws LengthException
	{
		for(int i= 0; i< A.length; i++)
		{
			int temp = A.toArray()[i] + B.toArray()[i];
			C.toArray()[i] = temp>Byte.MAX_VALUE?Byte.MAX_VALUE:(byte)temp;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#AddS(PackedShort, PackedShort, PackedShort)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#AddS(PackedShort, PackedShort, PackedShort)
	 */
	public void AddS(PackedShort A,PackedShort B, PackedShort C) throws LengthException
	{
		for(int i= 0; i< A.length; i++)
		{
			int temp = A.toArray()[i] + B.toArray()[i];
			C.toArray()[i] = temp>Short.MAX_VALUE?Short.MAX_VALUE:(short)temp;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE3Functions#AddSub(PackedDouble, PackedDouble)
	 */
	public void AddSub(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		if(A.length%2 == 0)
		{
			for(int i= 0; i< A.length; i=i+2)
			{
				C.toArray()[i]= A.toArray()[i]+B.toArray()[i];
				C.toArray()[i+1]= A.toArray()[i+1]-B.toArray()[i+1];
			}
		}
		else
		{
			for(int i=0;i<A.length-1;i=i+2)
			{
				C.toArray()[i] = A.toArray()[i]+B.toArray()[i];
				C.toArray()[i+1]=A.toArray()[i+1]-B.toArray()[i+1];
			}
			C.toArray()[A.length-1] = A.toArray()[A.length-1]+B.toArray()[A.length-1];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE3Functions#AddSub(PackedFloat, PackedFloat)
	 */
	public void AddSub(PackedFloat A,PackedFloat B, PackedFloat C) throws LengthException
	{
		if(A.length%2 == 0)
		{
			for(int i= 0; i< A.length; i=i+2)
			{
				C.toArray()[i]= A.toArray()[i]+B.toArray()[i];
				C.toArray()[i+1]= A.toArray()[i+1]-B.toArray()[i+1];
			}
		}
		else
		{
			for(int i=0;i<A.length-1;i=i+2)
			{
				C.toArray()[i] = A.toArray()[i]+B.toArray()[i];
				C.toArray()[i+1]=A.toArray()[i+1]-B.toArray()[i+1];
			}
			C.toArray()[A.length-1] = A.toArray()[A.length-1]+B.toArray()[A.length-1];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#And(PackedDouble, PackedDouble)
	 */
	public void And(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = Double.longBitsToDouble(Double.doubleToLongBits(A.toArray()[i])&Double.doubleToLongBits(B.toArray()[i]));
		}
	}
	
	public void And(PackedInt A, Integer B, PackedInt C)
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = A.toArray()[i]&B;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#And(PackedFloat, PackedFloat)
	 */
	public void And(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = Float.intBitsToFloat(Float.floatToIntBits(A.toArray()[i])&Float.floatToIntBits(B.toArray()[i]));
		}
	}
	
	public int createOpCode(int operation, int typeA, int typeB, int typeDes)
	{
		return operation << 12 | typeA << 8 | typeB << 4 | typeDes;
	}
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#InterleaveLow(jSIMD.PackedByte, jSIMD.PackedByte, jSIMD.PackedByte)
	 */
	public void InterleaveLow(PackedByte A, PackedByte B, PackedByte C) throws LengthException
	{
		//_mm_unpacklo_epi8 SSE2
		for(int i=0; i<C.length; i++)
		{
			if(i%2==0)
			{
				C.toArray()[i] = A.toArray()[i/2];
			}
			else
			{
				C.toArray()[i] = B.toArray()[i/2];
			}
		}
	}
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#InterleaveLow(jSIMD.PackedShort, jSIMD.PackedShort, jSIMD.PackedShort)
	 */
	public void InterleaveLow(PackedShort A, PackedShort B, PackedShort C) throws LengthException
	{
		//_mm_unpacklo_epi16 SSE2
		for(int i=0; i<C.length; i++)
		{
			if(i%2==0)
			{
				C.toArray()[i] = A.toArray()[i/2];
			}
			else
			{
				C.toArray()[i] = B.toArray()[i/2];
			}
		}
	}
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#InterleaveLow(jSIMD.PackedInt, jSIMD.PackedInt, jSIMD.PackedInt)
	 */
	public void InterleaveLow(PackedInt A, PackedInt B, PackedInt C) throws LengthException
	{
		//_mm_unpacklo_epi32 SSE2
		for(int i=0; i<C.length; i++)
		{
			if(i%2==0)
			{
				C.toArray()[i] = A.toArray()[i/2];
			}
			else
			{
				C.toArray()[i] = B.toArray()[i/2];
			}
		}
	}
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#InterleaveLow(jSIMD.PackedLong, jSIMD.PackedLong, jSIMD.PackedLong)
	 */
	public void InterleaveLow(PackedLong A, PackedLong B, PackedLong C) throws LengthException
	{
		//_mm_unpacklo_epi8 SSE2
		for(int i=0; i<C.length; i++)
		{
			if(i%2==0)
			{
				C.toArray()[i] = A.toArray()[i/2];
			}
			else
			{
				C.toArray()[i] = B.toArray()[i/2];
			}
		}
	}
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#InterleaveHigh(jSIMD.PackedByte, jSIMD.PackedByte, jSIMD.PackedByte)
	 */
	public void InterleaveHigh(PackedByte A, PackedByte B, PackedByte C) throws LengthException
	{
		//_mm_unpackhi_epi8 SSE2
		int halflength = A.length/2;
		for(int i=0; i<C.length; i++)
		{
			if(i%2==0)
			{
				C.toArray()[i] = A.toArray()[i/2 + halflength];
			}
			else
			{
				C.toArray()[i] = B.toArray()[i/2+ halflength];
			}
		}
	}
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#InterleaveHigh(jSIMD.PackedShort, jSIMD.PackedShort, jSIMD.PackedShort)
	 */
	public void InterleaveHigh(PackedShort A, PackedShort B, PackedShort C) throws LengthException
	{
		//_mm_unpackhi_epi16 SSE2
		int halflength = A.length/2;
		for(int i=0; i<C.length; i++)
		{
			if(i%2==0)
			{
				C.toArray()[i] = A.toArray()[i/2 + halflength];
			}
			else
			{
				C.toArray()[i] = B.toArray()[i/2+ halflength];
			}
		}
	}
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#InterleaveHigh(jSIMD.PackedInt, jSIMD.PackedInt, jSIMD.PackedInt)
	 */
	public void InterleaveHigh(PackedInt A, PackedInt B, PackedInt C) throws LengthException
	{
		//_mm_unpackhi_epi32 SSE2
		int halflength = A.length/2;
		for(int i=0; i<C.length; i++)
		{
			if(i%2==0)
			{
				C.toArray()[i] = A.toArray()[i/2 + halflength];
			}
			else
			{
				C.toArray()[i] = B.toArray()[i/2+ halflength];
			}
		}
	}
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#InterleaveHigh(jSIMD.PackedLong, jSIMD.PackedLong, jSIMD.PackedLong)
	 */
	public void InterleaveHigh(PackedLong A, PackedLong B, PackedLong C) throws LengthException
	{
		//_mm_unpackhi_epi64 SSE2
		int halflength = A.length/2;
		for(int i=0; i<C.length; i++)
		{
			if(i%2==0)
			{
				C.toArray()[i] = A.toArray()[i/2 + halflength];
			}
			else
			{
				C.toArray()[i] = B.toArray()[i/2+ halflength];
			}
		}
	}
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#Interleave(jSIMD.PackedShort, jSIMD.PackedShort, jSIMD.PackedInt)
	 */
	public void Interleave(PackedShort A, PackedShort B, PackedInt C) throws LengthException
	{
		//_mm_unpacklo_epi16 SSE2
		//_mm_unpackhi_epi16 SSE2
		int length = A.length;
		for(int i=0;i<length; i++)
		{
			C.toArray()[i]= ((int)(B.toArray()[i])<<16)|(A.toArray()[i]);
		}
	}
	
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#Interleave(jSIMD.PackedInt, jSIMD.PackedInt, jSIMD.PackedLong)
	 */
	public void Interleave(PackedInt A, PackedInt B, PackedLong C) throws LengthException
	{
		//_mm_unpacklo_epi32 SSE2
		//_mm_unpackhi_epi32 SSE2
		int length = A.length;
		for(int i=0;i<length; i++)
		{
			C.toArray()[i]= ((long)(B.toArray()[i])<<32)|(A.toArray()[i]);
		}
	}
	/* (non-Javadoc)
	 * @see jSIMD.Interleave#Interleave(jSIMD.PackedByte, jSIMD.PackedByte, jSIMD.PackedShort)
	 */
	public void Interleave(PackedByte A, PackedByte B, PackedShort C) throws LengthException
	{
		int length = A.length;
		for(int i=0;i<length; i++)
		{
			C.toArray()[i]= (short) (((short)(B.toArray()[i])<<8)|(A.toArray()[i]));
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#And(PackedLong, PackedLong)
	 */
	public void And(PackedLong A, PackedLong B, PackedLong C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = A.toArray()[i]&B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Avg(char[])
	 */
	public char Avg(PackedChar A)
	{
		long sum=0;
		for(int i=0;i<A.length;i++)
		{
			sum += A.toArray()[i];
		}
		return (char) (sum/A.length);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Blend(PackedByte, PackedByte, PackedByte)
	 */
	public void Blend(PackedByte A,PackedByte B,PackedInt mask, PackedByte C) throws LengthException
	{
		// The SSE4 Blend command will blend two arrays together based on the
		// value of Mask.
		// If the high bit of Mask is set to 1, the value from the first array
		// will be placed in the returned array,
		// if the high bit of Mask is set to 0, the value from the second array
		// is placed in the returned array.
		// 16 bytes are blended concurrently.
		for(int i=0; i<A.length;i++)
			C.toArray()[i] = ((mask.toArray()[i]&0x10)>0)?A.toArray()[i]:B.toArray()[i];
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Blend(PackedDouble, PackedDouble, PackedDouble)
	 */
	public void Blend(PackedDouble A, PackedDouble B, PackedInt Mask, PackedDouble C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (Double.doubleToLongBits(Mask.toArray()[i])%0x8000000000000000L)>0?A.toArray()[i]:B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Blend(PackedFloat, PackedFloat, PackedFloat)
	 */
	public void Blend(PackedFloat A, PackedFloat B, PackedInt Mask, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=(Float.floatToIntBits(Mask.toArray()[i])&0x80000000)>0?A.toArray()[i]:B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Blend(PackedInt, PackedInt, PackedInt)
	 */
	public void Blend(PackedInt A,PackedInt B, PackedInt Mask, PackedInt C) throws LengthException
	{
		for(int i=0; i<A.length;i++)
		{
			C.toArray()[i] = (Mask.toArray()[i]&0x80000000)>0?A.toArray()[i]:B.toArray()[i];
		}

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Ceil(PackedDouble)
	 */
	public void Ceil(PackedDouble A, PackedDouble C)
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (double)((long)A.toArray()[i]+1);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Ceil(PackedFloat)
	 */
	public void Ceil(PackedFloat A, PackedFloat C) throws LengthException
	{
		for(int i=0; i< A.length;i++)
		{
			C.toArray()[i] = (float)((long)A.toArray()[i]+1);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Divide(PackedFloat, PackedFloat)
	 */
	public void Divide(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i] = A.toArray()[i]/B.toArray()[i];
		}
	}
	
	public void Divide(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = A.toArray()[i]/B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Equal(PackedByte, PackedByte)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Equal(PackedByte, PackedByte)
	 */
	public void Equal(PackedByte A, PackedByte B, PackedByte C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (byte)(A.toArray()[i]==B.toArray()[i]?0xFF:0);
		}
	}
	
	public void Equal(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]=Double.longBitsToDouble(A.toArray()[i]==B.toArray()[i]?Long.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Equal(PackedFloat, PackedFloat)
	 */
	public void Equal(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{	
			C.toArray()[i]=Float.intBitsToFloat(A.toArray()[i]==B.toArray()[i]?Integer.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Equal(PackedInt, PackedInt)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Equal(PackedInt, PackedInt)
	 */
	public void Equal(PackedInt A, PackedInt B, PackedInt C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{	
			C.toArray()[i]=(A.toArray()[i]==B.toArray()[i]?Integer.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Equal(PackedLong, PackedLong)
	 */
	public void Equal(PackedLong A, PackedLong B, PackedLong C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{	
			C.toArray()[i]=(A.toArray()[i]==B.toArray()[i]?Long.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Equal(PackedShort, PackedShort)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Equal(PackedShort, PackedShort)
	 */
	public void Equal(PackedShort A, PackedShort B, PackedShort C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]=(short)(A.toArray()[i]==B.toArray()[i]?Short.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Floor(PackedDouble)
	 */
	public void Floor(PackedDouble A, PackedDouble C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= (double)((long)A.toArray()[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Floor(PackedFloat)
	 */
	public void Floor(PackedFloat A, PackedFloat C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= (float)((long)A.toArray()[i]);
		}

	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#GreaterThan(PackedByte, PackedByte)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#GreaterThan(PackedByte, PackedByte)
	 */
	public void GreaterThan(PackedByte A,PackedByte B, PackedByte C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= (byte)(A.toArray()[i]>B.toArray()[i]?0xFF:0);
		}
	}
	
	public void GreaterThan(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= Double.longBitsToDouble(A.toArray()[i]>B.toArray()[i]?Long.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#GreaterThan(PackedFloat, PackedFloat)
	 */
	public void GreaterThan(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= Float.intBitsToFloat(A.toArray()[i]>B.toArray()[i]?Integer.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#GreaterThan(PackedInt, PackedInt)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#GreaterThan(PackedInt, PackedInt)
	 */
	public void GreaterThan(PackedInt A, PackedInt B, PackedInt C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= A.toArray()[i]>B.toArray()[i]?Integer.MAX_VALUE:0;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#GreaterThan(PackedLong, PackedLong)
	 */
	public void GreaterThan(PackedLong A, PackedLong B, PackedLong C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= A.toArray()[i]>B.toArray()[i]?Long.MAX_VALUE:0;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#GreaterThan(PackedShort, PackedShort)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#GreaterThan(PackedShort, PackedShort)
	 */
	public void GreaterThan(PackedShort A,PackedShort B, PackedShort C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= A.toArray()[i]>B.toArray()[i]?Short.MAX_VALUE:0;
		}
	}
	
	public void GreaterThanOrEqual(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= Double.longBitsToDouble(A.toArray()[i]>=B.toArray()[i]?Long.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#GreaterThanOrEqual(PackedFloat, PackedFloat)
	 */
	public void GreaterThanOrEqual(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= Float.intBitsToFloat(A.toArray()[i]>=B.toArray()[i]?Integer.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE3Functions#HAdd(PackedDouble, PackedDouble)
	 */
	public void HAdd(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		
		switch(A.length%4)
		{
		case 0:
			for(int i=0;i<A.length;i+=4)
			{
				C.toArray()[i] = A.toArray()[i]+A.toArray()[i+1];
				C.toArray()[i+1]= A.toArray()[i+2]+A.toArray()[i+3];
				C.toArray()[i+2] = B.toArray()[i] + B.toArray()[i+1];
				C.toArray()[i+3] = B.toArray()[i+2] + B.toArray()[i+3];
			}
			break;
		case 1:
			
			for(int i=0;i<A.length;i+=4)
			{
				if(i+1 == A.length)
				{
					C.toArray()[i] = A.toArray()[i];
					C.toArray()[i+1] = B.toArray()[i];
				}
				else
				{
					C.toArray()[i] = A.toArray()[i]+A.toArray()[i+1];
					C.toArray()[i+1]= A.toArray()[i+2]+A.toArray()[i+3];
					C.toArray()[i+2] = B.toArray()[i] + B.toArray()[i+1];
					C.toArray()[i+3] = B.toArray()[i+2] + B.toArray()[i+3];
				}
			}
			break;
		case 2:
			
			for(int i=0;i<A.length;i+=4)
			{
				if(i+2 == A.length)
				{
					C.toArray()[i] = A.toArray()[i] + A.toArray()[i+1];
					C.toArray()[i+1] = B.toArray()[i] + B.toArray()[i+1];
				}
				else
				{
					C.toArray()[i] = A.toArray()[i]+A.toArray()[i+1];
					C.toArray()[i+1]= A.toArray()[i+2]+A.toArray()[i+3];
					C.toArray()[i+2] = B.toArray()[i] + B.toArray()[i+1];
					C.toArray()[i+3] = B.toArray()[i+2] + B.toArray()[i+3];
				}
			}
			break;
		case 3:
			for(int i=0;i<A.length;i+=4)
			{
				if(i+3 == A.length)
				{
					C.toArray()[i] = A.toArray()[i] + A.toArray()[i+1];
					C.toArray()[i+1] = B.toArray()[i] + B.toArray()[i+1];
					C.toArray()[i+2] = A.toArray()[i+3];
					C.toArray()[i+3] = B.toArray()[i+3];
				}
				else
				{						
					C.toArray()[i] = A.toArray()[i]+A.toArray()[i+1];
					C.toArray()[i+1]= A.toArray()[i+2]+A.toArray()[i+3];
					C.toArray()[i+2] = B.toArray()[i] + B.toArray()[i+1];
					C.toArray()[i+3] = B.toArray()[i+2] + B.toArray()[i+3];
				}
			}
			break;
				
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE3Functions#HAdd(PackedFloat, PackedFloat)
	 */
	public void HAdd(PackedFloat A,PackedFloat B,PackedFloat C) throws LengthException
	{
		// HADDPS (Horizontal-Add-Packed-Single)
		//
		// * Input: { A0, A1, A2, A3 }, { B0, B1, B2, B3 }
		// * Output: { A0 + A1, A2 + A3, B0 + B1, B2 + B3 }
		// So, best option is interleaving SumA1, SumA2, SumB1, SumB2, SumA3...
			
			switch(A.length%4)
			{
			case 0:
				for(int i=0;i<A.length;i+=4)
				{
					C.toArray()[i] = A.toArray()[i]+A.toArray()[i+1];
					C.toArray()[i+1]= A.toArray()[i+2]+A.toArray()[i+3];
					C.toArray()[i+2] = B.toArray()[i] + B.toArray()[i+1];
					C.toArray()[i+3] = B.toArray()[i+2] + B.toArray()[i+3];
				}
				break;
			case 1:
				for(int i=0;i<A.length;i+=4)
				{
					if(i+1 == A.length)
					{
						C.toArray()[i] = A.toArray()[i];
						C.toArray()[i+1] = B.toArray()[i];
					}
					else
					{
						C.toArray()[i] = A.toArray()[i]+A.toArray()[i+1];
						C.toArray()[i+1]= A.toArray()[i+2]+A.toArray()[i+3];
						C.toArray()[i+2] = B.toArray()[i] + B.toArray()[i+1];
						C.toArray()[i+3] = B.toArray()[i+2] + B.toArray()[i+3];
					}
				}
				break;
			case 2:
				for(int i=0;i<A.length;i+=4)
				{
					if(i+2 == A.length)
					{
						C.toArray()[i] = A.toArray()[i] + A.toArray()[i+1];
						C.toArray()[i+1] = B.toArray()[i] + B.toArray()[i+1];
					}
					else
					{
						C.toArray()[i] = A.toArray()[i]+A.toArray()[i+1];
						C.toArray()[i+1]= A.toArray()[i+2]+A.toArray()[i+3];
						C.toArray()[i+2] = B.toArray()[i] + B.toArray()[i+1];
						C.toArray()[i+3] = B.toArray()[i+2] + B.toArray()[i+3];
					}
				}
				break;
			case 3:
				for(int i=0;i<A.length;i+=4)
				{
					if(i+3 == A.length)
					{
						C.toArray()[i] = A.toArray()[i] + A.toArray()[i+1];
						C.toArray()[i+1] = B.toArray()[i] + B.toArray()[i+1];
						C.toArray()[i+2] = A.toArray()[i+3];
						C.toArray()[i+3] = B.toArray()[i+3];
					}
					else
					{						
						C.toArray()[i] = A.toArray()[i]+A.toArray()[i+1];
						C.toArray()[i+1]= A.toArray()[i+2]+A.toArray()[i+3];
						C.toArray()[i+2] = B.toArray()[i] + B.toArray()[i+1];
						C.toArray()[i+3] = B.toArray()[i+2] + B.toArray()[i+3];
					}
				}
				break;
					
			}
		}
		
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE3Functions#HSub(PackedDouble, PackedDouble)
	 */
	public void HSub(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		
		switch(A.length%4)
		{
		case 0:
			for(int i=0;i<A.length;i+=4)
			{
				C.toArray()[i] = A.toArray()[i]-A.toArray()[i+1];
				C.toArray()[i+1]= A.toArray()[i+2]-A.toArray()[i+3];
				C.toArray()[i+2] = B.toArray()[i] - B.toArray()[i+1];
				C.toArray()[i+3] = B.toArray()[i+2] - B.toArray()[i+3];
			}
			break;
		case 1:
			for(int i=0;i<A.length;i+=4)
			{
				if(i+1 == A.length)
				{
					C.toArray()[i] = A.toArray()[i];
					C.toArray()[i+1] = B.toArray()[i];
				}
				else
				{
					C.toArray()[i] = A.toArray()[i]-A.toArray()[i+1];
					C.toArray()[i+1]= A.toArray()[i+2]-A.toArray()[i+3];
					C.toArray()[i+2] = B.toArray()[i] - B.toArray()[i+1];
					C.toArray()[i+3] = B.toArray()[i+2] - B.toArray()[i+3];
				}
			}
			break;
		case 2:
			for(int i=0;i<A.length;i+=4)
			{
				if(i+2 == A.length)
				{
					C.toArray()[i] = A.toArray()[i] - A.toArray()[i+1];
					C.toArray()[i+1] = B.toArray()[i] - B.toArray()[i+1];
				}
				else
				{
					C.toArray()[i] = A.toArray()[i]-A.toArray()[i+1];
					C.toArray()[i+1]= A.toArray()[i+2]-A.toArray()[i+3];
					C.toArray()[i+2] = B.toArray()[i] - B.toArray()[i+1];
					C.toArray()[i+3] = B.toArray()[i+2] - B.toArray()[i+3];
				}
			}
			break;
		case 3:
			for(int i=0;i<A.length;i+=4)
			{
				if(i+3 == A.length)
				{
					C.toArray()[i] = A.toArray()[i] - A.toArray()[i+1];
					C.toArray()[i+1] = B.toArray()[i] - B.toArray()[i+1];
					C.toArray()[i+2] = A.toArray()[i+3];
					C.toArray()[i+3] = B.toArray()[i+3];
				}
				else
				{						
					C.toArray()[i] = A.toArray()[i]-A.toArray()[i+1];
					C.toArray()[i+1]= A.toArray()[i+2]-A.toArray()[i+3];
					C.toArray()[i+2] = B.toArray()[i] - B.toArray()[i+1];
					C.toArray()[i+3] = B.toArray()[i+2] - B.toArray()[i+3];
				}
			}
			break;
				
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE3Functions#HSub(PackedFloat, PackedFloat)
	 */
	public void HSub(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		
		switch(A.length%4)
		{
		case 0:
			for(int i=0;i<A.length;i+=4)
			{
				C.toArray()[i] = A.toArray()[i]-A.toArray()[i+1];
				C.toArray()[i+1]= A.toArray()[i+2]-A.toArray()[i+3];
				C.toArray()[i+2] = B.toArray()[i] - B.toArray()[i+1];
				C.toArray()[i+3] = B.toArray()[i+2] - B.toArray()[i+3];
			}
			break;
		case 1:
			for(int i=0;i<A.length;i+=4)
			{
				if(i+1 == A.length)
				{
					C.toArray()[i] = A.toArray()[i];
					C.toArray()[i+1] = B.toArray()[i];
				}
				else
				{
					C.toArray()[i] = A.toArray()[i]-A.toArray()[i+1];
					C.toArray()[i+1]= A.toArray()[i+2]-A.toArray()[i+3];
					C.toArray()[i+2] = B.toArray()[i] - B.toArray()[i+1];
					C.toArray()[i+3] = B.toArray()[i+2] - B.toArray()[i+3];
				}
			}
			break;
		case 2:
			for(int i=0;i<A.length;i+=4)
			{
				if(i+2 == A.length)
				{
					C.toArray()[i] = A.toArray()[i] - A.toArray()[i+1];
					C.toArray()[i+1] = B.toArray()[i] - B.toArray()[i+1];
				}
				else
				{
					C.toArray()[i] = A.toArray()[i]-A.toArray()[i+1];
					C.toArray()[i+1]= A.toArray()[i+2]-A.toArray()[i+3];
					C.toArray()[i+2] = B.toArray()[i] - B.toArray()[i+1];
					C.toArray()[i+3] = B.toArray()[i+2] - B.toArray()[i+3];
				}
			}
			break;
		case 3:
			for(int i=0;i<A.length;i+=4)
			{
				if(i+3 == A.length)
				{
					C.toArray()[i] = A.toArray()[i] - A.toArray()[i+1];
					C.toArray()[i+1] = B.toArray()[i] - B.toArray()[i+1];
					C.toArray()[i+2] = A.toArray()[i+3];
					C.toArray()[i+3] = B.toArray()[i+3];
				}
				else
				{						
					C.toArray()[i] = A.toArray()[i]-A.toArray()[i+1];
					C.toArray()[i+1]= A.toArray()[i+2]-A.toArray()[i+3];
					C.toArray()[i+2] = B.toArray()[i] - B.toArray()[i+1];
					C.toArray()[i+3] = B.toArray()[i+2] - B.toArray()[i+3];
				}
			}
			break;
				
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#LessThan(PackedDouble, PackedDouble)
	 */
	public void LessThan(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= Double.longBitsToDouble(A.toArray()[i]<B.toArray()[i]?Long.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#LessThan(PackedFloat, PackedFloat)
	 */
	public void LessThan(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{			
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= Float.intBitsToFloat(A.toArray()[i]<B.toArray()[i]?Integer.MAX_VALUE:0);
		}
	}
	
	public void LessThanOrEqual(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= Double.longBitsToDouble(A.toArray()[i]<=B.toArray()[i]?Long.MAX_VALUE:0);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#LessThanOrEqual(PackedFloat, PackedFloat)
	 */
	public void LessThanOrEqual(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]= Float.intBitsToFloat(A.toArray()[i]<=B.toArray()[i]?Integer.MAX_VALUE:0);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#MAcc(PackedShort, PackedShort)
	 */
	public void MAcc(PackedShort A, PackedShort B, PackedInt C) throws LengthException
	{
		// multiply and accumulate 4 numbers from A by four number from B and
		// store in the same 4 slots of resultant.
		for(int i=0;i<A.length;i+=4)
		{

			if(i+1 > A.length)
				C.toArray()[i] = A.toArray()[i]*B.toArray()[i];
			else if(i+2 > A.length)
				C.toArray()[i] = C.toArray()[i+1] = A.toArray()[i]*B.toArray()[i]+A.toArray()[i+1]*B.toArray()[i+1];
			else if(i+3 > A.length)
				C.toArray()[i]= C.toArray()[i+1] = C.toArray()[i+2] = A.toArray()[i]*B.toArray()[i]+A.toArray()[i+1]*B.toArray()[i+1]+A.toArray()[i+2]*B.toArray()[i+2];
			else
				C.toArray()[i]= C.toArray()[i+1] = C.toArray()[i+2] = C.toArray()[i+3] = A.toArray()[i]*B.toArray()[i]+A.toArray()[i+1]*B.toArray()[i+1]+A.toArray()[i+2]*B.toArray()[i+2]+A.toArray()[i+3]*B.toArray()[i+3];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#MAcc(PackedDouble, PackedDouble)
	 */
	public void MAcc(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		// multiply and accumulate 2 doubles from A and B. Store result in 2
		// slots of return Array
		for(int i=0;i<A.length;i+=2)
		{
			if(i+1 > A.length)
			{
				C.toArray()[i] = A.toArray()[i]*B.toArray()[i];
			}
			else
			{
				C.toArray()[i]=C.toArray()[i+1] = A.toArray()[i]*B.toArray()[i]+A.toArray()[i+1]*B.toArray()[i+1];
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#MAcc(PackedFloat, PackedFloat)
	 */
	public void MAcc(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i+=4)
		{

			if(i+1 > A.length)
				C.toArray()[i] = A.toArray()[i]*B.toArray()[i];
			else if(i+2 > A.length)
				C.toArray()[i] = C.toArray()[i+1] = A.toArray()[i]*B.toArray()[i]+A.toArray()[i+1]*B.toArray()[i+1];
			else if(i+3 > A.length)
				C.toArray()[i]= C.toArray()[i+1] = C.toArray()[i+2] = A.toArray()[i]*B.toArray()[i]+A.toArray()[i+1]*B.toArray()[i+1]+A.toArray()[i+2]*B.toArray()[i+2];
			else
				C.toArray()[i]= C.toArray()[i+1] = C.toArray()[i+2] = C.toArray()[i+3] = A.toArray()[i]*B.toArray()[i]+A.toArray()[i+1]*B.toArray()[i+1]+A.toArray()[i+2]*B.toArray()[i+2]+A.toArray()[i+3]*B.toArray()[i+3];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Max(PackedByte, PackedByte)
	 */
	public void Max(PackedByte A, PackedByte B, PackedByte C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]>B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}	
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Max(PackedFloat, PackedFloat)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Max(PackedFloat, PackedFloat)
	 */
	public void Max(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]>B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}
	}
	public void Max(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]>B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Max(PackedShort, PackedShort)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Max(PackedShort, PackedShort)
	 */
	public void Max(PackedShort A, PackedShort B, PackedShort C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]>B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Max(PackedInt, PackedInt)
	 */
	public void Max(PackedInt A, PackedInt B, PackedInt C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]>B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Min(PackedByte, PackedByte)
	 */
	public void Min(PackedByte A, PackedByte B, PackedByte C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]<B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}
	}
	
	public void Min(PackedChar A, PackedChar B, PackedChar C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]<B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Min(PackedInt, PackedInt)
	 */
	public void Min(PackedInt A, PackedInt B, PackedInt C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]<B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}
	}
	public void Min(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]<B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Min(PackedFloat, PackedFloat)
	 */
	public void Min(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]<B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}	
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Min(PackedShort, PackedShort)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Min(PackedShort, PackedShort)
	 */
	public void Min(PackedShort A, PackedShort B, PackedShort C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]<B.toArray()[i]?A.toArray()[i]:B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Mult(PackedFloat, PackedFloat)
	 */
	public void Mult(PackedFloat A,PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]*B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Mult(PackedDouble, PackedDouble)
	 */
	public void Mult(PackedDouble A,PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]*B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Mult(PackedInt, PackedInt)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Mult(PackedInt, PackedInt)
	 */
	public void Mult(PackedInt A,PackedInt B, PackedLong C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]*B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#MultH(PackedShort, PackedShort)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#MultH(PackedShort, PackedShort)
	 */
	public void MultH( PackedShort A, PackedShort B, PackedShort C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			int newVal =A.toArray()[i]*B.toArray()[i]; 
			C.toArray()[i]= (short)(newVal/(Short.MAX_VALUE+1));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#MultL(PackedInt, PackedInt)
	 */
	public void MultL(PackedInt A, PackedInt B, PackedInt C)
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]= A.toArray()[i]*B.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#MultL(PackedShort, PackedShort)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#MultL(PackedShort, PackedShort)
	 */
	public void MultL(PackedShort A, PackedShort B, PackedShort C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			int newVal =A.toArray()[i]*B.toArray()[i]; 
			C.toArray()[i]= (short)(newVal%((int)Short.MAX_VALUE+1));
		}

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Nand(PackedFloat, PackedFloat)
	 */
	public void Nand(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = Float.intBitsToFloat((Float.floatToIntBits(A.toArray()[i])&Float.floatToIntBits(B.toArray()[i]))^Integer.MAX_VALUE);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Nand(PackedLong, PackedLong)
	 */
	public void Nand(PackedLong A, PackedLong B, PackedLong C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (A.toArray()[i]&B.toArray()[i])^Long.MAX_VALUE;
		}
	}
	
	public void NotEqual(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]=Double.longBitsToDouble(A.toArray()[i]!=B.toArray()[i]?Long.MAX_VALUE:0);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#NotEqual(PackedFloat, PackedFloat)
	 */
	public void NotEqual(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i = 0;i<A.length;i++)
		{
			C.toArray()[i]=Float.intBitsToFloat(A.toArray()[i]==B.toArray()[i]?Integer.MAX_VALUE:0);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Or(PackedDouble, PackedDouble)
	 */
	public void Or(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = Double.longBitsToDouble(Double.doubleToLongBits(A.toArray()[i])|Double.doubleToLongBits(B.toArray()[i]));
		}
	}
	
	public void Or(PackedInt A, Integer B, PackedInt C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = A.toArray()[i]|B;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Or(PackedFloat, PackedFloat)
	 */
	public void Or(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = Float.intBitsToFloat(Float.floatToIntBits(A.toArray()[i])|Float.floatToIntBits(B.toArray()[i]));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Or(PackedLong, PackedLong)
	 */
	public void Or(PackedLong A, PackedLong B, PackedLong C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
			C.toArray()[i] = A.toArray()[i]|B.toArray()[i];
	}
	
	public void Ordered(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
			C.toArray()[i] = (new Double(A.toArray()[i])).isNaN()||(new Double(B.toArray()[i])).isNaN()?0:Double.longBitsToDouble(Long.MAX_VALUE);		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Ordered(PackedFloat, PackedFloat)
	 */
	public void Ordered(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (new Float(A.toArray()[i])).isNaN()||(new Float(B.toArray()[i])).isNaN()?0:Float.intBitsToFloat(Integer.MAX_VALUE);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE42Functions#PopCount(int)
	 */
	public int PopCount(int A)
	{
		return PopCount((long)A);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE42Functions#PopCount(long)
	 */
	public int PopCount(long A)
	{
		long tmp = A;
		int C = 0;
		while(tmp > 0)
		{
			C += tmp&1;
			tmp/=2;
		}
		return C;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Recip(PackedFloat)
	 */
	public void Recip(PackedFloat A, PackedFloat C)
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = 1/A.toArray()[i];
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#RecipSqrt(PackedFloat)
	 */
	public void RecipSqrt(PackedFloat A, PackedFloat C)
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (float)(Math.pow(A.toArray()[i], -0.5));
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Round(PackedDouble, int)
	 */
	public void Round(PackedDouble A, int control, PackedDouble C)
	{
		// need to figure out how control works
		throw(new IllegalArgumentException("Not Implemented"));
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE41Functions#Round(PackedFloat, int)
	 */
	public void Round(PackedFloat A,int control, PackedFloat C)
	{
		throw(new IllegalArgumentException("Not Implemented"));
	}
	public void Sad(PackedChar A,PackedChar B, PackedShort C)  throws LengthException
	{
		// PSADBW instructions when using 64-bit operands:
		// TEMP0 �? ABS(DEST.toArray()[7-0] − SRC.toArray()[7-0]);
		// * repeat operation for bytes 2 through 6 *;
		// TEMP7 �? ABS(DEST.toArray()[63-56] − SRC.toArray()[63-56]);
		// DEST.toArray()[15:0] �? SUM(TEMP0...TEMP7);
		// DEST.toArray()[63:16] �? 000000000000H;

		char[] tmp = {0,0,0,0,0,0,0,0};
		int cnt = 0;

		for(int i = 0;i<A.length;i++)
		{
			tmp[i%8] = (char) ((A.toArray()[i]<B.toArray()[i])?B.toArray()[i]-A.toArray()[i]:A.toArray()[i]-B.toArray()[i]);
			cnt++;
			if(cnt == 8)
			{
				C.toArray()[i/8] = (short) (tmp[0]+tmp[1]+tmp[2]+tmp[3]+tmp[4]+tmp[5]+tmp[6]+tmp[7]);
				cnt = 0;
				tmp = new char[]{0,0,0,0,0,0,0,0};
			}
		}
		C.toArray()[C.length-1] = (short) (tmp[0]+tmp[1]+tmp[2]+tmp[3]+tmp[4]+tmp[5]+tmp[6]+tmp[7]);
	}
	
	public void Shuf(PackedDouble A, PackedDouble B, int mask, PackedDouble C) throws LengthException
	{
		boolean[] submask = {(mask&0x2)>0,(mask&0x1)>0};
		for(int i=0; i< A.length; i++)
		{
			if(i%2 == 0)
			{
				C.toArray()[i] = submask[1]?((i+1<B.length)?B.toArray()[i+1]:0):B.toArray()[i];
			}
			if(i%2 == 1)
			{
				C.toArray()[i] = submask[0]?((i+1<A.length)?A.toArray()[i+1]:0):A.toArray()[i];
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Shuf(PackedFloat, PackedFloat, int)
	 */
	public void Shuf(PackedFloat A, PackedFloat B, int mask, PackedFloat C) throws LengthException
	{
		// Mask: Bits 0,1 select the value to be moved from the destination
		// operand to the destination operand low bits.
		// Bits 2,3 select the value to be moved from the destination operand to
		// the destination operand 2nd doubleword
		// Bits 4,5 select the value to be moved from the source operand to the
		// destination operand 3rd doubleword.
		// Bits 6,7 select the value to be moved from the source operand to the
		// destination operand hight bits.
		// CASE (SELECT..toArray()[1-0]) OF
		// 0: DEST.toArray()[31-0] DEST.toArray()[31-0];
		// 1: DEST.toArray()[31-0] DEST.toArray()[63-32];
		// 2: DEST.toArray()[31-0] DEST.toArray()[95-64];
		// 3: DEST.toArray()[31-0] DEST.toArray()[127-96];
		// ESAC;
		// CASE (SELECT..toArray()[3-2]) OF
		// 0: DEST.toArray()[63-32] DEST.toArray()[31-0];
		// 1: DEST.toArray()[63-32] DEST.toArray()[63-32];
		// 2: DEST.toArray()[63-32] DEST.toArray()[95-64];
		// 3: DEST.toArray()[63-32] DEST.toArray()[127-96];
		// ESAC;
		// CASE (SELECT..toArray()[5-4]) OF
		// 0: DEST.toArray()[95-64] SRC.toArray()[31-0];
		// 1: DEST.toArray()[95-64] SRC.toArray()[63-32];
		// 2: DEST.toArray()[95-64] SRC.toArray()[95-64];
		// 3: DEST.toArray()[95-64] SRC.toArray()[127-96];
		// ESAC;
		// CASE (SELECT..toArray()[7-6]) OF
		// 0: DEST.toArray()[127-96] SRC.toArray()[31-0];
		// 1: DEST.toArray()[127-96] SRC.toArray()[63-32];
		// 2: DEST.toArray()[127-96] SRC.toArray()[95-64];
		// 3: DEST.toArray()[127-96] SRC.toArray()[127-96];
		// ESAC;
		boolean[] submask = {(mask&0x80)>0,(mask&0x40)>0,(mask&0x20)>0,(mask&0x10)>0,(mask&0x08)>0,(mask&0x04)>0,(mask&0x02)>0,(mask&0x01)>0};
		for(int i=0; i< A.length; i++)
		{
			if(i%4 == 0)
			{
				if(!submask[0] && !submask[1])
				{
					C.toArray()[i] = B.toArray()[i];
				}
				if(!submask[0]&&submask[1])
				{
					C.toArray()[i] = i+1<B.length?B.toArray()[i+1]:0;
				}
				if(submask[0]&&!submask[1])
				{
					C.toArray()[i] = i+2<B.length?B.toArray()[i+2]:0;
				}
				if(submask[0]&&submask[1])
				{
					C.toArray()[i] = i+3<B.length?B.toArray()[i+3]:0;
				}
			}
			if(i%4 == 1)
			{
				if(!submask[2] && !submask[3])
				{
					C.toArray()[i] = B.toArray()[i];
				}
				if(!submask[2]&&submask[3])
				{
					C.toArray()[i] = i+1<B.length?B.toArray()[i+1]:0;
				}
				if(submask[2]&&!submask[3])
				{
					C.toArray()[i] = i+2<B.length?B.toArray()[i+2]:0;
				}
				if(submask[2]&&submask[3])
				{
					C.toArray()[i] = i+3<B.length?B.toArray()[i+3]:0;
				}
			}
			if(i%4 == 2)
			{
				if(!submask[4] && !submask[5])
				{
					C.toArray()[i] = A.toArray()[i];
				}
				if(!submask[4]&&submask[5])
				{
					C.toArray()[i] = i+1<A.length?A.toArray()[i+1]:0;
				}
				if(submask[4]&&!submask[5])
				{
					C.toArray()[i] = i+2<A.length?A.toArray()[i+2]:0;
				}
				if(submask[4]&&submask[5])
				{
					C.toArray()[i] = i+3<A.length?A.toArray()[i+3]:0;
				}
			}
			if(i%4 == 3)
			{
				
				if(!submask[6] && !submask[7])
				{
					C.toArray()[i] = A.toArray()[i];
				}
				if(!submask[6]&&submask[7])
				{
					C.toArray()[i] = i+1<A.length?A.toArray()[i+1]:0;
				}
				if(submask[6]&&!submask[7])
				{
					C.toArray()[i] = i+2<A.length?A.toArray()[i+2]:0;
				}
				if(submask[6]&&submask[7])
				{
					C.toArray()[i] = i+3<A.length?A.toArray()[i+3]:0;
				}
			}
		}
	}
	
// public PackedInt Shuf(PackedInt A, PackedInt B, int mask);
// public PackedShort Shuf(PackedShort A, PackedShort B, int mask);
// public PackedShort Shufh(PackedShort A, PackedShort B, int mask);
// public PackedShort Shufl(PackedShort A, PackedShort B, int mask);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SLL(PackedInt, int)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#SLL(PackedInt, int)
	 */
	public void SLL(PackedInt A, int count, PackedInt C)
	{
		
		for(int i=0;i<A.length;i++)
			C.toArray()[i] = A.toArray()[i]<<count;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SLL(PackedLong, int)
	 */
	public void SLL(PackedLong A, int count, PackedLong C)
	{
		
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = A.toArray()[i]<<count;
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SLL(PackedShort, int)
	 */
	public void SLL(PackedShort A, int count, PackedShort C)
	{
		for(int i=0;i<A.length;i++)
			C.toArray()[i] = (short)(A.toArray()[i]<<count);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Sqrt(PackedDouble)
	 */
	public void Sqrt(PackedDouble A, PackedDouble C)
	{
		for(int i=0;i<A.length;i++)
			C.toArray()[i] = Math.sqrt(A.toArray()[i]);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Sqrt(PackedFloat)
	 */
	public void Sqrt(PackedFloat A, PackedFloat C)
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (float)Math.sqrt(A.toArray()[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SRA(PackedInt, int)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#SRA(PackedInt, int)
	 */
	public void SRA(PackedInt A, int count, PackedInt C)
	{
		for(int i=0;i<A.length;i++)
			C.toArray()[i] = A.toArray()[i]>>count;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SRA(PackedLong, int)
	 */
	public void SRA(PackedLong A, int count, PackedLong C)
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = A.toArray()[i]>>count;
		}
	}
	
	public void SRA(PackedShort A, int count, PackedShort C)
	{
		for(int i=0;i<A.length;i++)
		{
			short shifty = (short) (A.toArray()[i]>>count);
			C.toArray()[i] = shifty;
//			C.setContents(i, shifty);
//			C.contents[i]= shifty;
//			if(i==12159)
//			{
//				System.out.println(A.toArray()[12159] + " >> " + count);
//				System.out.println(" = "+(C.toArray()[12159]));
//				System.exit(0);
//			}
		}

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SRL(PackedInt, int)
	 */
	public void SRL(PackedInt A, int count, PackedInt C)
	{
		for(int i=0;i<A.length;i++)
			C.toArray()[i] = A.toArray()[i]>>>count;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SRL(PackedLong, int)
	 */
	public void SRL(PackedLong A, int count, PackedLong C)
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = A.toArray()[i]>>>count;
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SRL(PackedShort, int)
	 */
	public void SRL(PackedShort A, int count, PackedShort C)  throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (short) (A.toArray()[i]>>>count);
		}
	
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Sub(PackedByte, PackedByte)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Sub(PackedByte, PackedByte)
	 */
	public void Sub(PackedByte A, PackedByte B, PackedByte C)  throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=(byte) (A.toArray()[i]-B.toArray()[i]);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Sub(PackedDouble, PackedDouble)
	 */
	public void Sub(PackedDouble A, PackedDouble B, PackedDouble C)  throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]= (A.toArray()[i]-B.toArray()[i]);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Sub(PackedFloat, PackedFloat)
	 */
	public void Sub(PackedFloat A, PackedFloat B, PackedFloat C)  throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=(A.toArray()[i]-B.toArray()[i]);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Sub(PackedInt, PackedInt)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Sub(PackedInt, PackedInt)
	 */
	public void Sub(PackedInt A, PackedInt B, PackedInt C) throws IllegalArgumentException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]-B.toArray()[i];
		}
	}
	
	
	public void Sub(PackedLong A, PackedLong B, PackedLong C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=(A.toArray()[i]-B.toArray()[i]);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#Sub(PackedShort, PackedShort)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#Sub(PackedShort, PackedShort)
	 */
	public void Sub(PackedShort A, PackedShort B, PackedShort C)  throws LengthException
	{ 
		for (int i = 0; i < A.length; i++) 
		{
			C.toArray()[i] = (short) (A.toArray()[i] - B.toArray()[i]);			
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SubS(PackedByte, PackedByte)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#SubS(PackedByte, PackedByte)
	 */
	public void SubS(PackedByte A, PackedByte B, PackedByte C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]>B.toArray()[i]?(byte) (A.toArray()[i]-B.toArray()[i]):0;
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SubS(PackedChar, PackedChar)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#SubS(PackedChar, PackedChar)
	 */
	public void SubS(PackedChar A, PackedChar B, PackedChar C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]>B.toArray()[i]?(char) (A.toArray()[i]-B.toArray()[i]):0;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#SubS(PackedShort, PackedShort)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#SubS(PackedShort, PackedShort)
	 */
	public void SubS(PackedShort A, PackedShort B, PackedShort C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i]=A.toArray()[i]>B.toArray()[i]?(short) (A.toArray()[i]-B.toArray()[i]):0;
		}
		
	}
	
	public void Unordered(PackedDouble A, PackedDouble B, PackedDouble C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
			C.toArray()[i] = (new Double(A.toArray()[i])).isNaN()||(new Double(B.toArray()[i])).isNaN()?Double.longBitsToDouble(Long.MAX_VALUE):0;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#Unordered(PackedFloat, PackedFloat)
	 */
	public void Unordered(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (new Float(A.toArray()[i])).isNaN()||(new Float(B.toArray()[i])).isNaN()?Float.intBitsToFloat(Integer.MAX_VALUE):0;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSE2Functions#XOr(PackedDouble, PackedDouble)
	 */
	public void XOr(PackedDouble A,PackedDouble B, PackedDouble C)  throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = Double.longBitsToDouble(Double.doubleToLongBits(A.toArray()[i])^Double.doubleToLongBits(B.toArray()[i]));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.SSEFunctions#XOr(PackedFloat, PackedFloat)
	 */
	public void XOr(PackedFloat A, PackedFloat B, PackedFloat C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = Float.intBitsToFloat(Float.floatToIntBits(A.toArray()[i])^Float.floatToIntBits(B.toArray()[i]));
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see jSIMD.MMXFunctions#XOr(PackedLong, PackedLong)
	 */
	public void XOr(PackedLong A, PackedLong B, PackedLong C) throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = A.toArray()[i]^B.toArray()[i];
		}
	}

	public void clear() {
		PackedArray.PackedObjectArray=new Vector<Object>();
		PackedInt.maxSize=0;
		PackedByte.maxSize=0;
		PackedChar.maxSize=0;
		PackedDouble.maxSize=0;
		PackedFloat.maxSize=0;
		PackedShort.maxSize=0;
		PackedLong.maxSize=0;
		PackedByte.count = 0;
		PackedChar.count = 0;
		PackedDouble.count = 0;
		PackedFloat.count = 0;
		PackedInt.count = 0;
		PackedLong.count = 0;
		PackedShort.count = 0;
		el = new ExecutionList();		 
	}
	
	public void Execute() {
		// TODO Auto-generated method stub
		el = new ExecutionList();
		//if(!this.isUnknownProcessor)
			
	}



	public void And(PackedInt A, int arg, PackedInt C)  throws LengthException
	{
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (A.toArray()[i])&arg;
		}
		
	}

	public void Or(PackedInt A, int arg, PackedInt C)  throws LengthException
	{		
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (A.toArray()[i])|arg;
		}
		
	}

	public void Or(PackedInt A, PackedInt B, PackedInt C)  throws LengthException
	{		
		for(int i=0;i<A.length;i++)
		{
			C.toArray()[i] = (A.toArray()[i])|(B.toArray()[i]);
		}
	}

}
