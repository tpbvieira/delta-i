package delta.statemask;

public interface StateMaskRepresentation {

  /*** these are used to visit the enabled states ***/
  public int[] arrayOfEnabledStates() ;

  public int numberOfEnabledStates() ;

  public int[] toArray() ;
  
  public int getCurrentIndex() ;
  
  /****************************************************
   * arrayOfEnabledStates can be expensive in sparsed
   * representation because it could have the information
   * stored in multiple places, not necessarily an array
   * but different data structures.
   ****************************************************/
  public StateMaskIterator intIterator() ;

  /*** number of states = enabled + disabled ***/
  public int size() ;

  /*** these are used to mutate the mask ***/
  public void setAt(int i) ;

  /*******************************************************
   *
   * I did not remove this because subsumption can
   * be made more efficient by mutating the mask (disabling
   * states).  The approach of creating a different mask and
   * adding the states non-subsumed adds ~.3s when compared
   * to the current approach that uses "unsetAt" in an
   * experiment that takes ~9.5s.
   *
   * For the same reason, we should consider mutating the
   * current mask in Delta[Int,Ref] instead of creating
   * trueMask and reseting current.
   *
   *******************************************************/
  public void unsetAt(int i) ;

  /*** no state is enabled by default ***/
  public void setAll() ;

  /*** clone method (could be removed) ***/
  public Object clone() ;

  /******************************************************
   * evaluates a predicate over a mask and returns a
   * trueMask for those state whose predicate evaluate
   * to true and a falseMask for those states where the
   * predicate evaluates to false
   ******************************************************/
  public MaskPair genMasksByPredicate(MaskPredicate pred) ;

  public boolean isSetAt(int i);
  
  public StateMask.KINDS getKind();

}