package treemap;
import java.util.Random;

import delta.statemask.StateMask;
import delta.util.Common;
import deltalib.General;
import deltalib.dint.DeltaInt;
import deltalib.dint.DeltaIntArray;

public class DriverTreemapDelta {

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

    // building instrumented delta tree object with constant values
    treemap.instrumented.TreeMap treemap = new treemap.instrumented.TreeMap();
    for (int i = 1; i < NUM_NODES; i++) {
      treemap.put(General.constant(tmp[i]), null);
    }

    // building long delta input
    int[] input = new int[NUM_INPUTS];
    for (int i = 1; i < NUM_INPUTS; i++) {
      input[i] = (r.nextInt(hi));
    }

    DeltaInt di = DeltaIntArray.create(input);
    Common.setNumStates(NUM_INPUTS);
    StateMask sm = new StateMask(StateMask.KINDS.CONTIGUOUS_REPRESENTATION, Common.NUM_STATES);
    StateMask.setStateMask(sm);
    sm.setAll();
    Common.bh.clearHistory(); // clear the branch history stack

    // main loop
    int numExecutions = 0;
    long l2 = System.nanoTime();
    do {
      if (numExecutions > 0) {        
        Common.bh.popAndSetHistory();
      }
      // call operation 
      treemap.put(di, null);
      numExecutions++;
    } while(Common.bh.backtrack());    
    long l3 = System.nanoTime();
    System.out.println(NUM_NODES + "\t" + NUM_INPUTS + "\t" + (l3-l2));

  }
  
}