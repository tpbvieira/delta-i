package bst;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import delta.statemask.StateMask;
import delta.util.Common;
import deltalib.General;
import deltalib.dint.DeltaInt;
import deltalib.dint.DeltaIntArray;

public class DriverBST {

  static int lo = 0;
  static int hi = 9999999;
  static int SEED = 5029385;
  static int NUM_NODES = 70;
  static int NUM_INPUTS = 2000000;

  public static void main(String[] args) {
    General.C = false;
    General.Debug = false;

    // building initial state
    long l1 = System.currentTimeMillis();    
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

    // building standard BST object
    bst.original.BST bstStd = new bst.original.BST();
    for (int i = 1; i < NUM_NODES; i++) {
      bstStd.add(randomNumbers[i]);
    }

    // building long delta input
    int[] input = new int[NUM_INPUTS];
    for (int i = 1; i < NUM_INPUTS; i++) {
      input[i] = (r.nextInt(hi));
    }
    DeltaInt deltaInt = DeltaIntArray.create(input);

    // set the state mask and reset branch history
    Common.setNumStates(NUM_INPUTS);
    StateMask stateMask = new StateMask(StateMask.KINDS.SPARSE_REPRESENTATION, Common.NUM_STATES);
    StateMask.setStateMask(stateMask);
    stateMask.setAll();
    Common.bh.clearHistory(); // clear the branch history stack

    //Delta execution with instrumented tree
    System.out.println("start execution.\nsize of subjects: " + NUM_NODES + "\nC:" + General.C);
    int numExecutions = 0;
    long l2 = System.currentTimeMillis();
    do {
      if (numExecutions > 0) {        
        Common.bh.popAndSetHistory();
      }
      bstInstr.add(deltaInt);
      numExecutions++;
    } while(Common.bh.backtrack());    
    long l3 = System.currentTimeMillis();
    long deltaCost = l3 - l2;
    System.out.printf("DELTA:\n numExecutions: %d\n numDeltaOps: " +
        "%d\n timeExecution: %d (millis)\n timeTotal: %d (millis)", 
        numExecutions, General.countDeltaOperations, deltaCost, l3 - l1);

    NumberFormat num = DecimalFormat.getNumberInstance();
    System.out.println("DeltaCTime=" + General.deltaCTime);
    General.numOps = General.numOpsArrayArray + General.numOpsArrayConst + General.numOpsConstArray + General.numOpsConstConst;
    System.out.println("numOps=" + General.numOps);
    System.out.println("numOpsArrayArray=" + num.format((double)General.numOpsArrayArray/General.numOps));
    System.out.println("numOpsArrayConst=" + num.format((double)General.numOpsArrayConst/General.numOps));
    System.out.println("numOpsConstArray=" + num.format((double)General.numOpsConstArray/General.numOps));
    System.out.println("numOpsConstConst=" + num.format((double)General.numOpsConstConst/General.numOps));
    
    // standard tree
    l2 = System.currentTimeMillis();
    for (int i = 0; i < input.length; i++) {
      bstStd.add(input[i]);
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
        stdCost,
        nf.format((double)stdCost/input.length));

    System.out.printf("\nstats:\n STD/DELTA runtime: %sx \n avg. cost of Delta Op.: %s", 
        nf.format((double)stdCost/deltaCost), 
        nf.format((double)deltaCost/General.countDeltaOperations));
  }
}