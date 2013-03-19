package stackSample;

import gov.nasa.jpf.jvm.Verify;

import java.util.ArrayList;
import java.util.List;

public class StackDriver {
	
	public static final boolean EA = true;	
	public static final int SEQ_BOUND = 5;

	public static void main(String[] args) {
		Stack state = new Stack();
		
		/*
		 * i denotes the i_th method call
		 * against one stack object
		 */
		for (int i = 1; i <= SEQ_BOUND; i++) {
			
			boolean raisedException = false;
			boolean updated;
			int index;
			int method;
			
			int[] optsPre = getEnableOperations(state);
			if(optsPre.length > 0){		
				index = Verify.getInt(0, optsPre.length - 1);
				method = optsPre[index];
			}else{
				continue;
			}
			
			String strPre = state.toString();
						
			try {
				switch (method) {
				case 1:
					state.pop();
					break;
				case 2:
					state.push(1);
					break;
				default:
					break;
				}
			} catch (RuntimeException _) {
				raisedException = true;
				_.printStackTrace();
			} catch (Exception _) {
				raisedException = true;
				_.printStackTrace();
			}

			String strPost = state.toString();
			
			if (EA) {
				int[] optsPost = getEnableOperations(state);
				updated = InterfaceGenerator.updateAbstractFSM(optsPre, method, optsPost, raisedException);
//				Verify.ignoreIf(!updated);
			} else {
				updated = InterfaceGenerator.updateConcreteFSM(strPre, method, strPost, raisedException);
			}

		}
		
	}
	
	private static int[] getEnableOperations(Stack state) {
		
		List<Integer> opts = new ArrayList<Integer>();
		if (state.popPre()) {
			opts.add(new Integer(1));
		}
		if (state.pushPre()) {
			opts.add(new Integer(2));
		}
		
		/**
		 * mount array of enabled operations
		 */
		int[] optsEnabled = new int[opts.size()];
		for (int k = 0; k < optsEnabled.length; k++) {
			optsEnabled[k] = opts.get(k).intValue();
		}
		return optsEnabled;
	}

}