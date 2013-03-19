package deltalib.dref;

import delta.statemask.MaskPair;
import delta.statemask.MaskPredicate;
import delta.statemask.StateMask;
import delta.statemask.StateMaskIterator;
import delta.util.Common;

public abstract class DeltaRefCommon<T> implements DeltaRef<T> {

  protected T constant;
  protected boolean isStrictConstant;

  public DeltaRefCommon(T ref, boolean isCte) {
    this.constant = ref;
    this.isStrictConstant = isCte;
  }

  public void undoConstant() {
    isStrictConstant = false;
  }

  private StateMask lastStateMask;
  private ConstantInfoRef<T> cache;
  public ConstantInfoRef<T> getConstantInfo(StateMask mask) {
    if (isStrictConstant()) {
      return new ConstantInfoRef<T>(true, constant);
    }
    if (cache != null && mask == lastStateMask) {
      // for comparison when there are no splits
      return cache;
    }
    boolean isConstant = true;
    StateMaskIterator it = mask.intIterator();
    T expectedConstant = getValueAt(it.next());
    while (it.hasNext()) {
      T tmp = getValueAt(it.next());
      if (expectedConstant != tmp) {
        isConstant = false;
        break;
      }
    }
    if (isConstant) {
      Common.bh.becameConstant(this);
      constant = expectedConstant;
      isStrictConstant = true;
    }
    lastStateMask = mask;
    cache = new ConstantInfoRef<T>(isConstant, expectedConstant);        
    return cache;
  }

  /********************************************************/

  @SuppressWarnings("unchecked")
  static final DeltaRef NULL = new DeltaRefConst(null) {
    
  };

  @SuppressWarnings("unchecked")
  public boolean isNull() {
    return eq(NULL);
  }

  @SuppressWarnings("unchecked")
  public boolean isNotNull() {
    return neq(NULL);
  }

  @SuppressWarnings("unchecked")
  public boolean neq(DeltaRef<T> arg) {
    return !eq(arg);
  }

  /*******************************************************
   * This operation compares the equality (i.e., ==) of the
   * references in a pair-wise fashion.  In other words,
   * this.eq(arg) is true if references at corresponding
   * indices are ==.
   *******************************************************/
  @SuppressWarnings("unchecked")
  public boolean eq(DeltaRef<T> arg) {
    if (Common.bh.isBranchHistory()) {
      return Common.bh.getBranchChoice();
    }
    boolean result;
    if (this.isStrictConstant() && arg.isStrictConstant()) {
      result = (constant == arg.getStrictConstant());
      Common.bh.pushBranch(result);
    } else {
      result = _eq(arg);
    }
    return result;
  }

  /********************************************************/
  private boolean _eq(final DeltaRef<T> arg) {
    boolean isStrictConstantLHS = this.isStrictConstant();
    boolean isStrictConstantRHS = arg.isStrictConstant();
    StateMask mask = StateMask.getStateMask();
    MaskPredicate tmp;
    // optimization to make this object a strict constant
    // when possible
    boolean willMakeConstant = false ;
    // LHS is a constant
    if (isStrictConstantLHS) {
      final T LHS = this.getStrictConstant();
      tmp = new MaskPredicate() {
        public boolean pred(int index) {
          T RHS = arg.getValueAt(index);
          return (LHS == RHS);
        }
      };
      // RHS is a constant
    } else if (isStrictConstantRHS) {
      willMakeConstant = true;
      final T RHS = arg.getStrictConstant();
      tmp = new MaskPredicate() {
        public boolean pred(int index) {
          T LHS = getValueAt(index);
          return (LHS == RHS);
        }
      };
      // neither side is a constant
    } else {
      tmp = new MaskPredicate() {
        public boolean pred(int index) {
          T LHS = getValueAt(index);
          T RHS = arg.getValueAt(index);
          return (LHS == RHS);
        }
      };
    }
    MaskPair maskPair = mask.genMasksByPredicate(tmp);
    int num = maskPair.getNumTrue();
    StateMask trueMask = maskPair.getTrueMask();
    StateMask falseMask = maskPair.getFalseMask();
    boolean result;
    if (num == 0) {  // it is deterministic...
      Common.bh.pushBranch(false);
      result = false;
    } else if (num == mask.numberOfEnabledStates()) {
      Common.bh.pushBranch(true);
      result = true;
    } else {  // it is NOT deterministic, we should split!!
      Common.bh.pushBranchAndMask(false, falseMask);
      StateMask.setStateMask(trueMask);
      result = true;
      if (willMakeConstant) {
        // Remember more computation
        constant = arg.getStrictConstant();
        isStrictConstant = true;
      }
    }
    return result;
  }

  /**
   * admits only one value
   * @return
   */
  public boolean isSingleton() {
    return false;
  }

}