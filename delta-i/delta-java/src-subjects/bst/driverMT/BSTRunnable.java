package bst.driverMT;

import bst.instrumented.BST;
import delta.statemask.StateMask;
import delta.util.threads.MyRunnable;
import delta.util.threads.Util;
import deltalib.dint.DeltaInt;

public class BSTRunnable implements MyRunnable {
  
  /*test input*/
  DeltaInt input;
  BST bst;
  /* state mask */
  StateMask sm;
  /* shutdown hook */
  ShutdownHook hook;
  
  /** constructor **/
  public BSTRunnable(DeltaInt input, BST bst, StateMask sm, ShutdownHook hook) {
    super();
    this.input = input;
    this.bst = bst;
    this.sm = sm;
    this.hook = hook;
  }
  
  @Override
  public void run() {
    Thread t = Thread.currentThread();
    StateMask.setStateMask(sm);
    bst.add(input);
    StateMask sm2 = StateMask.getStateMask();
//    System.out.println(sm2);
    int num = sm2.numberOfEnabledStates();
    synchronized (hook) {
      hook.numStates -= num;
      if (hook.numStates == 0) {
        Util.shutdown();
      } else if (hook.numStates < 0) {
        throw new RuntimeException("watch out error: redundant states explored!");
      }
    }
    StateMask.clearThreadMap(t);
  }

  @Override
  public void setStateMask(StateMask sm) {
    this.sm = sm;
  }
  
  public MyRunnable clone() {
    return new BSTRunnable(input, bst, null, hook);
  }

}
