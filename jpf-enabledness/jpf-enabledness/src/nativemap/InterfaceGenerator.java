package nativemap;

public class InterfaceGenerator { 
	
	public static native boolean updateAbstractFSM(String clsName, int[] optsPre, int executedMethod, int[] optPost, boolean exception, boolean abstractMode);	
	public static native boolean updateConcreteFSM(String clsName, String strPre, int mId, String strPost, boolean exception, boolean abstractMode);
	
}