package deltalib.dint;

import java.util.Arrays;

import delta.statemask.StateMask;


public class DeltaIntConst extends DeltaIntCommon {

  DeltaIntConst(int constant) {
    this.constant = constant;
    this.isStrictConstant = true;
  }

  public static DeltaIntArray cloneExpanded(DeltaIntConst arg) {
    int constant = arg.getStrictConstant();
    StateMask sm = StateMask.getStateMask();
    int[] values = new int[sm.size()];
    Arrays.fill(values, constant);
    return DeltaIntArray.create(values);
  }

  public DeltaIntConst copy() {
    return new DeltaIntConst(this.getStrictConstant());
  }

  /*** non-public abstract methods ***/
  DeltaInt arith_(BinaryArithmeticCommand op, int arg, StateMask mask) { 
    throw new RuntimeException("invalid method"); 
  }
  public int getValueAt(int index) { return constant; }
  public void setValueAt(int index, int val) { throw new RuntimeException("invalid method"); }
  public Integer getIntegerValueAt(int index) { throw new RuntimeException("invalid method"); }

  public ConstantInfoInt getConstantInfo(StateMask mask) {
    return new ConstantInfoInt(true, constant);
  }

  public int getStrictConstant() {
    return constant;
  }

  public boolean isStrictConstant() {
    return true;
  }

  public String toString() {
    return constant + "";
  }

}
