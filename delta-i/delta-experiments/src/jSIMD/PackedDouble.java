package jSIMD;

import java.util.Vector;

public class PackedDouble extends PackedArray{
	private double[] contents;
	public int index=0;
	public int length;
	public final int type = 2;

	public PackedDouble(double[] cont)
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
	public PackedDouble(int size)
	{
		contents = new double[size];
		length = lastlength = size;
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public PackedDouble()
	{
		contents = new double[length= lastlength];
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
	}
	public double[] toArray()
	{
		return contents;
	}
}