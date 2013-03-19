package deltalib.dint;

import java.util.Arrays;

import delta.statemask.StateMask;
import delta.util.DynamicIntArray;


public class DeltaIntSparsed extends DeltaIntCommon {

  private int[] states;
  private int[] values;
  DynamicIntArray dynIntArrayStates ; // for initialization...
  DynamicIntArray dynIntArrayValues ; // for initialization...

  private DeltaIntSparsed() {}

  public static DeltaIntSparsed create() {
    DeltaIntSparsed result = new DeltaIntSparsed();
    result.dynIntArrayStates = new DynamicIntArray() ;
    result.dynIntArrayValues = new DynamicIntArray() ;
    return result;
  }

  public DeltaIntConst copy() {
    throw new UnsupportedOperationException();
  }

  public static DeltaInt createForMerger(int[] states, int[] values) {
    DeltaIntSparsed result = new DeltaIntSparsed();
    result.states = states;
    result.values = values;
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
    if (states == null) {
      states = new int[dynIntArrayStates.size()];
      values = new int[dynIntArrayValues.size()];
      dynIntArrayStates.toArray(states);
      dynIntArrayValues.toArray(values);
      dynIntArrayStates = dynIntArrayValues = null;
    } 
    // find index (this should be small)
    int j = -1 ;
    for (int i = 0; i < states.length ; i++) {
      if (states[i] == index) {
        j = i; break;
      }
    }
    if (j == -1) {
      throw new RuntimeException("this index is unreachable") ;
    }

    return values[j];
  }

  // go in lock step, only used in initialization phase
  public void setValueAt(int index, int val) {
    dynIntArrayStates.set(dynIntArrayStates.size(),index)  ;
    dynIntArrayValues.set(dynIntArrayValues.size(),val)  ;
  }

  /************ abstract method implementation ************/
  public int getStrictConstant() {
    return constant;
  }

  /************ abstract method implementation ************/
  public boolean isStrictConstant() {
    return isStrictConstant;
  }    

}
