package bst.driverMT;

import java.util.Random;

import bst.instrumented.BST;
import delta.statemask.StateMask;
import delta.util.Common;
import delta.util.Config;
import deltalib.General;
import deltalib.dint.DeltaInt;
import deltalib.dint.DeltaIntArray;

public class DriverBSTForMT {
  
  static int lo = 0;
  static int hi = 9999999;
  static int SEED = 5029385;

  static int NUM_NODES = 70;
  static int NUM_INPUTS = 2000000;
  
  public static void main(String[] args) {
    General.C = false;
    Config.PARALLEL_DE = true;

    // building initial state  
    Random r = new Random(SEED);
    int[] randomNumbers = new int[NUM_NODES];
    for (int i = 1; i < NUM_NODES; i++) {
      randomNumbers[i] = r.nextInt(hi);
    }

    // building delta tree object with constant values
    BST bstInstr = new BST();
    for (int i = 1; i < NUM_NODES; i++) {
      bstInstr.add(General.constant(randomNumbers[i]));
    }
    BST.modify = false;
    
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
//    System.out.println("start execution.\nsize of subjects: " + NUM_NODES + "\nC:" + General.C);
//    int numExecutions = 0;

//    System.out.println(deltaInt);
    ShutdownHook shutdownHook = new ShutdownHook(System.currentTimeMillis(), NUM_INPUTS);
//    final long l1 = System.currentTimeMillis();
    BSTRunnable runnable = 
      new BSTRunnable(deltaInt, 
                      bstInstr, 
                      StateMask.getStateMask(), 
                      shutdownHook);
    Config.myRun = runnable;
    runnable.run();

    Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));

  }

}
