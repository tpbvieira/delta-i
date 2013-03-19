package deltalib;

import nativedelta.NativeDelta;
import delta.statemask.MaskPair;
import delta.statemask.MaskPredicate;
import delta.statemask.StateMask;
import delta.statemask.StateMaskIterator;
import delta.util.Common;
import delta.util.Config;
import delta.util.threads.MyRunnable;
import deltalib.dboolean.DeltaBoolean;
import deltalib.dboolean.DeltaBooleanArray;
import deltalib.dboolean.DeltaBooleanCommon;
import deltalib.dint.DeltaInt;
import deltalib.dint.DeltaIntArray;
import deltalib.dint.DeltaIntCommon;
import deltalib.dint.DeltaIntConst;

public class General {

  public static int countDeltaOperations;
  public static boolean C = true;
  public static boolean Debug = false;
  public static long deltaCTime = 0;

  public static int numOps = 0;
  public static int numOpsConstConst = 0;
  public static int numOpsArrayConst = 0;
  public static int numOpsConstArray = 0;
  public static int numOpsArrayArray = 0;

  /*************************
   * constants
   *************************/

  public static final DeltaInt MINUS_ONE = DeltaIntCommon.createConstant(-1);
  public static final DeltaInt ZERO = DeltaIntCommon.ZERO;
  public static final DeltaInt ONE = DeltaIntCommon.ONE;
  public static final DeltaInt TWO = DeltaIntCommon.createConstant(2);

  public static final DeltaBoolean FALSE = DeltaBooleanCommon.FALSE;
  public static final DeltaBoolean TRUE = DeltaBooleanCommon.TRUE;

  public static DeltaInt constant(int i) {
    return DeltaIntCommon.createConstant(i);
  }

  /*************************
   * branch
   *************************/

  public static boolean split(final DeltaBoolean arg) {    
    if (!Config.PARALLEL_DE && Common.bh.isBranchHistory()) {
      return Common.bh.getBranchChoice();
    }

    if (arg.isStrictConstant()) {
      return arg.getStrictConstant();
    }

    //TODO: this can be simplified 
    StateMask mask = StateMask.getStateMask();
    MaskPredicate maskPredicate = new MaskPredicate() {
      public boolean pred(int index) {
        return arg.getValueAt(index) /*check if its true*/;
      }
    };


    int num;
    StateMask trueMask;
    StateMask falseMask;
    synchronized (ONE) {
      MaskPair maskPair = mask.genMasksByPredicate(maskPredicate);
      num = maskPair.getNumTrue();
      trueMask = maskPair.getTrueMask();
      falseMask = maskPair.getFalseMask();
    }

    boolean result;

    if (num == 0) {
      // it is deterministic...
      if (!Config.PARALLEL_DE) Common.bh.pushBranch(false);
      result = false;
    } else if (num == mask.numberOfEnabledStates()) {
      // it is deterministic...
      if (!Config.PARALLEL_DE) Common.bh.pushBranch(true);
      result = true;
    } else {  // it is NOT deterministic, we should split!!

      if (Config.PARALLEL_DE) {
        MyRunnable myRun = Config.myRun.clone();
        myRun.setStateMask(falseMask);
        delta.util.threads.Util.spawnWorker(myRun);
      } else {
        Common.bh.pushBranchAndMask(false, falseMask);
      }
      StateMask.setStateMask(trueMask);
      result = true;
    }

    return result;
  }

  /*************************
   * relational operations
   *************************/

  public static enum ROP {GT, GEQ, LT, LEQ, EQ, NEQ}

  public static DeltaBoolean gt(final DeltaInt arg1, final DeltaInt arg2) {

    if(arg1 instanceof DeltaIntArray && arg2 instanceof DeltaIntArray)
      return gt((DeltaIntArray)arg1,(DeltaIntArray)arg2);
    else 
      if (arg1 instanceof DeltaIntConst && arg2 instanceof DeltaIntConst)
        return gt((DeltaIntConst)arg1,(DeltaIntConst)arg2);
      else
        if(arg1 instanceof DeltaIntArray && arg2 instanceof DeltaIntConst)
          return gt((DeltaIntArray)arg1,(DeltaIntConst)arg2);
        else
          return gt((DeltaIntConst)arg1,(DeltaIntArray)arg2);
  }

