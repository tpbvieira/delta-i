package deltalib.dint;

import delta.statemask.StateMask;


public interface DeltaInt {
   
  public static final DeltaIntCommon ZERO = DeltaIntCommon.createConstant(0); 
  public static final DeltaIntCommon ONE = DeltaIntCommon.createConstant(1); 
  public static final DeltaIntCommon MAX_VALUE = DeltaIntCommon.createConstant(Integer.MAX_VALUE);

  // hack for use DeltaInt as DeltaBooleans
  public static final DeltaIntCommon TRUE = DeltaIntCommon.createConstant(888);
  public static final DeltaIntCommon FALSE = DeltaIntCommon.createConstant(777);    

  /*** arithmetic operators ***/
  public DeltaInt add(int arg);
  public DeltaInt sub(int arg);
  public DeltaInt mult(int arg);
  public DeltaInt div(int arg);

  public DeltaInt add(DeltaInt arg);
  public DeltaInt sub(DeltaInt arg);
  public DeltaInt mult(DeltaInt arg);
  public DeltaInt div(DeltaInt arg);

  /*** relational operators ***/
  public boolean gt(int arg);
  public boolean geq(int arg);
  public boolean lt(int arg);
  public boolean leq(int arg);
  public boolean eq(int arg);
  public boolean neq(int arg);

  public boolean gt(DeltaInt arg);
  public boolean geq(DeltaInt arg);
  public boolean lt(DeltaInt arg);
  public boolean leq(DeltaInt arg);
  public boolean eq(DeltaInt arg);
  public boolean neq(DeltaInt arg);

  /*** accessors ***/
  public ConstantInfoInt getConstantInfo(StateMask mask) ;
  public int getValueAt(int index);

  /*** optimization ***/
  public boolean isStrictConstant() ;
  public int getStrictConstant() ;
  public void undoConstant();

  /*** mutators (use with care) ***/
  public DeltaInt update(DeltaInt newv);
  public void setValueAt(int index, int val);
  public void setValue(int val);
  
  /*** asynchronous creation ***/
  public void setInitialized(boolean val);
  public boolean isInitialized();

}
