package treemap;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import delta.statemask.StateMask;
import delta.util.Common;
import deltalib.General;
import deltalib.dint.DeltaInt;
import deltalib.dint.DeltaIntArray;

public class DriverTreemap {
  
  static int NUM_NODES = 2000;  
  static int NUM_INPUTS = 60000000;

  static int SEED = 5029385;
  static int lo = 0;
  static int hi = 9999999;
  
  public static void main(String[] args) {
    General.C = true;
    long l1 = System.currentTimeMillis();
    
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
    
    long l2, l3;
    
    DeltaInt di = DeltaIntArray.create(input);
    // set the state mask reset branch history
    Common.setNumStates(NUM_INPUTS);
    StateMask sm = new StateMask(StateMask.KINDS.CONTIGUOUS_REPRESENTATION, Common.NUM_STATES);
    StateMask.setStateMask(sm);
    sm.setAll();
    Common.bh.clearHistory(); // clear the branch history stack
    
    
    
    System.out.println("start execution.  size of subjects: " + NUM_NODES);
    // main loop
    int numExecutions = 0;
    l2 = System.currentTimeMillis();
    do {
      if (numExecutions > 0) {        
        Common.bh.popAndSetHistory();
      }
      // call operation 
      treemap.put(di, null);
      numExecutions++;
    } while(Common.bh.backtrack());    
    l3 = System.currentTimeMillis();
    long deltaCost = l3 - l2;
    System.out.printf("DELTA:\n numExecutions: %d\n num Delta Ops: " +
    		"%d\n time execution: %d (millis)\n time total: %d (millis)", 
        numExecutions, General.countDeltaOperations, deltaCost, l3 - l1);
    
    
    
    l2 = System.currentTimeMillis();
    for (int i = 0; i < input.length; i++) {
      treemap2.put(input[i], null);
    }
    l3 = System.currentTimeMillis();
    long stdCost = l3 - l2;    
    NumberFormat nf = DecimalFormat.getNumberInstance();
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);    
    System.out.printf("\nSTANDARD:\n num. Executions: %d\n " +
    		"time execution: %d (millis)\n" +
    		"avg. cost of one execution: %s (millis)", 
        input.length, 
        stdCost
        , nf.format((double)stdCost/input.length)
        );    

    
    System.out.printf("\nstats:\n STD/DELTA runtime: %sx \n avg. cost of Delta Op.: %s", 
        nf.format((double)stdCost/deltaCost), 
        nf.format((double)deltaCost/General.countDeltaOperations));    
  }

}