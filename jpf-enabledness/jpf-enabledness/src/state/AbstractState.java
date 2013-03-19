package state;

import java.lang.reflect.Method;
import java.util.Arrays;

import util.EnablednessUtil;

public class AbstractState extends State {

	private int[] methods;

	@SuppressWarnings("rawtypes")
	public AbstractState(Class clss, int[] enabledMethods, boolean isFirst) {
		super(clss, isFirst);
		Arrays.sort(enabledMethods);
		methods = enabledMethods;	
	}

	public int[] getMethods(){
		return methods;
	}

	@Override
	public int hashCode() {
		return (cls.getName() + Arrays.toString(methods)).hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		Method[] mtds = EnablednessUtil.getMethods(cls);
		sb.append("[");
		for (int i = 0; i < methods.length; i++) {			
			sb.append(mtds[methods[i]].getName());
			if(i + 1 < methods.length)
				sb.append(",");
		}
		sb.append("]");

		return sb.toString();
	}

	@Override
	public boolean equals(Object obj){

		boolean result = false;

		if(obj instanceof AbstractState){
			AbstractState state = (AbstractState)obj;
			result = (this.hashCode() == state.hashCode());
		}

		return result;
	}
}