  public static DeltaBoolean gt(final DeltaIntArray arg1, final DeltaIntArray arg2) {

    if(General.C){     
      if (!Config.PARALLEL_DE) countDeltaOperations++;
      final StateMask mask = StateMask.getStateMask();
      final DeltaBooleanArray result = (DeltaBooleanArray)DeltaBooleanCommon.create(mask.size());

      if(mask.repr.getKind() == StateMask.KINDS.CONTIGUOUS_REPRESENTATION){
        NativeDelta.GTIntArrayContigousMask(arg1.values, arg2.values, result.values);
      }else{
        NativeDelta.GTIntArray(arg1.values, arg2.values, result.values, mask.repr.toArray(),
            mask.repr.getCurrentIndex(),mask.repr.numberOfEnabledStates(),mask.repr.getKind().ordinal());
      }
      if(Debug){
        final DeltaBoolean result2 = rop(arg1, arg2, ROP.GT);
        if(!result.equals(result2))System.out.println("###1isEquals="+ result.equals(result2)+
            " Mask=" + mask.repr.getKind() + " EnabledStates=" + mask.repr.numberOfEnabledStates() + " Index=" + mask.repr.getCurrentIndex());
      }
      return result;
    }else{
      return rop(arg1, arg2, ROP.GT);
    }

  }

  public static DeltaBoolean gt(final DeltaIntArray arg1, final DeltaIntConst arg2) {
    numOpsArrayConst++;

    if(General.C){
      if (!Config.PARALLEL_DE) countDeltaOperations++;
      final StateMask mask = StateMask.getStateMask();
      final DeltaBooleanArray result = (DeltaBooleanArray)DeltaBooleanCommon.create(mask.size());

      if(mask.repr.getKind() == StateMask.KINDS.CONTIGUOUS_REPRESENTATION){
        NativeDelta.GTIntArrayConstContigousMask(arg1.values, arg2.getStrictConstant(), result.values);
      }else{
        NativeDelta.GTIntArrayConst(arg1.values, arg2.getStrictConstant(), result.values, mask.repr.toArray(),
            mask.repr.getCurrentIndex(),mask.repr.numberOfEnabledStates(),mask.repr.getKind().ordinal());
      }
      if(Debug){
        final DeltaBoolean result2 = rop(arg1, arg2, ROP.GT);
        if(!result.equals(result2))System.out.println("###2isEquals="+ result.equals(result2)+
            " Mask=" + mask.repr.getKind() + " EnabledStates=" + mask.repr.numberOfEnabledStates() + " Index=" + mask.repr.getCurrentIndex());
      }
      return result;
    }else{
      return rop(arg1, arg2, ROP.GT);
    }

  }

  public static DeltaBoolean gt(final DeltaIntConst arg1, final DeltaIntArray arg2) {
    numOpsConstArray++;

    if(General.C){
      if (!Config.PARALLEL_DE) countDeltaOperations++;
      final StateMask mask = StateMask.getStateMask();
      final DeltaBooleanArray result = (DeltaBooleanArray)DeltaBooleanCommon.create(mask.size());

      if(mask.repr.getKind() == StateMask.KINDS.CONTIGUOUS_REPRESENTATION){
        NativeDelta.GTIntConstArrayContigousMask(arg1.getStrictConstant(), arg2.values, result.values);
      }else{
        NativeDelta.GTIntConstArray(arg1.getStrictConstant(), arg2.values, result.values, mask.repr.toArray(),
            mask.repr.getCurrentIndex(),mask.repr.numberOfEnabledStates(),mask.repr.getKind().ordinal());
      }

      if(Debug){
        final DeltaBoolean result2 = rop(arg1, arg2, ROP.GT);
        if(!result.equals(result2)){
          System.out.println("###3isEquals=" + result.equals(result2) + " Mask=" + mask.repr.getKind() + 
              " EnabledStates=" + mask.repr.numberOfEnabledStates() + " Index=" + mask.repr.getCurrentIndex());
        }
      }
      return result;
    }else{
      return rop(arg1, arg2, ROP.GT);
    }

  }

  public static DeltaBoolean gt(final DeltaIntConst arg1, final DeltaIntConst arg2) {
    numOpsConstConst++;
    DeltaBoolean result = null;

    if(General.C){
      boolean tmp = arg1.getStrictConstant() > arg2.getStrictConstant();
      result = DeltaBooleanCommon.createConstant(tmp);
    }else{
      result = rop(arg1, arg2, ROP.GT);
    }

    return result;
  }

