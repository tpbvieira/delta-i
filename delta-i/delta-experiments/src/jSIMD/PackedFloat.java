package jSIMD;

import java.util.Vector;


public class PackedFloat extends PackedArray{
	private float[] contents;
	public int index=0;
	public int length;
	public final int type = 3;

	
	public PackedFloat(float[] cont)
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
	public PackedFloat(int size)
	{
		contents = new float[size];
		length = lastlength = size;
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public PackedFloat()
	{
		contents = new float[lastlength];
		length = lastlength;
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
	}
	public float[] toArray()
	{
		return contents;
	}
}