package test;

import simdMapper.NativeLibrary;


public class NativeReferenceCopyTest {

	private int arraySize = 3000000;
	public Integer[] varInteger;

	public NativeReferenceCopyTest(){
		varInteger = new Integer[arraySize];		
		for (int j = 0; j < arraySize; j++) {
			varInteger[j] = new Integer(Integer.MAX_VALUE);
		}	
	}
	
	public NativeReferenceCopyTest(Integer[] obj){
		varInteger = obj;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start,end;
		NativeReferenceCopyTest testObject;
			
		start = System.nanoTime();
		testObject = new NativeReferenceCopyTest();
		end = System.nanoTime();
		System.out.print("JavaInstantiationTime= [" + (end-start)+"] ");
		start = System.nanoTime();
		testObject = new NativeReferenceCopyTest();
		end = System.nanoTime();
		System.out.println("[" + (end-start)+"] ");
			
		start = System.nanoTime();
		testObject = (NativeReferenceCopyTest)testObject.clone();
		end = System.nanoTime();
		System.out.print("JavaCloneTime [" + (end-start)+"] ");
		start = System.nanoTime();
		testObject = (NativeReferenceCopyTest)testObject.clone();
		end = System.nanoTime();
		System.out.println("[" + (end-start)+"] ");
		
		start = System.nanoTime();
		NativeLibrary.nativeReferenceCopyTest1(testObject);
		end = System.nanoTime();
		System.out.print("NativeTimeCallWithObject [" + (end-start)+"] ");
		start = System.nanoTime();
		NativeLibrary.nativeReferenceCopyTest1(testObject);
		end = System.nanoTime();
		System.out.println("[" + (end-start)+"] ");
				
		start = System.nanoTime();
		NativeLibrary.nativeReferenceCopyTest1(null);
		end = System.nanoTime();
		System.out.print("NativeTimeCallWithObjectNull [" + (end-start)+"] ");
		start = System.nanoTime();
		NativeLibrary.nativeReferenceCopyTest1(null);
		end = System.nanoTime();
		System.out.println("[" + (end-start)+"] ");
		
		start = System.nanoTime();
		NativeLibrary.nativeReferenceCopyTest2(testObject);
		end = System.nanoTime();
		System.out.print("NativeTimeCallGettingArray [" + (end-start)+"] ");
		start = System.nanoTime();
		NativeLibrary.nativeReferenceCopyTest2(testObject);
		end = System.nanoTime();
		System.out.println("[" + (end-start)+"] ");
		
		start = System.nanoTime();
		NativeLibrary.nativeReferenceCopyTest3(testObject);
		end = System.nanoTime();
		System.out.print("NativeTimeCallIteratingArray [" + (end-start)+"] ");
		start = System.nanoTime();
		NativeLibrary.nativeReferenceCopyTest3(testObject);
		end = System.nanoTime();
		System.out.println("[" + (end-start)+"] ");
		
		NativeReferenceCopyTest tmp = testObject;
		start = System.nanoTime();
		NativeLibrary.nativeReferenceCopyCompareReferenceTest(testObject, tmp);
		end = System.nanoTime();
		System.out.println("NativeTimeReferenceCompare [" + (end-start)+"] ");
				
		tmp = (NativeReferenceCopyTest)testObject.clone();
		start = System.nanoTime();
		NativeLibrary.nativeReferenceCopyCompareCloneTest(testObject, tmp);
		end = System.nanoTime();
		System.out.println("NativeTimeCloneCompare [" + (end-start)+"] ");
	}
	
	public Integer[] getVarInteger() {
		return varInteger;
	}

	public void setVarInteger(Integer[] varInteger) {
		this.varInteger = varInteger;
	}

	public int getArraySize() {
		return arraySize;
	}

	public void setArraySize(int arraySize) {
		this.arraySize = arraySize;
	}

	public boolean equals(Object obj){
		NativeReferenceCopyTest ob = (NativeReferenceCopyTest)obj;
		boolean isEqual = false;
		Integer[] aux = ob.getVarInteger();
		
		for (int i = 0; i < varInteger.length; i++) {
			isEqual = isEqual && (varInteger[i] == aux[i]);
		}
		
		return ob.getVarInteger().equals(ob.getVarInteger());
	}
	
	protected Object clone(){		
		Integer[] tmp = new Integer[varInteger.length];		
		System.arraycopy(varInteger, 0, tmp, 0, varInteger.length);
		
		return new NativeReferenceCopyTest(tmp);
	}
}
