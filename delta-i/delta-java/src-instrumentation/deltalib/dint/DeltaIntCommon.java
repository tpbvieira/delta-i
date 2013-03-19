package deltalib.dint;


import delta.statemask.MaskPair;
import delta.statemask.MaskPredicate;
import delta.statemask.StateMask;
import delta.statemask.StateMaskIterator;
import delta.util.Common;
import delta.util.PoolIntArrays;


public abstract class DeltaIntCommon implements DeltaInt {

  /****** fields *******/
  protected int constant;
  protected boolean isStrictConstant;

  /********************************************
   * Factory methods for DeltaInt
   ********************************************/

  public static DeltaInt clone(DeltaInt arg) {
    DeltaInt result ;
    if (arg instanceof DeltaIntArray ) {
      result = DeltaIntArray.clone((DeltaIntArray)arg);
    } else if (arg instanceof DeltaIntConst) {
      result = DeltaIntConst.createConstant (arg.getStrictConstant()) ;
    } else {
      throw new RuntimeException("invalid option");
    }
    return result;
  }

  public static DeltaIntCommon create(int size) {
    DeltaIntCommon result ;
    switch (Common.getDeltaStateRepresentation()) {
    case DENSE_ARRAY:
      int[] ar = (Common.isPooledAllocation() && Common.isExecution() ?
          PoolIntArrays.acquire(size):
            new int[size]);
      result = DeltaIntArray.create(ar);
      break;
    case SPARSE:
      result = DeltaIntSparsed.create();
      break ;
    default:
      throw new RuntimeException("invalid option");
    }
    return result ;
  }

  public static boolean BST = false;
  public static DeltaIntCommon createDynamic(int totalSize, int numberOfActiveStates) {      
    if (!BST || numberOfActiveStates >= 500) {
      int[] ar = (Common.isPooledAllocation() && Common.isExecution() ?
          PoolIntArrays.acquire(totalSize):
            new int[totalSize]);
      return DeltaIntArray.create(ar);
    } else {
      return DeltaIntSparsed.create();
    }
  }

  public static DeltaIntCommon createConstant(int value) {
    return new DeltaIntConst(value) ;
  }

  /****** arithmetic op constants ********/
  public static interface BinaryArithmeticCommand {
    public int doit(int arg1, int arg2) ;
  }
  private static final BinaryArithmeticCommand ADD =
    new BinaryArithmeticCommand() {
    public int doit(int arg1, int arg2) { return arg1 + arg2; }
  };
  private static final BinaryArithmeticCommand SUB =
    new BinaryArithmeticCommand() {
    public int doit(int arg1, int arg2) { return arg1 - arg2; }
  };
  private static final BinaryArithmeticCommand MULT =
    new BinaryArithmeticCommand() {
    public int doit(int arg1, int arg2) { return arg1 * arg2; }
  };
  private static final BinaryArithmeticCommand DIV =
    new BinaryArithmeticCommand() {
    public int doit(int arg1, int arg2) { return arg1 / arg2; }
  };

  /****** relational op constants *******/
  public static interface BinaryRelationalCommand {
    public boolean doit(int arg1, int arg2) ;
  }
  private static final BinaryRelationalCommand GT =
    new BinaryRelationalCommand() {
    public boolean doit(int arg1, int arg2) { return arg1 > arg2; }
  };
  private static final BinaryRelationalCommand GEQ =
    new BinaryRelationalCommand() {
    public boolean doit(int arg1, int arg2) { return arg1 >= arg2; }
  };
  private static final BinaryRelationalCommand LT =
    new BinaryRelationalCommand() {
    public boolean doit(int arg1, int arg2) { return arg1 < arg2; }
  };
  private static final BinaryRelationalCommand LEQ =
    new BinaryRelationalCommand() {
    public boolean doit(int arg1, int arg2) { return arg1 <= arg2; }
  };
  private static final BinaryRelationalCommand EQ =
    new BinaryRelationalCommand() {
    public boolean doit(int arg1, int arg2) { return arg1 == arg2; }
  };
  private static final BinaryRelationalCommand NEQ =
    new BinaryRelationalCommand() {
    public boolean doit(int arg1, int arg2) { return arg1 != arg2; }
  };

