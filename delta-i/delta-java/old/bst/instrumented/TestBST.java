package bst.instrumented;

import java.util.Random;

import delta.statemask.StateMask;
import delta.util.Common;
import delta.util.Driver;
import deltalib.dint.DeltaIntCommon;

public class TestBST {

  public static void main(String[] args) {
    if (true) {
      
      Common.setNumStates(1 /*11 to run 1 original + 10 mutants*/);
      
      StateMask sm = new StateMask(StateMask.KINDS.CONTIGUOUS_REPRESENTATION, Common.NUM_STATES);
      StateMask.setStateMask(sm); 

      BinarySearchTree bst1 = new BinarySearchTree();
      bst1.add(DeltaIntCommon.createConstant(0));
      bst1.add(DeltaIntCommon.createConstant(-1));
      bst1.add(DeltaIntCommon.createConstant(2));

      bst1.add(DeltaIntCommon.createConstant(3));
      bst1.add(DeltaIntCommon.createConstant(2));
      bst1.add(DeltaIntCommon.createConstant(1));
      
    } else {      
      merge(Integer.parseInt(args[0]));
    }
  }
  
  
  private static void merge(final int times) {
    /***
     * simulate a step in a delta execution.  for now,
     * we can consider that instrumentation wraps 
     * method calls in test sequences within 
     * "driver.step(...)" (see below).  delta execution 
     * should explore all states in it.
     ***/
    Driver driver = new Driver();
    driver.startup();
    final BinarySearchTree bst1 = new BinarySearchTree();
    // one step
    driver.step(
        new Driver.Runner(){
          @Override
          public void run() {
            Random r = new Random(90281);
            for (int i = 0; i < times; i++) {
              bst1.add(DeltaIntCommon.createConstant(r.nextInt()));
            }
          }
        }
    );
  }

}