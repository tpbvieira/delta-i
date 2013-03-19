package deltalib.dboolean;

import delta.statemask.StateMask;


public interface DeltaBoolean {
  
  public static DeltaBoolean TRUE = DeltaBooleanCommon.createConstant(true); 
  public static DeltaBoolean FALSE = DeltaBooleanCommon.createConstant(false); 

  /*** arithmetic operators ***/
  public boolean eval();

  /*** accessors ***/
  public ConstantInfoBoolean getConstantInfo(StateMask mask) ;
  public abstract boolean getValueAt(int index);
  public abstract void setValueAt(int index, boolean val);

  /*** due to optimization ***/
  public boolean isStrictConstant() ;
  public boolean getStrictConstant() ;

  public void setInitialized(boolean val);
  public boolean isInitialized();

  public boolean equals(Object obj);
}
