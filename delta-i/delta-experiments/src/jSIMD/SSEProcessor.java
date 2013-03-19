package jSIMD;


public class SSEProcessor extends Processor implements SSEFunctions {

//	private native void SendSourceAList(Vector<Integer> SourceA);
//	private native void SendSourceBList(Vector<Integer> SourceB);
//	private native void SendDestinationList(Vector<Integer> Destination);
//	private native void SendExecutionList(Vector<Integer> ExecutionList);
//	private native void SendByteArray(byte[] A, int size);
//	private native void SendCharArray(char[] A, int size);
//	private native void SendDoubleArray(double[] A, int size);
//	private native void SendFloatArray(float[] A, int size);
//	private native void SendIntArray(int[] A, int size);
//	private native void SendLongArray(long[]A, int size);
//	private native void SendShortArray(short[] A, int size);
//	private native void Execute();
	
	//static{System.loadLibrary("SSELibrary");}
	
	@Override
	public void Unpack()
	{
		simdMapper.NativeLibrary.Execute(el);
//		//send the arrays
//		for(int i=0;i<PackedByte.PackedByteArray.size();i++)
//		{
//			simdMapper.NativeLibrary.SendByteArray(PackedByte.PackedByteArray.get(i), PackedByte.count);
//		}
//		for(int i=0;i<PackedChar.PackedCharArray.size();i++)
//		{
//			simdMapper.NativeLibrary.SendCharArray(PackedChar.PackedCharArray.get(i), PackedChar.count);
//		}
//		for(int i=0;i<PackedDouble.PackedDoubleArray.size();i++)
//		{
//			simdMapper.NativeLibrary.SendDoubleArray(PackedDouble.PackedDoubleArray.get(i), PackedDouble.count);
//		}
//		for(int i=0;i<PackedFloat.PackedFloatArray.size();i++)
//		{
//			simdMapper.NativeLibrary.SendFloatArray(PackedFloat.PackedFloatArray.get(i), PackedFloat.count);
//		}
//		for(int i=0;i<PackedInt.PackedIntArray.size();i++)
//		{
//			simdMapper.NativeLibrary.SendIntArray(PackedInt.PackedIntArray.get(i), PackedInt.count);
//		}
//		for(int i=0;i<PackedLong.PackedLongArray.size();i++)
//		{
//			simdMapper.NativeLibrary.SendLongArray(PackedLong.PackedLongArray.get(i), PackedLong.count);
//		}
//		for(int i=0;i<PackedShort.PackedShortArray.size();i++)
//		{
//			simdMapper.NativeLibrary.SendShortArray(PackedShort.PackedShortArray.get(i), PackedShort.count);
//		}
//		//send the lists to be processed
////		simdMapper.NativeLibrary.SendSourceAList(SourceA);
////		simdMapper.NativeLibrary.SendSourceBList(SourceB);
////		simdMapper.NativeLibrary.SendDestinationList(Destination);
////		simdMapper.NativeLibrary.SendExecutionList(ExecutionList);
	}
	
	@Override
	public void Add(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
//		SourceA.add(A.index);
//		SourceB.add(B.index);
//		Destination.add(C.index);
//		ExecutionList.add(0);
	}

	@Override
	public void And(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public char Avg(PackedChar A) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void Divide(PackedFloat A, PackedFloat B, PackedFloat C) {
		
		// TODO Auto-generated method stub
	}

	@Override
	public void Equal(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void GreaterThan(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void GreaterThanOrEqual(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void LessThan(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void LessThanOrEqual(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Max(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Max(PackedShort A, PackedShort B, PackedShort C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Min(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Min(PackedShort A, PackedShort B, PackedShort C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Mult(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Nand(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void NotEqual(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Or(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Ordered(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Recip(PackedFloat A, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void RecipSqrt(PackedFloat A, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Shuf(PackedFloat A, PackedFloat B, int mask, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Sqrt(PackedFloat A, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Sub(PackedFloat A, PackedFloat B,PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Unordered(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void XOr(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

}
