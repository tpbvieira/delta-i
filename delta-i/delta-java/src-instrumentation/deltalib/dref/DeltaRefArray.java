package deltalib.dref;

/**
 * generic dense implementation of a delta state of references
 * 
 * @author damorim
 *
 * @param <T>
 */
public abstract class DeltaRefArray<T> extends DeltaRefCommon<T> {

  protected T[] values;

  public DeltaRefArray(T[] values) {
    super(null, false);
    this.values = values;
  }


  /************ abstract method implementation ************/
  public T getValueAt(int index) {
    return values[index];
  }

  public void setValueAt(int index, T val) {
    values[index] = val;
  }

  /************ abstract method implementation ************/
  public T getStrictConstant() {
    return constant;
  }

  /************ abstract method implementation ************/
  public boolean isStrictConstant() {
    return isStrictConstant;
  }

}