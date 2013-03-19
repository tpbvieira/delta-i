package bst;
import java.util.Random;

import delta.statemask.StateMask;
import delta.util.Common;
import deltalib.General;
import deltalib.dint.DeltaInt;
import deltalib.dint.DeltaIntArray;

public class DriverBSTNative {

  private static int hi = 9999999;
  private static int SEED = 5029385;

  private static int NUM_NODES = 100;
  private static int NUM_INPUTS = 0;

  public static void main(String[] args) {
    General.C = true;
    NUM_NODES = Integer.valueOf(args[0]).intValue();
    NUM_INPUTS = Integer.valueOf(args[1]).intValue();

    // building initial state
    Random r = new Random(SEED);
    int[] randomNumbers = new int[NUM_NODES];
    for (int i = 1; i < NUM_NODES; i++) {
      randomNumbers[i] = r.nextInt(hi);
    }

    // building delta tree object with constant values
    bst.instrumented.BST bstInstr = new bst.instrumented.BST();
    for (int i = 1; i < NUM_NODES; i++) {
      bstInstr.add(General.constant(randomNumbers[i]));
    }

    // building long delta input
    int[] input = new int[NUM_INPUTS];
    for (int i = 1; i < NUM_INPUTS; i++) {
      input[i] = (r.nextInt(hi));
    }
    DeltaInt deltaInt = DeltaIntArray.create(input);

    // set the state mask and reset branch history
    Common.setNumStates(NUM_INPUTS);
    StateMask stateMask = new StateMask(StateMask.KINDS.CONTIGUOUS_REPRESENTATION, Common.NUM_STATES);
    StateMask.setStateMask(stateMask);
    stateMask.setAll();
    Common.bh.clearHistory(); // clear the branch history stack

    //Delta execution with instrumented tree
    int numExecutions = 0;
    long l2 = System.nanoTime();
    do {
      if (numExecutions > 0) {        
        Common.bh.popAndSetHistory();
      }
      // call operation 
      bstInstr.add(deltaInt);
      numExecutions++;
    } while(Common.bh.backtrack());    
    long l3 = System.nanoTime();

    System.out.println(NUM_NODES + "\t" + NUM_INPUTS + "\t" + (l3-l2));
  }
}