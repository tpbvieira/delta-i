package deltalib.dboolean;

import delta.statemask.MaskPair;
import delta.statemask.MaskPredicate;
import delta.statemask.StateMask;
import delta.statemask.StateMaskIterator;
import delta.util.Common;

public abstract class DeltaBooleanCommon implements DeltaBoolean {

  /****** fields *******/
  protected boolean constant;
  protected boolean isStrictConstant;

  /************************************
   * Factory methods for DeltaBoolean
   ************************************/
  public static DeltaBoolean clone(DeltaBoolean arg) {
    DeltaBoolean result ;
    if (arg instanceof DeltaBooleanArray ) {
      result = DeltaBooleanArray.clone((DeltaBooleanArray)arg);
    } else if (arg instanceof DeltaBooleanConst) {
      result = DeltaBooleanConst.createConstant (arg.getStrictConstant()) ;
    } else {
      throw new RuntimeException("invalid option");
    }
    return result;
  }

  public static DeltaBooleanCommon create(int size) {
    DeltaBooleanCommon result ;
    switch (Common.getDeltaStateRepresentation()) {
    case DENSE_ARRAY:
//    int[] ar = Constants.isPooledAllocation() &&
//    Constants.isExecution() ?
//    PoolIntArrays.acquire(size):
//    new int[size];
      // we don't support pools for boolean[]'s
      boolean[] ar = new boolean[size];
      result = DeltaBooleanArray.create(ar);
      break;
    case SPARSE:
      result = DeltaBooleanSparsed.create();
      break ;
    default:
      throw new RuntimeException("invalid option");
    }
    return result ;
  }

  public static DeltaBooleanCommon createConstant(boolean value) {
    return new DeltaBooleanConst(value) ;
  }

  /************************************************************
   *  The following operation returns deterministically
   *  true/false if all the values in this object give the
   *  same answer to the query (relational operation).
   *  Otherwise, it creates a backtracking point.  The states
   *  which answer true are carried on to the true branch, the
   *  states that answer false are carried to the false branch.
   ************************************************************/
  public boolean eval() {
    StateMask mask = StateMask.getStateMask();
    ConstantInfoBoolean ciPair = getConstantInfo(mask);

    if (ciPair.isConstant()) { // no change to the mask
      boolean result = ciPair.getConstant();
      Common.bh.pushBranch(result);
      return result;
    } else { // non-deterministic choice
      MaskPredicate tmp = new MaskPredicate() {
        public boolean pred(int index) {
          return getValueAt(index);
        }
      };
      MaskPair maskPair = mask.genMasksByPredicate(tmp);
      int num = maskPair.getNumTrue();
      StateMask trueMask = maskPair.getTrueMask();
      StateMask falseMask = maskPair.getFalseMask();
      boolean result;
      if (num == 0 || (num == mask.numberOfEnabledStates())) {  
        throw new RuntimeException("it should not be deterministic...");
      } else {  // it is NOT deterministic, we should split!!
        Common.bh.pushBranchAndMask(false, falseMask);
        StateMask.setStateMask(trueMask);
        result = true;
      }
      return result;
    }
  }

  /*** support for constants ***/
  public ConstantInfoBoolean getConstantInfo(StateMask mask) {
    if (isStrictConstant()) {
      return new ConstantInfoBoolean(true, constant);
    }
    
    boolean isConstant = true;
    StateMaskIterator it = mask.intIterator();
    boolean expectedConstant = getValueAt(it.next());
    
    while (it.hasNext()) {
      int index = it.next();
      boolean tmp = getValueAt(index);
      if (expectedConstant != tmp) {
        isConstant = false;
        break;
      }
    }
    return new ConstantInfoBoolean(isConstant, expectedConstant);
  }
  

  private boolean initialized = true;
  public void setInitialized(boolean val) {
    initialized = val;
  }
  public boolean isInitialized() {
    return initialized;
  }


}