package drivers.vector;

import gov.nasa.jpf.jvm.Verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import nativemap.InterfaceGenerator;

public class VectorDriver {

	private static final boolean EA = true;	
	private static final int SEQ_BOUND = 2;

	public static void main(String[] args) {
		//	***Used to get ID and name of each declared method
		//				Method[] methods = EnablednessUtil.getMethods(Vector.class);
		//				for (int i = 0; i < methods.length; i++) {
		//					System.out.println(i +" - " + methods[i].getName());
		//				}

		VectorSubject<Integer> vector = new VectorSubject<Integer>();
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
			int[] optsPre = getEnableOperations(vector);
			if(optsPre.length > 0){		
				index = Verify.getInt(0, optsPre.length - 1);
				method = optsPre[index];
			}else{
				continue;
			}

			String strPre = vector.toString();
			Integer n1 = new Integer(1);
			Integer n2 = new Integer(2);
			Integer n3 = new Integer(3);
			Collection<Integer> c1 = new ArrayList<Integer>();
			c1.add(n1);
			c1.add(n2);

			/*
			 * execute each enabled methods 
			 */
			try {
				switch (method) {

				case 0:					
					vector.add(n1);
					break;
				case 1:
					Random random = new Random();
					index = random.nextInt(vector.size());					
					vector.add(1, n2);
					break;
				case 2:					
					vector.addAll(c1);
					break;
				case 3:
					Random rndm = new Random();
					index = rndm.nextInt(vector.size());					
					vector.addAll(index, c1);
					break;
				case 6:					
					vector.addElement(n3);
					break;
				case 10:
					vector.capacity();
					break;
				case 12:
					vector.clear();
					break;
				case 15:
					vector.contains(new String("vector.contains"));
					break;
				case 16:
					vector.containsAll(c1);
					break;
				case 19:
					Integer[] anArray = new Integer[0];
					vector.copyInto(anArray);
					break;
				case 21:
					vector.elementAt(0);
					break;
				case 23:
					vector.elements();
					break;
				case 25:
					vector.ensureCapacity(4);
					break;
				case 29:
					vector.firstElement();
					break;
				case 31:
					vector.get(0);
					break;
				case 34:
					vector.indexOf(new String("vector.addAll"));
					break;
				case 35:
					vector.indexOf(new String("vector.addAll"), 0);
					break;
				case 38:
					vector.insertElementAt(n1, 0);
					break;
				case 40:
					vector.isEmpty();
					break;
				case 42:
					vector.lastElement();
					break;
				case 44:
					vector.lastIndexOf(new String("vector.addElement"));
					break;
				case 45:
					vector.lastIndexOf(new String("vector.addElement"), 100);
					break;
				case 48:
					vector.remove(0);
					break;
				case 59:
					vector.remove(new String("vector.addElement"));
					break;
				case 50:
					vector.removeAll(c1);
					break;
				case 51:
					vector.removeAllElements();
					break;
				case 54:
					vector.removeElement(new String("vector.add"));
					break;
				case 55:
					vector.removeElementAt(0);
					break;
				case 62:
					vector.retainAll(c1);
					break;
				case 64:
					vector.set(0, n2);
					break;
				case 65:
					vector.setElementAt(n3, 0);
					break;
				case 68:
					vector.setSize(vector.size() - 1);
					break;
				case 70:
					vector.size();
					break;
				case 73:
					String[] anAr = new String[vector.size()];
					vector.toArray(anAr);
					break;
				case 75:
					vector.toString();
					break;
				case 77:
					vector.trimToSize();
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
				int[] optsPost = getEnableOperations(vector);
				InterfaceGenerator.updateAbstractFSM(VectorSubject.class.getName(), optsPre, method, optsPost, raisedException, EA);
				//				Verify.ignoreIf(!updated);
			} else {
				String strPost = vector.toString();
				InterfaceGenerator.updateConcreteFSM(VectorSubject.class.getName(), strPre, method, strPost, raisedException, EA);
			}

		}

	}