  public static DeltaBoolean geq(final DeltaInt arg1, final DeltaInt arg2) {
    return rop(arg1, arg2, ROP.GEQ);
  }

  public static DeltaBoolean lt(final DeltaInt arg1, final DeltaInt arg2) {

    if(arg1 instanceof DeltaIntArray && arg2 instanceof DeltaIntArray)
      return lt((DeltaIntArray)arg1,(DeltaIntArray)arg2);
    else 
      if (arg1 instanceof DeltaIntConst && arg2 instanceof DeltaIntConst)
        return lt((DeltaIntConst)arg1,(DeltaIntConst)arg2);
      else
        if(arg1 instanceof DeltaIntArray && arg2 instanceof DeltaIntConst)
          return lt((DeltaIntArray)arg1,(DeltaIntConst)arg2);
        else
          return lt((DeltaIntConst)arg1,(DeltaIntArray)arg2);
  }

  public static DeltaBoolean lt(final DeltaIntArray arg1, final DeltaIntArray arg2) {
    numOpsArrayArray++;

    if(General.C){     
      if (!Config.PARALLEL_DE) countDeltaOperations++;
      final StateMask mask = StateMask.getStateMask();
      final DeltaBooleanArray result = (DeltaBooleanArray)DeltaBooleanCommon.create(mask.size());

      if(mask.repr.getKind() == StateMask.KINDS.CONTIGUOUS_REPRESENTATION){
        NativeDelta.LTIntArrayContigousMask(arg1.values, arg2.values, result.values);
      }else{
        NativeDelta.LTIntArray(arg1.values, arg2.values, result.values, mask.repr.toArray(),
            mask.repr.getCurrentIndex(),mask.repr.numberOfEnabledStates(),mask.repr.getKind().ordinal());
      }
      if(Debug){
        final DeltaBoolean result2 = rop(arg1, arg2, ROP.LT);
        if(!result.equals(result2))System.out.println("###4isEquals="+ result.equals(result2)+
            " Mask=" + mask.repr.getKind() + " EnabledStates=" + mask.repr.numberOfEnabledStates() + " Index=" + mask.repr.getCurrentIndex());
      }
      return result;
    }else{
      return rop(arg1, arg2, ROP.LT);
    }

  }

  public static DeltaBoolean lt(final DeltaIntArray arg1, final DeltaIntConst arg2) {
    numOpsArrayConst++;

    if(General.C){
      if (!Config.PARALLEL_DE) countDeltaOperations++;
      final StateMask mask = StateMask.getStateMask();
      final DeltaBooleanArray result = (DeltaBooleanArray)DeltaBooleanCommon.create(mask.size());

      if(mask.repr.getKind() == StateMask.KINDS.CONTIGUOUS_REPRESENTATION){
        NativeDelta.LTIntArrayConstContigousMask(arg1.values, arg2.getStrictConstant(), result.values);
      }else{
        NativeDelta.LTIntArrayConst(arg1.values, arg2.getStrictConstant(), result.values, mask.repr.toArray(),
            mask.repr.getCurrentIndex(),mask.repr.numberOfEnabledStates(),mask.repr.getKind().ordinal());
      }
      if(Debug){
        final DeltaBoolean result2 = rop(arg1, arg2, ROP.LT);
        if(!result.equals(result2))System.out.println("###5isEquals="+ result.equals(result2)+
            " Mask=" + mask.repr.getKind() + " EnabledStates=" + mask.repr.numberOfEnabledStates() + " Index=" + mask.repr.getCurrentIndex());
      }
      return result;
    }else{
      return rop(arg1, arg2, ROP.LT);
    }

  }

  public static DeltaBoolean lt(final DeltaIntConst arg1, final DeltaIntArray arg2) {
    numOpsConstArray++;

    if(General.C){
      if (!Config.PARALLEL_DE) countDeltaOperations++;
      final StateMask mask = StateMask.getStateMask();
      final DeltaBooleanArray result = (DeltaBooleanArray)DeltaBooleanCommon.create(mask.size());

      if(mask.repr.getKind() == StateMask.KINDS.CONTIGUOUS_REPRESENTATION){
        NativeDelta.LTIntConstArrayContigousMask(arg1.getStrictConstant(), arg2.values, result.values);
      }else{
        NativeDelta.LTIntConstArray(arg1.getStrictConstant(), arg2.values, result.values, mask.repr.toArray(),
            mask.repr.getCurrentIndex(),mask.repr.numberOfEnabledStates(),mask.repr.getKind().ordinal());
      }
      if(Debug){
        final DeltaBoolean result2 = rop(arg1, arg2, ROP.LT);
        if(!result.equals(result2))System.out.println("###6isEquals="+ result.equals(result2)+
            " Mask=" + mask.repr.getKind() + " EnabledStates=" + mask.repr.numberOfEnabledStates() + " Index=" + mask.repr.getCurrentIndex());
      }
      return result;
    }else{
      return rop(arg1, arg2, ROP.LT);
    }

  }

