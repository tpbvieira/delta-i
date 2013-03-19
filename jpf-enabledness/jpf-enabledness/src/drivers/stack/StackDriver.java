package drivers.stack;

import gov.nasa.jpf.jvm.Verify;

import java.util.ArrayList;
import java.util.List;

import nativemap.InterfaceGenerator;


public class StackDriver {
	
	private static final boolean EA = true;	
	private static final int SEQ_BOUND = 2;

	public static void main(String[] args) {
		StackSubject stack = new StackSubject();
		
		/*
		 * i denotes the i_th method call
		 * against one stack object
		 */
		for (int i = 1; i <= SEQ_BOUND; i++) {
			
			boolean raisedException = false;			
			int index;
			int method;			
			
			/*
			 * verify what transitions(method) are enabled previously 
			 */
			int[] optsPre = getEnableOperations(stack);
			if(optsPre.length > 0){		
				index = Verify.getInt(0, optsPre.length - 1);
				method = optsPre[index];
			}else{
				continue;// if there is not method enabled, go to new iteration
			}			
			String strPre = stack.toString();
			
			/*
			 * execute each enabled methods 
			 */
			try {
				switch (method) {
				case 0:
					stack.pop();
					break;
				case 2:
					stack.push(1);
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

			/*
			 * verify what transitions(method) are enabled after method execution and update saved states
			 */
			if (EA) {
				int[] optsPost = getEnableOperations(stack);
				InterfaceGenerator.updateAbstractFSM(StackSubject.class.getName(), optsPre, method, optsPost, raisedException, EA);
//				Verify.ignoreIf(!updated);
			} else {
				String strPost = stack.toString();
				InterfaceGenerator.updateConcreteFSM(StackSubject.class.getName(), strPre, method, strPost, raisedException, EA);
			}

		}
		
	}
	
	
	/**
	 * get enabled methods for one state of StackSubject
	 */
	private static int[] getEnableOperations(StackSubject state) {
		
		List<Integer> opts = new ArrayList<Integer>();
		if (state.popPre()) {
			opts.add(new Integer(0));
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