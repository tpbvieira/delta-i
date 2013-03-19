package deltalib.dboolean;

/**********************************************
 *
 * helper class wrapping two primitive resulted
 * from calling the checkConstant method
 *
 *********************************************/
public class ConstantInfoBoolean {

  private boolean isConstant;
  private boolean constant;

  ConstantInfoBoolean(boolean isConstant, boolean constant) {
    this.isConstant = isConstant;
    this.constant = constant;
  }

  public boolean isConstant() { return isConstant; }
  public boolean getConstant() { return constant; }

}