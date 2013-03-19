package deltalib.dboolean;

import java.util.Arrays;

import delta.statemask.StateMask;
import delta.util.DynamicIntArray;


public class DeltaBooleanSparsed extends DeltaBooleanCommon {

  private int[] states;
  private boolean[] values;
  DynamicIntArray dynIntArrayStates ; // for initialization...
  DynamicIntArray dynIntArrayValues ; // for initialization...

  private DeltaBooleanSparsed() {}

  public static DeltaBooleanSparsed create() {
    DeltaBooleanSparsed result = new DeltaBooleanSparsed();
    result.dynIntArrayStates = new DynamicIntArray() ;
    result.dynIntArrayValues = new DynamicIntArray() ;
    return result;
  }

  public static DeltaBoolean createForMerger(int[] states, boolean[] values) {
    DeltaBooleanSparsed result = new DeltaBooleanSparsed();
    result.states = states;
    result.values = values;
    return result;
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
    if (states == null) {
      states = new int[dynIntArrayStates.size()];
      values = new boolean[dynIntArrayValues.size()];
      dynIntArrayStates.toArray(states);
//    dynIntArrayValues.toArray(values);
      for (int i = 0 ; i < dynIntArrayValues.size(); i++) {
        values[i] = dynIntArrayValues.get(i) == 1;
      }
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
      throw new RuntimeException("this index is unreachable "+j) ;
    }

    return values[j];
  }

  // go in lock step, only used in initialization phase
  public void setValueAt(int index, boolean val) {
    dynIntArrayStates.set(dynIntArrayStates.size(),index)  ;
    dynIntArrayValues.set(dynIntArrayValues.size(),val ? 1 : 0)  ;
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
    boolean result = true;
    DeltaBooleanSparsed tmp = (DeltaBooleanSparsed)obj;
    
    result = result && (this.dynIntArrayStates.equals(tmp.dynIntArrayStates));
    result = result && (this.dynIntArrayValues.equals(tmp.dynIntArrayValues));

    for (int i = 0; i < states.length; i++) {
      result = result && (this.states == tmp.states);
    }
    
    for (int i = 0; i < values.length; i++) {
      result = result && (this.values == tmp.values);
    }
    
    return result;
  }
  
}
