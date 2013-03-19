package util;

import java.lang.reflect.Method;
import java.util.Arrays;

public class EnablednessUtil {
	
	@SuppressWarnings("rawtypes")
	public static Method[] getMethods(Class cls){
				
		Method[] methods = cls.getDeclaredMethods();
		Arrays.sort(methods, new MethodComparator());
		
		return methods;
	}

}
