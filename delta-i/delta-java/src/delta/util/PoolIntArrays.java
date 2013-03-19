package delta.util;

import java.util.*;

public class PoolIntArrays {
    
    private static List used = new LinkedList();    
    private static List pool = new LinkedList();
    private static int size = 0;
    private static boolean isClear = true;
    
    public static void releaseAll() {
        for (int i = 0 ; i < used.size() ; i++) {
            pool.add(used.remove(0));
        }
    }

    // remember the index for a given size!
    public static int[] acquire(int size) {
        if (!isClear && (size != PoolIntArrays.size)) {
            throw new RuntimeException("pool does not admit this size") ;
        }
        PoolIntArrays.size=size;
        isClear=false;
        int[] obj ;
        if (pool.size() != 0) {
            obj = (int[]) pool.remove(0);   
        } else {
            obj = new int[size];   
        }
        used.add(obj);
        return obj;
    }    
    
    public static void freeAll() {
        // unlikely that different iterations spawn
        // the same number of states
        used.clear();
        pool.clear();        
        isClear=true;
    }
    
}
