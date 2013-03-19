package deltalib.dref;

import delta.statemask.StateMask;

public interface DeltaRef<T> {
  
  /*** accessor and mutator ***/

  public abstract T getValueAt(int index);

  public abstract void setValueAt(int index, T val);

  /**************** dealing with constants ****************/

  public abstract boolean isStrictConstant();

  public abstract T getStrictConstant();

  public abstract ConstantInfoRef<T> getConstantInfo(StateMask sm);

}
