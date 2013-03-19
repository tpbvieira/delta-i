package stackSample;

public class InterfaceGenerator {
	
	static native boolean updateAbstractFSM(int[] optsPre, int executedMethod, int[] optPost, boolean exception);	
	static native boolean updateConcreteFSM(String strPre, int mId, String strPost, boolean exception);
	
}
