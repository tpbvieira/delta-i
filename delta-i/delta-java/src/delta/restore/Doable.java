package delta.restore;

import delta.statemask.StateMask;

public interface Doable<T> {

  public void doit(StateMask sm);
  
}
