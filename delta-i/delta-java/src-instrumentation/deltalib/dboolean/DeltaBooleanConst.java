package deltalib.dboolean;

import delta.statemask.StateMask;

public class DeltaBooleanConst extends DeltaBooleanCommon {

  DeltaBooleanConst(boolean constant) {
    this.constant = constant;
    this.isStrictConstant = true;
  }

  public ConstantInfoBoolean getConstantInfo(StateMask mask) {
    return new ConstantInfoBoolean(true, constant);
  }

  public boolean getStrictConstant() {
    return constant;
  }

  public boolean isStrictConstant() {
    return true;
  }

  /*** non-public abstract methods ***/
  public boolean getValueAt(int index) { return constant; }
  public void setValueAt(int index, boolean val) { throw new RuntimeException("invalid method"); }
  public Integer getIntegerValueAt(int index) { throw new RuntimeException("invalid method"); }

  public String toString() {
    return constant+"";
  }

  
  public boolean equals(Object obj){
    DeltaBooleanConst tmp = (DeltaBooleanConst)obj;
    boolean result = true;
    
    result = result && (tmp.getStrictConstant() == this.getStrictConstant());
    result = result && (tmp.isStrictConstant() == this.isStrictConstant());
    
    return result;
  }
}