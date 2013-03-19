package delta.util.threads;

import delta.statemask.StateMask;

public interface MyRunnable extends Runnable {
  
  public MyRunnable clone();
  public void setStateMask(StateMask sm);

}
