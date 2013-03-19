package delta.statemask;

public class StateMaskContiguousRepresentation implements StateMaskRepresentation {

  private int size ;
  private boolean allSet ;

  public StateMaskContiguousRepresentation(int size) {
    this.size = size ;
  }    

  private StateMaskContiguousRepresentation(int size, boolean allSet) {
    this.size = size ;
    this.allSet = allSet ;
  }


  public StateMaskIterator intIterator() {
    return new StateMaskIterator() { 
      int current = 0;
      int size = allSet ? StateMaskContiguousRepresentation.this.size : 0 ;
      public boolean hasNext() {
        return current < size;
      }
      public int next() {
        return current++ ;
      }
      public int size(){
        return size;
      }
    };
  }
  
  public int[] toArray(){    
    return null;
  }
  
  public int getCurrentIndex(){
    return -1;
  }

  /*** number of states = enabled + disabled ***/
  public int size() {
    return size ;        
  }

  public static class ContiguousException extends RuntimeException {}

  /*** these are used to mutate the mask ***/
  public void setAt(int i) {
    // translate to a different representation 
    // and perform the operation
    throw new ContiguousException() ;
  }

  public void unsetAt(int i) { 
    throw new ContiguousException() ;
  }

  /*** no state is enabled by default ***/
  public void setAll() {
    allSet = true ;        
  }
  /*** clone method (could be removed) ***/
  public Object clone() {
    return new StateMaskContiguousRepresentation(size, allSet) ;        
  }

  private static MaskPair result = new MaskPair();
  
  public MaskPair genMasksByPredicate(MaskPredicate maskPred) {
    if (!allSet) {
      result.setNumTrue(0);
      result.setTrueMask(new StateMask(this));
      result.setFalseMask(null);            
    } else {
      int num = 0;
      boolean isFirst = true;
      boolean expected = true;
      
      for (int stateId = 0; stateId < size; stateId++) {
        if (maskPred.pred(stateId)) {
          num++;
          if (isFirst) {
            expected = true ;                        
          } else if (!expected){
            throw new ContiguousException() ;
          }
        } else {
          if (isFirst) {
            expected = false ;
          } else if (expected){
            throw new ContiguousException() ;
          }
        }
        isFirst = false ;
      }
      
      result.setNumTrue(num);
      if (expected) {
        result.setTrueMask(new StateMask(this));
        result.setFalseMask(null);                
      } else {
        result.setFalseMask(new StateMask(this));
        result.setTrueMask(null);                                
      }
    }

    return result;        
  }

  /*** these are used to visit the enabled states ***/
  public int[] arrayOfEnabledStates() {
    throw new UnsupportedOperationException() ;
  }

  public int numberOfEnabledStates() {
    return allSet ? size : 0; 
  }

  public String toString() {
    java.util.List list = new java.util.LinkedList() ;
    if (allSet) {
      for (int i = 0 ; i < size ; i++) {
        list.add(i) ;
      }
    }
    return list.toString() ;        
  }

  @Override
  public boolean isSetAt(int i) {
    return allSet ? i < size : false;
  }    

  public StateMask.KINDS getKind(){
    return StateMask.KINDS.CONTIGUOUS_REPRESENTATION;
  }
  
}