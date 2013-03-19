package deltalib.dboolean;

import java.util.Arrays;

import delta.statemask.StateMask;



public class DeltaBooleanArray extends DeltaBooleanCommon {

  public boolean[] values;

  public static DeltaBoolean clone(DeltaBooleanArray arg) {
    int len = arg.values.length;
    boolean[] v = new boolean[len];        
    System.arraycopy(arg.values,0,v,0,len);
    return create(v);
  }

  public static DeltaBooleanArray createConstant (boolean constant) {
    DeltaBooleanArray result = new DeltaBooleanArray() ;
    result.constant = constant;
    result.isStrictConstant = true;
    return result ;
  }    

  public static DeltaBooleanArray create(boolean[] values) {
    DeltaBooleanArray result = new DeltaBooleanArray() ;
    result.values = values ;
    result.isStrictConstant = false ;
    return result ;
  }    

  /*********  this is for supporting arrays ******/
  public boolean[] cloneArray() {
    int size = StateMask.getStateMask().size();
    boolean[] result = new boolean[size];
    if (values != null) {
      System.arraycopy(values, 0, result, 0, size);
    } else {
      Arrays.fill(result, constant);
    }
    return result;
  }

  /************ abstract method implementation ************/
  public boolean getValueAt(int index) {
    return values[index];
  }

  public void setValueAt(int index, boolean val) {
    values[index] = val ;
  }

  /************ abstract method implementation ************/
  public boolean getStrictConstant() {
    return constant;
  }

  /************ abstract method implementation ************/
  public boolean isStrictConstant() {
    return isStrictConstant;
  }

  public boolean equals(Object obj){
    DeltaBooleanArray tmp = (DeltaBooleanArray)obj;
    boolean result = true;

    boolean[] objValue = tmp.cloneArray(); 
    boolean[] thisValue = this.cloneArray();

    for (int i = 0; i < thisValue.length; i++) {
      if(!(objValue[i] == thisValue[i])){
        result = false;
        break;
      }
    }

    return result;

  }

}