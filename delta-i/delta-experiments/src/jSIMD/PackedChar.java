package jSIMD;

import java.util.Vector;

public class PackedChar extends PackedArray
{
	private char[] contents;
	public int index=0;
	public int length;
	public final int type = 1;
	
	public PackedChar(char[] cont)
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
	public PackedChar(int size)
	{
		length = lastlength = size;
		contents = new char[size];
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public PackedChar()
	{
		contents = new char[length = lastlength];
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
		
	}
	public char[] toArray()
	{
		return contents;
	}
}