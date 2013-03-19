package deltalib.dint;

import java.lang.reflect.Array;
import java.util.Arrays;

import delta.statemask.StateMask;


public class DeltaIntArray extends DeltaIntCommon {

  public int[] values;
  public boolean isExpanded = false;  // see hack in IntArray

  public static DeltaInt clone(DeltaInt in) {
    DeltaIntArray arg = (DeltaIntArray) in;
    int len = arg.values.length;
    int[] v = new int[len];        
    System.arraycopy(arg.values,0,v,0,len);
    return create(v);
  }

  public DeltaInt copy() {
    int len = this.values.length;
    int[] v = new int[len];        
    System.arraycopy(this.values, 0, v, 0, len);
    return create(v);
  }

  public static DeltaIntArray createConstant (int constant) {
    DeltaIntArray result = new DeltaIntArray() ;
    result.constant = constant;
    result.isStrictConstant = true;
    return result ;
  }    

  public static DeltaIntArray create(int[] values) {
    DeltaIntArray result = new DeltaIntArray() ;
    result.values = values ;
    result.isStrictConstant = false ;
    return result ;
  }    

  /** 
   * Create DeltaIntArray with determined size and initial value.
   * @param size
   * @param value
   * @return
   */
  public static DeltaIntArray create(int size, int value) {
    DeltaIntArray result = new DeltaIntArray();
    result.values = new int[size];
    result.isStrictConstant = false;
    Arrays.fill(result.values, value);
    
    return result;
  } 
  
  /** 
   * Create DeltaIntArray with determined size and default value.
   * @param size
   * @param value
   * @return
   */
  public static DeltaIntArray createDefault(int size) {
    DeltaIntArray result = new DeltaIntArray();
    result.values = new int[size];
    result.isStrictConstant = false;
    
    Array.setInt(result.values, 0, 0);
    
    return result;
  }
  
  /*********  this is for supporting arrays ******/
  public int[] cloneArray() {
    int size = StateMask.getStateMask().size();
    int[] result = new int[size];
    if (values != null) {
      System.arraycopy(values, 0, result, 0, size);
    } else {
      Arrays.fill(result, constant);
    }
    return result;
  }

  /************ abstract method implementation ************/
  public int getValueAt(int index) {
    return values[index];
  }

  public void setValueAt(int index, int val) {
    values[index] = val ;
  }

  public void setValue(int val) {
    Arrays.fill(values, val);
  }
  
  /************ abstract method implementation ************/
  public int getStrictConstant() {
    return constant;
  }

  /************ abstract method implementation ************/
  public boolean isStrictConstant() {
    return isStrictConstant;
  }

  public String toString() {
    return java.util.Arrays.toString(values);
  }

}
