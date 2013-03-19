package deltalib.dint;

/**********************************************
 *
 * helper class wrapping two primitive resulted
 * from calling the checkConstant method
 *
 *********************************************/
public class ConstantInfoInt {
  private boolean isConstant;
  private int constant;
  ConstantInfoInt(boolean isConstant, int constant) {
    this.isConstant = isConstant;
    this.constant = constant;
  }
  public boolean isConstant() { return isConstant; }
  public int getConstant() { return constant; }
}