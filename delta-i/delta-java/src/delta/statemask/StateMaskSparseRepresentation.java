package delta.statemask;

import delta.util.DynamicIntArray;

public class StateMaskSparseRepresentation implements StateMaskRepresentation {

  /*** document assumptions ***/

  int numStates;
  int[] enabledStates;
  /************************************************************
   * the results includes the content from enabledStates, plus
   * that of addedStates and minus that of deletedStates
   ************************************************************/
  DynamicIntArray addedStates;
  /************************************************************
   * this is only used within subsumption.  After merging the
   * enabled states (which occur right after subsumption), such
   * mask is no longer used.
   ***********************************************************/
  DynamicIntArray deletedStates;

  public StateMaskSparseRepresentation(int numStates) {
    this.numStates = numStates;
  }

  /*******************************************************/
  public int[] arrayOfEnabledStates() {
    align();
    return enabledStates;
  }

  /*******************************************************/
  private void align() {
    if (deletedStates != null) {
      alignWithDeletes();
    } else {
      alignWithNoDeletes();
    }
  }

  /*******************************************************/
  private void alignWithDeletes() {
    assert (enabledStates != null && addedStates == null);
    int lenSrc = enabledStates.length;
    /**********************************************
     * assuming deleted is a subset of the elements
     * appearing in source!  The code will break
     * otherwise with an "array index out of bounds
     * exception".
     *********************************************/
    int sizeDeleted = deletedStates.size();
    int lenNewAr = lenSrc - sizeDeleted;
    int[] newAr = new int[lenNewAr];
    int index = 0; // index for newAr
    int j = 0; // optimization to avoid quadratic algorithm
    int vi = -1, vj = -1;
    a: for (int i = 0; i < lenSrc; i++) {
      vi = enabledStates[i];
      for (; j < sizeDeleted; j++) {
        vj = deletedStates.get(j);
        if (vj == vi) { // match! ignore the elements
          j++; // move j
          continue a; // move i
        } else if (vj > vi) {
          // read from i, move i
          newAr[index++] = vi;
          continue a;
        } else { // vj < vi
          String strMsg = "I am assuming deleted elements is a " +
          "subset of the source.  Assumption violated.";
          throw new RuntimeException(strMsg);
        }
      }
      // if we get to here all remaining elements of
      // source must be available...
      System.arraycopy(enabledStates, i, newAr, index, lenSrc - i);
      break;
    }
    enabledStates = newAr;
    resetCaching();
  }

  /*******************************************************/
  private void alignWithNoDeletes() {
    if (enabledStates == null) {
      int[] ar = new int[addedStates.size()];
      addedStates.toArray(ar);
      enabledStates = ar;
    }
    resetCaching();
  }

  /*******************************************************/
  private void resetCaching() {
    addedStates = null;
    deletedStates = null;
  }

  /*******************************************************/
  public int numberOfEnabledStates() {
    // this precondition seem stronger than necessary but
    // works fine for us
    assert ((enabledStates != null) ^ (addedStates != null));
    int result = -1;
    if (enabledStates != null) {
      result = enabledStates.length;
    } else {
      result = addedStates.size();
    }
    if (deletedStates != null) {
      result -= deletedStates.size();
    }
    return result;
  }

  /*******************************************************/
//static int bla = 0 ;
  public StateMaskIterator intIterator() {
    align();
    StateMaskIterator it = new StateMaskIterator() { 
      int current = 0;
      int size = enabledStates.length;
      public boolean hasNext() {
        return current < size;
      }
      public int next() {
        return enabledStates[current++];
      }
      public int size(){
        return size;
      }
    };
    return it;
  }
  
  public int[] toArray(){    
    return enabledStates;
  }
  
  public int getCurrentIndex(){
    return -1;
  }


  /*******************************************************/
  public int size() {
    return numStates;
  }

  /*******************************************************/
  public void setAt(int stateId) {
    assert (enabledStates == null);
    if (addedStates == null) {
      addedStates = new DynamicIntArray();
    }
    addedStates.set(addedStates.size(), stateId);
  }

  /*******************************************************/
  public void unsetAt(int stateId) {
    assert (enabledStates != null && addedStates == null);
    if (deletedStates == null) {
      deletedStates = new DynamicIntArray();
    }
    deletedStates.set(deletedStates.size(), stateId);
  }

  /*******************************************************/
  public void setAll() {
    resetCaching();
    if (enabledStates == null || enabledStates.length != numStates) {
      enabledStates = new int[numStates];
    }
    // one optimization is having a flag to denote all enabled
    for (int i = 0; i < numStates; i++) {
      enabledStates[i] = i;
    }
  }

  /*******************************************************/
  public Object clone() {
    align();
    StateMaskSparseRepresentation other =
      new StateMaskSparseRepresentation(numStates);
    int len = enabledStates.length;
    other.enabledStates = new int[len];
    System.arraycopy(this.enabledStates, 0, other.enabledStates, 0, len);
    return other;
  }


  // there might be better implementation for this...
  private static final int USE_MUTATION = 0;
  private static final int NO_MUTATION = 1;
  /*******************************************
   * NOTE: if we use mutation the code breaks
   ******************************************/
  private static final int MUTATION_CHOICE = NO_MUTATION;
  private static MaskPair result = new MaskPair();
  public MaskPair genMasksByPredicate(MaskPredicate maskPred) {
    StateMask trueMask;
    switch (MUTATION_CHOICE) {
    case USE_MUTATION:
      trueMask = new StateMask(this);
      break;
    case NO_MUTATION:
      trueMask = new StateMask(this.size());
      break;
    default:
      throw new RuntimeException("invalid option");
    }
    StateMask falseMask = new StateMask(this.size());
    return genMasksByPredicate(maskPred, trueMask, falseMask);
  }


  /*******************************************************/
  public MaskPair genMasksByPredicate(MaskPredicate maskPred, StateMask trueMask, StateMask falseMask) {
    int[] ar = arrayOfEnabledStates();
    int num = 0;
    for (int i = 0; i < ar.length; i++) {
      int stateId = ar[i];
      if (maskPred.pred(stateId)) {
        trueMask.setAt(stateId);
        num++;
      } else {
        falseMask.setAt(stateId);
      }
    }
    result.setNumTrue(num);
    result.setTrueMask(trueMask);
    result.setFalseMask(falseMask);
    return result;
  }


  public String toString() {
    align() ;
    java.util.List list = new java.util.LinkedList() ;
    for (int i = 0 ; i < enabledStates.length ; i++) {
      list.add(enabledStates[i]) ;
    }
    return list.toString() ;        
  }

  @Override
  public boolean isSetAt(int i) {
    throw new UnsupportedOperationException();
  }
  
  public StateMask.KINDS getKind(){
    return StateMask.KINDS.SPARSE_REPRESENTATION;
  }

}

