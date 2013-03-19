package stackSample;
import gov.nasa.jpf.jvm.MJIEnv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JPF_stackSample_InterfaceGenerator {

	static Set<State> states = new HashSet<State>();
	static Map<Map<State,State>,Set<Edge>> edges = new HashMap<Map<State,State>,Set<Edge>>();
	
	public static boolean updateAbstractFSM(MJIEnv env, int objref, int intArrRefPre, int method, int intArrRefPost, boolean raisedException) {
		return updateFSM(env, objref, intArrRefPre, method, intArrRefPost,raisedException);
	}

	public static boolean updateConcreteFSM(MJIEnv env, int objref, int intArrRefPre, int method, int intArrRefPost, boolean raisedException) {
		return updateFSM(env, objref, intArrRefPre, method, intArrRefPost, raisedException);
	}

	public static boolean updateFSM(MJIEnv env, int objref, int intArrRefPre, int executedEdge, int intArrRefPost, boolean isException) {
		
		boolean updated = false;
		
		// pre state
		Object[] preObj = addStateIfNeeded(env, intArrRefPre);
		State preState = (State) preObj[0];
		boolean preAdded = ((Boolean) preObj[1]).booleanValue();

		// post state
		Object[] postObj = addStateIfNeeded(env, intArrRefPost);
		State postState = (State) postObj[0];
		boolean postAdded = ((Boolean) postObj[1]).booleanValue();
		
		// if needed, create map to store transitions from pre-state 
		Map<State,State> stts = new HashMap<State,State>();
		stts.put(preState, postState);
		Method[] clsMethods = Stack.class.getDeclaredMethods();
		Set<Edge> tmpEdges = new HashSet<Edge>();
		if(edges.containsKey(stts)){
			tmpEdges = edges.get(stts);		
		}		
		tmpEdges.add(new Edge(isException, clsMethods[executedEdge].getName()));
		edges.put(stts, tmpEdges);
		updated = true;
		
		return preAdded || postAdded || updated;
	}

	private static Object[] addStateIfNeeded(MJIEnv env, int intArrRef) {
		State state = createState(env, intArrRef);
		boolean isNew = states.add(state);
		return new Object[]{state, isNew};		
	}

	public static void dumpFSM() {
//		printStdOut();
		generateGrath();
	}

	private static void printStdOut() {
		StringBuffer sb = new StringBuffer();
		Set<Entry<Map<State,State>,Set<Edge>>> edge = edges.entrySet();
		
		for (Entry<Map<State, State>, Set<Edge>> entry : edge) {
			Map<State,State> stts = entry.getKey();
			Set<Edge> tmpEdges = entry.getValue();
			sb.append(stts.entrySet().iterator().next().getKey());
			sb.append(":");
			sb.append("[");
			int i = 1;
			for (Edge method: tmpEdges) {				
				sb.append(method.getName());
				if(method.isException())
					sb.append("*");
				if((i++) < tmpEdges.size())
					sb.append(",");
			}
			sb.append("]");
			sb.append(":");
			sb.append(stts.entrySet().iterator().next().getValue());
			sb.append("\n");
		}
		System.out.println(sb);
	}

	@SuppressWarnings("rawtypes")
	private static void generateGrath(){

		StringBuffer sb = new StringBuffer();		
		Map<State,Integer> tmp = new HashMap<State,Integer>();
		Class cls = Stack.class;
		int index = 1;

		// Declare states
		sb.append("\ndigraph enabledness {\n");
		for (State state : states) {
			sb.append(index);
			sb.append(" [label=\"");
			sb.append(state);
			sb.append("\"]\n");
			tmp.put(state, index);
			index++;
			cls = state.getStateClass();
		}
		sb.append("\n");
		
		// Declare Edges
		Set<Entry<Map<State,State>,Set<Edge>>> edge = edges.entrySet();
		for (Entry<Map<State, State>, Set<Edge>> entry : edge) {
			Map<State,State> stts = entry.getKey();
			Set<Edge> mtds = entry.getValue();
			
			sb.append(tmp.get(stts.entrySet().iterator().next().getKey()));
			sb.append("->");
			sb.append(tmp.get(stts.entrySet().iterator().next().getValue()));
			sb.append(" [label=\"<");
			int i = 1;
			for (Edge method : mtds) {
				sb.append(method.getName());
				if(method.isException())
					sb.append("*");
				if((i++) < mtds.size())
					sb.append(" | ");
			}
			sb.append(">\"]\n");
		}
		sb.append("}");

		//saving .dot file and generating .png graph
		String fileName = cls.getName() + ".dot";
		try{
			File myFile = new File(fileName);
			myFile.delete();

			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true));
			writer.write(sb.toString());
			writer.flush();
			writer.close();
			Runtime.getRuntime().exec("dot -Tpng -o"+ cls.getName() + ".png " + fileName);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	/**
	 * auxiliary data-structures
	 */
	
	static State createState(MJIEnv env, int intArrRef) {
		State result;
		
		if (StackDriver.EA) {
			int[] opts = env.getIntArrayObject(intArrRef);
			result = new AbstractState(Stack.class, opts);
		} else {
			String str = env.getStringObject(intArrRef);
			result = new ConcreteState(Stack.class, str);
		}
		
		return result;
	}

}