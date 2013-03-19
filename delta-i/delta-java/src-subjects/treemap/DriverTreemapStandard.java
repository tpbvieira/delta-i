package treemap;
import java.util.Random;

import deltalib.General;

public class DriverTreemapStandard {
  
  private static int NUM_NODES = 20;
  private static int SEED = 5029385;
  private static int NUM_INPUTS = 1000000;

  private static int hi = 9999999;
  
  public static void main(String[] args) {
    General.C = false;
    NUM_NODES = Integer.valueOf(args[0]).intValue();
    NUM_INPUTS = Integer.valueOf(args[1]).intValue();
    
    // building initial state
    Random r = new Random(SEED);
    int[] tmp = new int[NUM_NODES];
    for (int i = 1; i < NUM_NODES; i++) {
      tmp[i] = r.nextInt(hi);
    }
 
    // building original delta tree object 
    treemap.original.TreeMap treemap2 = new treemap.original.TreeMap();
    for (int i = 1; i < NUM_NODES; i++) {
      treemap2.put(tmp[i], null);
    }
    
    // building long delta input
    int[] input = new int[NUM_INPUTS];
    for (int i = 1; i < NUM_INPUTS; i++) {
      input[i] = (r.nextInt(hi));
    }
    
    long l2 = System.nanoTime();
    for (int i = 0; i < input.length; i++) {
      treemap2.put(input[i], null);
    }
    long l3 = System.nanoTime();
    
    System.out.println(NUM_NODES + "\t" + NUM_INPUTS + "\t" + (l3-l2));
  }

}