  public static DeltaBoolean lt(final DeltaIntConst arg1, final DeltaIntConst arg2) {
    numOpsConstConst++;
    DeltaBoolean result = null;

    if(General.C){
      boolean tmp = arg1.getStrictConstant() < arg2.getStrictConstant();
      result = DeltaBooleanCommon.createConstant(tmp);
    }else{
      result = rop(arg1, arg2, ROP.LT);
    }

    return result;
  }

  public static DeltaBoolean leq(final DeltaInt arg1, final DeltaInt arg2) {
    return rop(arg1, arg2, ROP.LEQ);
  }

  public static DeltaBoolean eq_int(final DeltaInt arg1, final DeltaInt arg2) {
    return rop(arg1, arg2, ROP.EQ);
  }

  public static DeltaBoolean neq_int(final DeltaInt arg1, final DeltaInt arg2) {
    return rop(arg1, arg2, ROP.NEQ);
  }

  //TODO: provide version that splits immediately.
  private static DeltaBoolean rop(final DeltaInt arg1, final DeltaInt arg2, final ROP op) {

    final boolean isStrictConstantOne = arg1.isStrictConstant();
    final boolean isStrictConstantTwo = arg2.isStrictConstant();
    final DeltaBoolean result;

    if (isStrictConstantOne && isStrictConstantTwo) {      
      boolean tmp = rop(arg1.getStrictConstant(), arg2.getStrictConstant(), op);
      result = DeltaBooleanCommon.createConstant(tmp);
    } else {
      if (!Config.PARALLEL_DE) countDeltaOperations++;
      final StateMask mask = StateMask.getStateMask();
      result = DeltaBooleanCommon.create(mask.size());
      javaRopRun(arg1, arg2, op, isStrictConstantOne, isStrictConstantTwo, result, mask);
    }

    return result;
  }


  private static boolean rop(int arg1, int arg2, ROP op) {
    boolean result;
    switch (op) {
    case GT:
      result = arg1 > arg2;
      break;
    case GEQ:
      result = arg1 >= arg2;
      break;
    case LT:
      result = arg1 < arg2;
      break;
    case LEQ:
      result = arg1 <= arg2;
      break;
    case EQ:
      result = arg1 == arg2;
      break;
    case NEQ:
      result = arg1 != arg2;
      break;
    default:
      throw new RuntimeException();
    }
    return result;
  }


  /*************************
   * arithmetic operations
   *************************/

  public static enum AOP {ADD, SUB, MULT, DIV}

  public static DeltaInt add(final DeltaInt arg1, final DeltaInt arg2) {

    if(arg1 instanceof DeltaIntArray && arg2 instanceof DeltaIntArray){
      return add((DeltaIntArray)arg1,(DeltaIntArray)arg2);
    }
    else 
      if (arg1 instanceof DeltaIntConst && arg2 instanceof DeltaIntConst){
        return add((DeltaIntConst)arg1,(DeltaIntConst)arg2);
      }
      else
        if(arg1 instanceof DeltaIntArray && arg2 instanceof DeltaIntConst){
          return add((DeltaIntArray)arg1,(DeltaIntConst)arg2);
        }
        else
          return add((DeltaIntConst)arg1,(DeltaIntArray)arg2);
  }


