package deltalib.dref;

public class ConstantInfoRef<T>{

  private boolean isConstant;
  private T constant;

  public ConstantInfoRef(boolean isConstant, T constant) {
    this.isConstant = isConstant;
    this.constant = constant;
  }

  public boolean isConstant() {
    return isConstant;
  }

  public T getConstant() {
    return constant;
  }
}