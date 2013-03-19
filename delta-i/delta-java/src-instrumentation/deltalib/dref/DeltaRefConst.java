package deltalib.dref;

import delta.statemask.StateMask;

public abstract class DeltaRefConst<T> extends DeltaRefCommon<T> {

  public DeltaRefConst(T object) {
    super(object, true);
  }

  @Override
  public ConstantInfoRef<T> getConstantInfo(StateMask mask) {
    return new ConstantInfoRef<T>(true, constant) ;
  }

  @Override
  public T getStrictConstant() {
    return constant ;
  }

  @Override
  public boolean isStrictConstant() {
    return true ;
  }

  @Override
  public T getValueAt(int index) { 
    throw new UnsupportedOperationException("invalid method");
  }

  @Override
  public void setValueAt(int index, T val) { 
    throw new UnsupportedOperationException("invalid method"); 
  }

}