  public static DeltaInt add(final DeltaIntArray arg1, final DeltaIntArray arg2) {
    numOpsArrayArray++;

    if(General.C){  
      if (!Config.PARALLEL_DE) countDeltaOperations++;      
      final StateMask mask = StateMask.getStateMask();
      final DeltaIntArray result = (DeltaIntArray)DeltaIntCommon.create(mask.size());

      if(mask.repr.getKind() == StateMask.KINDS.CONTIGUOUS_REPRESENTATION){
        NativeDelta.ADDIntArrayContigousMask(arg1.values, arg2.values, result.values);
      }else{
        NativeDelta.ADDIntArray(arg1.values, arg2.values, result.values, mask.repr.toArray(),
            mask.repr.getCurrentIndex(),mask.repr.numberOfEnabledStates(),mask.repr.getKind().ordinal());
      }
      if(Debug){
        final DeltaInt result2 = arith(arg1, arg2, AOP.ADD);
        if(!result.equals(result2))System.out.println("###7isEquals="+ result.equals(result2)+
            " Mask=" + mask.repr.getKind() + " EnabledStates=" + mask.repr.numberOfEnabledStates() + " Index=" + mask.repr.getCurrentIndex());
      }
      return result;
    }else{
      return arith(arg1, arg2, AOP.ADD);
    }    
  }


  public static DeltaInt add(final DeltaIntConst arg1, final DeltaIntArray arg2) {

    if(General.C){      
      if (!Config.PARALLEL_DE) countDeltaOperations++;
      final StateMask mask = StateMask.getStateMask();
      final DeltaIntArray result = (DeltaIntArray)DeltaIntCommon.create(mask.size());

      if(mask.repr.getKind() == StateMask.KINDS.CONTIGUOUS_REPRESENTATION){
        NativeDelta.ADDIntConstArrayContigousMask(arg1.getStrictConstant(), arg2.values, result.values);
      }else{
        NativeDelta.ADDIntConstArray(arg1.getStrictConstant(), arg2.values, result.values, mask.repr.toArray(),
            mask.repr.getCurrentIndex(),mask.repr.numberOfEnabledStates(),mask.repr.getKind().ordinal());
      }
      if(Debug){
        final DeltaInt result2 = arith(arg1, arg2, AOP.ADD);
        if(!result.equals(result2))System.out.println("###8isEquals="+ result.equals(result2)+
            " Mask=" + mask.repr.getKind() + " EnabledStates=" + mask.repr.numberOfEnabledStates() + " Index=" + mask.repr.getCurrentIndex());
      }
      return result;
    }else{
      return arith(arg1, arg2, AOP.ADD);
    }

  }


  public static DeltaInt add(final DeltaIntArray arg1, final DeltaIntConst arg2) {
    numOpsArrayConst++;

    if(General.C){      
      if (!Config.PARALLEL_DE) countDeltaOperations++;      
      final StateMask mask = StateMask.getStateMask();
      final DeltaIntArray result = (DeltaIntArray)DeltaIntCommon.create(mask.size());

      if(mask.repr.getKind() == StateMask.KINDS.CONTIGUOUS_REPRESENTATION){
        NativeDelta.ADDIntArrayConstContigousMask(arg1.values, arg2.getStrictConstant(), result.values);
      }else{
        NativeDelta.ADDIntArrayConst(arg1.values, arg2.getStrictConstant(), result.values, mask.repr.toArray(),
            mask.repr.getCurrentIndex(),mask.repr.numberOfEnabledStates(),mask.repr.getKind().ordinal());
      }
      if(Debug){
        final DeltaInt result2 = arith(arg1, arg2, AOP.ADD);
        if(!result.equals(result2))System.out.println("###9isEquals="+ result.equals(result2)+
            " Mask=" + mask.repr.getKind() + " EnabledStates=" + mask.repr.numberOfEnabledStates() + " Index=" + mask.repr.getCurrentIndex());
      }
      return result;
    }else{
      return arith(arg1, arg2, AOP.ADD);
    }    

  }


  public static DeltaInt add(final DeltaIntConst arg1, final DeltaIntConst arg2) {
    numOpsConstConst++;
    DeltaInt result = null;

    if(General.C){      
      int tmp = arg1.getStrictConstant() + arg2.getStrictConstant();
      result = DeltaIntConst.createConstant(tmp);      
    }else{
      result = arith(arg1, arg2, AOP.ADD);
    }    

    return result;
  }


  public static DeltaInt sub(final DeltaInt arg1, final DeltaInt arg2) {
    return arith(arg1, arg2, AOP.SUB);
  }  

  public static DeltaInt mult(final DeltaInt arg1, final DeltaInt arg2) {
    return arith(arg1, arg2, AOP.MULT);
  }  

  public static DeltaInt div(final DeltaInt arg1, final DeltaInt arg2) {
    return arith(arg1, arg2, AOP.DIV);
  }

