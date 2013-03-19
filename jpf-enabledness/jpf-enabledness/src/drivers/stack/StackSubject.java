package drivers.stack;

import java.util.Arrays;


public class StackSubject {

	private final static int MSIZE = 3; 
	private int currPos = 0;
	private int[] val = new int[MSIZE];
//	private int depth=1;

	public void push(int a) {
		if (!pushPre()) {
			throw new RuntimeException("cannot push; full stack");
		}
		val[currPos++] = a;
//		System.out.println("## Push:"+currPos+":"+depth);		
//		depth++;
	}

	public void pop() {
		if (!popPre()) {
			throw new RuntimeException("cannot pop; empty stack");
		}
		val[--currPos] = 0;
//		System.out.println("## Pop:"+currPos+":"+depth);
//		depth++;
	}

	public boolean pushPre() {		
		return currPos >= 0 && currPos < MSIZE;
	}

	public boolean popPre() {
		return currPos > 0 && currPos <= MSIZE;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("currPost="+currPos);
		sb.append("\\n");
		sb.append("val="+Arrays.toString(val));
		return sb.toString();
	}

}