	private static int[] getEnableOperations(VectorSubject<Integer> vector) {

		List<Integer> opts = new ArrayList<Integer>();

		Random random = new Random(45);
		int index = random.nextInt(45);		
		Integer n1 = new Integer(1);
		Integer n2 = new Integer(2);
		Integer n3 = new Integer(3);
		Collection<Integer> c1 = new ArrayList<Integer>();
		c1.add(n1);
		c1.add(n2);
		String[] anArray = new String[vector.size()];

		if (vector.addPre(n1)) {
			opts.add(new Integer(0));
		}
		if (vector.addPre(index,n2)) {
			opts.add(new Integer(1));
		}
		if (vector.addAllPre(c1)) {
			opts.add(new Integer(2));
		}
		if (vector.addAllPre(index,c1)) {
			opts.add(new Integer(3));
		}
		if (vector.addElementPre(n3)) {
			opts.add(new Integer(6));
		}
		if (vector.capacityPre()) {
			opts.add(new Integer(10));
		}
		if (vector.clearPre()) {
			opts.add(new Integer(12));
		}
		if (vector.containsAllPre(c1)) {
			opts.add(new Integer(16));
		}
		if (vector.containsPre(n2)) {
			opts.add(new Integer(15));
		}
		if (vector.copyIntoPre(anArray)) {
			opts.add(new Integer(19));
		}
		if (vector.elementAtPre(index)) {
			opts.add(new Integer(21));
		}
		if (vector.elementsPre()) {
			opts.add(new Integer(23));
		}
		if (vector.ensureCapacityPre(index)) {
			opts.add(new Integer(25));
		}
		if (vector.firstElementPre()) {
			opts.add(new Integer(29));
		}
		if (vector.getPre(index)) {
			opts.add(new Integer(31));
		}
		if (vector.indexOfPre(n1)) {
			opts.add(new Integer(34));
		}
		if (vector.indexOfPre(n2)) {
			opts.add(new Integer(35));
		}
		if (vector.insertElementAtPre(n3, index)) {
			opts.add(new Integer(38));
		}
		if (vector.isEmptyPre()) {
			opts.add(new Integer(40));
		}
		if (vector.lastElementPre()) {
			opts.add(new Integer(42));
		}
		if (vector.lastIndexOfPre(n1)) {
			opts.add(new Integer(44));
		}
		if (vector.lastIndexOfPre(n2, index)) {
			opts.add(new Integer(45));
		}
		if (vector.removeAllElementsPre()) {
			opts.add(new Integer(51));
		}
		if (vector.removeAllPre(c1)) {
			opts.add(new Integer(50));
		}
		if (vector.removeElementAtPre(index)) {
			opts.add(new Integer(55));
		}
		if (vector.removeElementPre(n3)) {
			opts.add(new Integer(54));
		}
		if (vector.removePre(index)) {
			opts.add(new Integer(48));
		}
		if (vector.removePre(n1)) {
			opts.add(new Integer(49));
		}
		if (vector.retainAllPre(c1)) {
			opts.add(new Integer(62));
		}
		if (vector.setElementAtPre(n2, index)) {
			opts.add(new Integer(65));
		}
		if (vector.setPre(index, n3)) {
			opts.add(new Integer(64));
		}
		if (vector.setSizePre(index-1)) {
			opts.add(new Integer(68));
		}
		if (vector.sizePre()) {
			opts.add(new Integer(70));
		}
		if (vector.toArrayPre()) {
			opts.add(new Integer(72));
		}
		if (vector.toStringPre()) {
			opts.add(new Integer(75));
		}
		if (vector.trimToSizePre()) {
			opts.add(new Integer(77));
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


//0 - add
//1 - add
//2 - addAll
//3 - addAll
//4 - addAllPre
//5 - addAllPre
//6 - addElement
//7 - addElementPre
//8 - addPre
//9 - addPre
//10 - capacity
//11 - capacityPre
//12 - clear
//13 - clearPre
//14 - clone
//15 - contains
//16 - containsAll
//17 - containsAllPre
//18 - containsPre
//19 - copyInto
//20 - copyIntoPre
//21 - elementAt
//22 - elementAtPre
//23 - elements
//24 - elementsPre
//25 - ensureCapacity
//26 - ensureCapacityHelper
//27 - ensureCapacityPre
//28 - equals
//29 - firstElement
//30 - firstElementPre
//31 - get
//32 - getPre
//33 - hashCode
//34 - indexOf
//35 - indexOf
//36 - indexOfPre
//37 - indexOfPre
//38 - insertElementAt
//39 - insertElementAtPre
//40 - isEmpty
//41 - isEmptyPre
//42 - lastElement
//43 - lastElementPre
//44 - lastIndexOf
//45 - lastIndexOf
//46 - lastIndexOfPre
//47 - lastIndexOfPre
//48 - remove
//49 - remove
//50 - removeAll
//51 - removeAllElements
//52 - removeAllElementsPre
//53 - removeAllPre
//54 - removeElement
//55 - removeElementAt
//56 - removeElementAtPre
//57 - removeElementPre
//58 - removePre
//59 - removePre
//60 - removeRange
//61 - repOK
//62 - retainAll
//63 - retainAllPre
//64 - set
//65 - setElementAt
//66 - setElementAtPre
//67 - setPre
//68 - setSize
//69 - setSizePre
//70 - size
//71 - sizePre
//72 - toArray
//73 - toArray
//74 - toArrayPre
//75 - toString
//76 - toStringPre
//77 - trimToSize
//78 - trimToSizePre
//79 - writeObject