  /******* arithmetic operations with primitive arg ******/
  public DeltaInt add(int arg) {
    return arith(ADD, arg);
  }
  public DeltaInt sub(int arg) {
    return arith(SUB, arg);
  }
  public DeltaInt mult(int arg) {
    return arith(MULT, arg);
  }
  public DeltaInt div(int arg) {
    return arith(DIV, arg);
  }
  /********************************************************/
  private DeltaInt arith(BinaryArithmeticCommand op, int arg) {
    StateMask mask = StateMask.getStateMask();
    DeltaInt result;
    ConstantInfoInt pair = getConstantInfo(mask);
    if (pair.isConstant()) {
      result = DeltaIntCommon.createConstant(op.doit(pair.getConstant(), arg));
    } else {
      result = arith_(op, arg, mask);
    }
    return result;
  }
  /********************************************************/
  // This method works for both dense and sparse masks. A
  // dense implementation could be written for performance
  DeltaInt arith_(BinaryArithmeticCommand op, int arg2, StateMask mask) {
    DeltaInt result = DeltaIntCommon.create(mask.size()) ;
    StateMaskIterator it = mask.intIterator();
    while (it.hasNext()) {
      int stateId = it.next() ;
      int arg1 = getValueAt(stateId);
      result.setValueAt(stateId, op.doit(arg1, arg2)) ;
    }
    return result ;
  }

  /******* arithmetic operations with deltaint arg *******/
  public DeltaIntCommon add(DeltaInt arg) {
    return arith2(ADD, arg);
  }
  public DeltaIntCommon sub(DeltaInt arg) {
    return arith2(SUB, arg);
  }
  public DeltaIntCommon mult(DeltaInt arg) {
    return arith2(MULT, arg);
  }
  public DeltaIntCommon div(DeltaInt arg) {
    return arith2(DIV, arg);
  }
  /********************************************************/
  private DeltaIntCommon arith2(BinaryArithmeticCommand op, DeltaInt arg) {
    if (this.isStrictConstant() && arg.isStrictConstant()) {
      int result = op.doit(this.constant, arg.getStrictConstant());
      return DeltaIntConst.createConstant(result);
    } else {
      return arith2_(op, arg);
    }
  }
  /********************************************************/
  private DeltaIntCommon arith2_(final BinaryArithmeticCommand op, final DeltaInt arg) {
    boolean isStrictConstantLHS = this.isStrictConstant();
    boolean isStrictConstantRHS = arg.isStrictConstant();
    StateMask mask = StateMask.getStateMask();
    DeltaIntCommon result = DeltaIntCommon.create(mask.size());
    StateMaskIterator it = mask.intIterator();
    while (it.hasNext()) {
      int stateId = it.next();
      int lhs, rhs;

      if (isStrictConstantLHS) {
        lhs = getStrictConstant();                   
      } else {
        lhs = getValueAt(stateId);
      }

      if (isStrictConstantRHS) {
        rhs = arg.getStrictConstant();                   
      } else {
        rhs = arg.getValueAt(stateId);
      }
      result.setValueAt(stateId, op.doit(lhs, rhs));
    }
    return result;
  }

  /****** relational operations  ******/
  public boolean gt(int arg) {
    return rel(GT, arg);
  }
  public boolean geq(int arg) {
    return rel(GEQ, arg);
  }
  public boolean lt(int arg) {
    return rel(LT, arg);
  }
  public boolean leq(int arg) {
    return rel(LEQ, arg);
  }
  public boolean eq(int arg) {
    return rel(EQ, arg);
  }
  public boolean neq(int arg) {
    return rel(NEQ, arg);
  }

  /************************************************************
   *  The following operations only return deterministically
   *  true/false if all the values in this object give the
   *  same answer to the query (relational operation).
   *  Otherwise, it creates a backtracking point.  The states
   *  which answer true are carried on to the true branch, the
   *  states that answer false are carried to the false branch.
   ************************************************************/
  private boolean rel(final BinaryRelationalCommand op, final int arg) {

    if (Common.bh.isBranchHistory()) {
      return Common.bh.getBranchChoice();
    }

    StateMask mask = StateMask.getStateMask();
    boolean result;
    ConstantInfoInt ctePair = getConstantInfo(mask) ;
    if (ctePair.isConstant()) { // no change to the mask
      result = op.doit(ctePair.getConstant(), arg);
      Common.bh.pushBranch(result);

    } else {
      MaskPredicate tmp = new MaskPredicate() {
        public boolean pred(int index) {
          return op.doit(getValueAt(index), arg);
        }
      };
      MaskPair maskPair = mask.genMasksByPredicate(tmp);
      int num = maskPair.getNumTrue();
      StateMask trueMask = maskPair.getTrueMask();
      StateMask falseMask = maskPair.getFalseMask();

      if (num == 0) { // it is deterministic...
        // there are no states satisfying this, i.e.
        // ALL states satisfy the falseMask.
        result = false;
        Common.bh.pushBranch(false);

      } else if (num == mask.numberOfEnabledStates()) {
        result = true;
        Common.bh.pushBranch(true);

      } else { // it is NOT deterministic, we should split!!
        Common.bh.pushBranchAndMask(false, falseMask);
        StateMask.setStateMask(trueMask);
        result = true;
      }
    }
    return result;
  }

