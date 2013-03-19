package jSIMD;

import java.util.ArrayList;
import java.util.Vector;

public class PackedInt extends PackedArray{
	private int[] contents;
	public int index=0;
	public int length;
	public final int type = 4;
	
	public PackedInt(int[] cont)
	{
		contents = cont;
		length = lastlength = cont.length;
		index = count++;
		if(PackedObjectArray== null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public PackedInt()
	{
		contents = new int[lastlength];
		length = lastlength;
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public PackedInt(int size)
	{
		contents = new int[size];
		length = lastlength = size;	
		index = count++;
		if(PackedObjectArray == null)
		{
			PackedObjectArray  = new Vector<Object>();
		}
		PackedObjectArray.add(contents);
		maxSize= (maxSize>lastlength)?maxSize:lastlength;
	}
	public int[] toArray()
	{
		return contents;
	}
	
	public ArrayList<Integer> toList()
	{
		ArrayList<Integer> v = new ArrayList<Integer>(); 

		for(int elem:contents)
			v.add(elem);
		return v;
	}
}