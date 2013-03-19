package delta.statemask;

import delta.util.DynamicIntArray;

public class StateMaskSharedRepresentation implements StateMaskRepresentation, Cloneable {

  /*** document assumptions ***/

  int numStates;
  int[] enabledStates;

  // start is inclusive indices... end is not... end > start
  int start;
  int end;

  /************************************************************
   * this is only used within subsumption.  After merging the
   * enabled states (which occur right after subsumption), such
   * mask is no longer used.
   ***********************************************************/
  DynamicIntArray deletedStates;

  public StateMaskSharedRepresentation(int numStates) {
    this.numStates = numStates;
    this.enabledStates = new int[numStates];
    this.start = 0;
    this.end = 0;
  }

  public StateMaskSharedRepresentation(int numStates, int start, int end, int[] enabledStates) {
    this.numStates = numStates ;
    this.start = start;
    this.end = end;
    this.enabledStates = enabledStates;
  }

  /*******************************************************/
  public int[] arrayOfEnabledStates() {
    //  throw new RuntimeException("bad call to arrayOfEnabledStates");
    int[] result = new int[enabledStates.length];
    System.arraycopy(enabledStates, 0, result, 0, result.length);
    return result; 
  }

  /*******************************************************/
  private void align() {
    if (deletedStates != null) {
      alignWithDeletes();
    }
  }

  private void alignWithDeletes() {
    alignWithDeletes(deletedStates) ;
  }

  /*******************************************************/
  public void alignWithDeletes(DynamicIntArray deletedStates) {
    //  System.out.println(this) ;
    /*******************************************************
     * preconditions:
     *   delete array is a subset of enabledStates
     *   delete array is in the same order as enabledStates
     *******************************************************/
    int leftIndex = 0;
    int rightIndex = deletedStates.size() - 1;
    int current = start;

    while (leftIndex <= rightIndex) { // while there are elements to delete
      // find one element to delete
      if (enabledStates[current] != deletedStates.get(leftIndex)) {
        current++;
      } else {
        // current holds one element to delete
        while ( (end > 0) && 
            (rightIndex > 0) && 
            (rightIndex >= leftIndex) && 
            (enabledStates[end - 1] == deletedStates.get(rightIndex))) {// Deletes the last states first, reducing the "end" index
          rightIndex--;
          end--;
        }
        // end - 1 holds one element to keep
        if (end != current) {
          leftIndex++;
          enabledStates[current++] = enabledStates[--end];
        }
      }
    }
    this.deletedStates = null;
  }



  /*******************************************************/
  public int numberOfEnabledStates() {
    align() ; // this is not a very good place for this
    return (end - start);
  }

  /*******************************************************/
  public StateMaskIterator intIterator() {
    align();
    StateMaskIterator it = new StateMaskIterator() {
      int current = start;
      int stop = end;
      public boolean hasNext() {
        return current < stop;
      }
      public int next() {
        return enabledStates[current++];
      }
      public int size(){
        return end - start;
      }
    };
    return it;
  }

  public int[] toArray(){
    return enabledStates;
  }

  /*******************************************************/
  public int size() {
    return numStates;
  }

  /*******************************************************/
  public void setAt(int stateId) {
    enabledStates[end++] = stateId;
  }

  /*******************************************************/
  public void unsetAt(int stateId) {
    if (deletedStates == null) {
      deletedStates = new DynamicIntArray(100);
    }
    deletedStates.set(deletedStates.size(), stateId);
  }

  /*******************************************************/
  public void setAll() {
    for (int i = 0; i < numStates; i++) {
      enabledStates[i] = i;
    }
    start = 0 ;
    end = numStates;
  }

  /*******************************************************/
  public Object clone() {
    align();
    Object other;

    try {
      other = super.clone();
      // avoid shallow copy of the array reference!
      ((StateMaskSharedRepresentation)other).enabledStates = new int[numStates] ;
    } catch (CloneNotSupportedException _) {
      _.printStackTrace() ;
      throw new RuntimeException("must implement Cloneable") ;
    }
    System.arraycopy(this.enabledStates, 0, ((StateMaskSharedRepresentation)other).enabledStates, 0, numStates);
    return other;
  }


  /*******************************************************/
  private static MaskPair result = new MaskPair();

  public MaskPair genMasksByPredicate(MaskPredicate maskPred) {
    int i = start;
    int j = end ;
    int numTrue = 0 ;

    a: for (; i < j ; i++) {
      if (maskPred.pred(enabledStates[i])) {
        numTrue++ ;
        continue ;
      } 
      for (; j > i ; j--) {
        int actualIndex = j - 1;
        if (maskPred.pred(enabledStates[actualIndex])) { //If what??
          int tmp = enabledStates[actualIndex] ;
          enabledStates[actualIndex] = enabledStates[i] ;
          enabledStates[i] = tmp ;
          numTrue++ ;
          j--; // break or continue would not decreement the loop index
          break ; 
        }
      }
      if (i == j){
        break ;
      }
    }           
    assert i == j ;
    /******************************************************
     * the split depends on whether the last visited states
     * evaluated true or false to the predicate  
     ******************************************************/
    StateMaskSharedRepresentation trueRep = new StateMaskSharedRepresentation(numStates, start, i, enabledStates);
    StateMask trueMask = new StateMask(trueRep);
    StateMaskSharedRepresentation falseRep = new StateMaskSharedRepresentation(numStates, i, end, enabledStates);
    StateMask falseMask = new StateMask(falseRep);
    
    result.setNumTrue(numTrue);
    result.setTrueMask(trueMask);
    result.setFalseMask(falseMask);
    
    return result;
  }

  public String toString() {
    if (deletedStates != null){
      alignWithDeletes();
    }
    
    java.util.List l = new java.util.LinkedList() ;
    
    for (int i = start ; i < end ; i++) {
      l.add(enabledStates[i]) ;
    }
    
    StringBuffer sb = new StringBuffer(l.toString());
    return sb.toString() ;
  }

  @Override
  public boolean isSetAt(int i) {
    if (deletedStates!=null && deletedStates.size() > 0) {
      throw new UnsupportedOperationException();
    }
    
    boolean result = false;
    
    for (int j = start; j < end; j++) {
      if (enabledStates[j]==i) {
        result = true; 
        break;
      }
    }
    
    return result;
  }
  
  public int getCurrentIndex(){
    return start;
  }
  
  public StateMask.KINDS getKind(){
    return StateMask.KINDS.SHARED_SPARSE_REPRESENTATION;
  }

}