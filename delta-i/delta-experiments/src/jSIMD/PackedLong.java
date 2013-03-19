package jSIMD;

import java.util.Vector;

public class PackedLong extends PackedArray{
	private long[] contents;
	public int index=0;
	public int length;
	public final int type = 5;
	public PackedLong(long[] cont)
	{
		contents = cont;
		length = lastlength = cont.length;
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public PackedLong(int size)
	{
		contents = new long[size];
		length = lastlength = size;	
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public PackedLong()
	{
		contents = new long[lastlength];
		length = lastlength;		
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public long[] toArray()
	{
		return contents;
	}
}