  private static DeltaInt arith(final DeltaInt arg1, final DeltaInt arg2, final AOP op) {
    final boolean isStrictConstantOne;
    synchronized(arg1) { 
      isStrictConstantOne= arg1.isStrictConstant();      
    }
    final boolean isStrictConstantTwo;
    synchronized(arg2) { 
      isStrictConstantTwo= arg2.isStrictConstant();      
    }

    final DeltaInt result;

    if (isStrictConstantOne && isStrictConstantTwo) {      
      int tmp = aop(arg1.getStrictConstant(), arg2.getStrictConstant(), op);
      result = DeltaIntConst.createConstant(tmp);
    } else {
      if (!Config.PARALLEL_DE) countDeltaOperations++;      
      final StateMask mask = StateMask.getStateMask();
      result = DeltaIntCommon.create(mask.size());
      javaArithRun(arg1, arg2, op, isStrictConstantOne, isStrictConstantTwo, result, mask);
    }

    return result;
  }

  private static int aop(int arg1, int arg2, AOP op) {
    int result;
    switch (op) {
    case ADD:
      result = arg1 + arg2;
      break;
    case SUB:
      result = arg1 - arg2;
      break;
    case MULT:
      result = arg1 * arg2;
      break;
    case DIV:
      result = arg1 / arg2;
      break;
    default:
      throw new RuntimeException();
    }
    return result;
  }

  /***********************
   * main function
   ***********************/

  //  public static void main(String[] args) {
  //    int[] a1 = new int[500000];
  //    int[] a2 = new int[500000];
  //    Random r = new Random(32423);
  //    for (int i = 0; i < 10000; i++) {
  //      a1[i] = r.nextInt();
  //      a2[i] = r.nextInt();
  //    }
  //
  //    StateMask sm = new StateMask(a1.length);
  //    sm.setAll();
  //    StateMask.setStateMask(sm);
  //    DeltaInt di1 = DeltaIntArray.create(a1);
  //    DeltaInt di2 = DeltaIntArray.create(a2);
  //
  //    System.out.println("!");
  //    long l1 = System.currentTimeMillis();
  //    DeltaInt di3 = null;
  //    for (int i = 1; i <= 1000; i++) {
  //      di3 = add(di1, di2);
  //    }
  //    add(di3, di2);
  //    long l2 = System.currentTimeMillis() - l1;
  //    System.out.printf("%d ", l2);
  //  }

  public static DeltaBoolean and(DeltaBoolean false2, DeltaBoolean edgesNotBuilt) {
    throw new RuntimeException();
  }


  public static DeltaBoolean neq_bool(boolean visited, DeltaBoolean true2) {
    throw new RuntimeException();
  }


  public static void javaRopRun(final DeltaInt arg1, final DeltaInt arg2, final ROP op,
      final boolean isStrictConstantOne,
      final boolean isStrictConstantTwo, final DeltaBoolean result,
      final StateMask mask) {
    StateMaskIterator it = mask.intIterator();
    while (it.hasNext()) {
      int stateId = it.next();
      int lhs, rhs;

      // read value from argument 1
      if (isStrictConstantOne) {
        lhs = arg1.getStrictConstant();                 
      } else {
        lhs = arg1.getValueAt(stateId);
      }

      // read value from argument 2
      if (isStrictConstantTwo) {
        rhs = arg2.getStrictConstant();                   
      } else {
        rhs = arg2.getValueAt(stateId);
      }

      // set result
      boolean tmp = rop(lhs, rhs, op);
      result.setValueAt(stateId, tmp);
    }
  }


  public static void javaArithRun(final DeltaInt arg1, final DeltaInt arg2,
      final AOP op, final boolean isStrictConstantOne,
      final boolean isStrictConstantTwo, final DeltaInt result,
      final StateMask mask) {
    StateMaskIterator it = mask.intIterator();
    while (it.hasNext()) {
      int stateId = it.next();
      int lhs, rhs;

      // read value from argument 1
      if (isStrictConstantOne) {
        lhs = arg1.getStrictConstant();                 
      } else {
        lhs = arg1.getValueAt(stateId);
      }

      // read value from argument 2
      if (isStrictConstantTwo) {
        rhs = arg2.getStrictConstant();                   
      } else {
        rhs = arg2.getValueAt(stateId);
      }

      // set result
      int tmp = aop(lhs, rhs, op);
      result.setValueAt(stateId, tmp);
    }
  }

}