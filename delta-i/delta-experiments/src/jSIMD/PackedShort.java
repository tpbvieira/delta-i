package jSIMD;

import java.util.Vector;

public class PackedShort extends PackedArray{
	public short[] contents;
	public int index=0;
	public int length;
	public final int type = 6;

	
//	public synchronized void setContents(int index, short data)
//	{
//		contents[index]=data;
//		return;
//	}
	
	public PackedShort(short[] cont)
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
	public PackedShort()
	{
		contents = new short[lastlength];
		length = lastlength;
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
	}
	public PackedShort(int size)
	{
		length = lastlength = size;
		contents = new short[size];
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public short[] toArray()
	{
		return contents;
//		return PackedShortArray.elementAt(this.index);
	}
}