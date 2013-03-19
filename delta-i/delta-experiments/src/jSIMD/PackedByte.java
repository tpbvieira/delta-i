package jSIMD;

import java.util.Vector;

public class PackedByte extends PackedArray {
	private byte[] contents;
	public int length;
	public int index;
	public final int type = 0;
	
	//public static Vector<byte[]> PackedByteArray;
		
	public PackedByte(byte[] cont)
	{
		contents = cont;
		length = cont.length;
		lastlength = cont.length;

		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public PackedByte(int size)
	{
		length = lastlength = size;
		contents = new byte[size];
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public PackedByte()
	{
		contents = new byte[lastlength];
		length = lastlength;
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public byte[] toArray()
	{
		return contents;
	}
}