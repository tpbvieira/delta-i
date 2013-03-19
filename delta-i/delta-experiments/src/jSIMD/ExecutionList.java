package jSIMD;

import java.util.Vector;

public class ExecutionList {//implements iterator	
	Vector<ExecutionItem> Items;
	Vector<Object> ObjectArrays;
	
	int ObjectArraysMaxSize;
	int counter = 0;

	public void populateVectors(){
		ObjectArrays = PackedArray.PackedObjectArray;
		ObjectArraysMaxSize = PackedArray.maxSize;
	}
	
	public ExecutionList(){
	}
	
	public void Add(ExecutionItem item){
		if(Items == null){
			Items = new Vector<ExecutionItem>();
		}
		Items.add(item);
	}
	
	public boolean hasNext(){
		return counter < Items.size();
	}

	public ExecutionItem next(){
		return Items.get(counter++); 
	}

	public void remove(){
		Items.remove(0);
	}
}