  /****** relational2 operations  ******/
  public boolean gt(DeltaInt arg) {
    return rel2(GT, arg);
  }
  public boolean geq(DeltaInt arg) {
    return rel2(GEQ, arg);
  }
  public boolean lt(DeltaInt arg) {
    return rel2(LT, arg);
  }
  public boolean leq(DeltaInt arg) {
    return rel2(LEQ, arg);
  }
  public boolean eq(DeltaInt arg) {
    return rel2(EQ, arg);
  }
  public boolean neq(DeltaInt arg) {
    return rel2(NEQ, arg);
  }

  /********************************************************/
  private boolean rel2(BinaryRelationalCommand op, DeltaInt arg) {
    if (Common.bh.isBranchHistory()) {
      return Common.bh.getBranchChoice();
    }
    boolean result;
    if (this.isStrictConstant() && arg.isStrictConstant()) {
      result = op.doit(this.constant, arg.getStrictConstant());
      Common.bh.pushBranch(result);
    } else {
      result = rel2_(op, arg);
    }
    return result;
  }

  /********************************************************/
  private boolean rel2_(final BinaryRelationalCommand op, final DeltaInt arg) {
    boolean isStrictConstantLHS = this.isStrictConstant();
    boolean isStrictConstantRHS = arg.isStrictConstant();
    StateMask mask = StateMask.getStateMask();
    MaskPredicate tmp;
    // LHS is a constant
    if (isStrictConstantLHS) {
      final int LHS = this.constant;
      tmp = new MaskPredicate() {
        public boolean pred(int index) {
          int RHS = arg.getValueAt(index);
          return op.doit(LHS, RHS);
        }
      };

      // RHS is a constant
    } else if (isStrictConstantRHS) {
      final int RHS = arg.getStrictConstant();
      tmp = new MaskPredicate() {
        public boolean pred(int index) {
          int LHS = DeltaIntCommon.this.getValueAt(index);
          return op.doit(LHS, RHS);
        }
      };

      // neither side is a constant
    } else {
      tmp = new MaskPredicate() {
        public boolean pred(int index) {
          int LHS = DeltaIntCommon.this.getValueAt(index);
          int RHS = arg.getValueAt(index);
          return op.doit(LHS, RHS);
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

    } else if (num == mask.numberOfEnabledStates()) {  // it is deterministic...
      Common.bh.pushBranch(true);
      result = true;

    } else {  // it is NOT deterministic, we should split!!
      Common.bh.pushBranchAndMask(false, falseMask);
      StateMask.setStateMask(trueMask);
      result = true;
    }
    return result;
  }

  /****** dealing with constants ******/
  public boolean isConstant() {
    throw new UnsupportedOperationException("deprecated method!") ;
  }

  public boolean isNonStrictConstant() {
    throw new UnsupportedOperationException("deprecated method!") ;
  }

  public int getConstant() {
    throw new UnsupportedOperationException("deprecated method!") ;
  }

  private StateMask lastStateMask;
  private ConstantInfoInt cache;
  public ConstantInfoInt getConstantInfo(StateMask mask) {
    if (isStrictConstant()) {
      return new ConstantInfoInt(true, constant);
    }
    if (cache != null && mask == lastStateMask) {
      // for comparison when there are no splits
      return cache;
    }
    boolean isConstant = true;
    StateMaskIterator it = mask.intIterator();
    int expectedConstant = getValueAt(it.next());
    while (it.hasNext()) {
      int index = it.next();
      int tmp = getValueAt(index);
      if (expectedConstant != tmp) {
        isConstant = false;
        break;
      }
    }
    lastStateMask = mask;
    cache = new ConstantInfoInt(isConstant, expectedConstant);        
    return cache;
  }

  /********************************************************/
  public void undoConstant() {
    isStrictConstant = false;
  }

  /*** methods ***/
  public abstract int getValueAt(int index);
  public abstract void setValueAt(int index, int val);
  
  @Override
  public DeltaInt update(DeltaInt dNew) {
    StateMask sm = StateMask.getStateMask();
    if (sm.numberOfEnabledStates() == sm.size()) {
      return dNew;
    }
    DeltaInt dOld = this;
    DeltaInt result = dOld;    
    if (dOld instanceof DeltaIntConst) {
      result = DeltaIntConst.cloneExpanded((DeltaIntConst)dOld);
    } else if (dOld.isStrictConstant()) {
      dOld.undoConstant();
      System.err.println("check this out!");
    }
    StateMaskIterator smit = sm.intIterator();
    boolean dNewIsCte = dNew instanceof DeltaIntConst;
    while (smit.hasNext()) {
      int stateid = smit.next();
      int val = dNewIsCte ? dNew.getStrictConstant() : dNew.getValueAt(stateid);
      result.setValueAt(stateid, val);
    }
    return result;
  }
  
  private boolean isInitialized = true;
  @Override
  public void setInitialized(boolean val) {
    isInitialized = val;
  }
  @Override
  public boolean isInitialized() {
    return isInitialized = true;
  }
  
}

