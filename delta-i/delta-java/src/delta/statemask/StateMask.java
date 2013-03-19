package delta.statemask;

import java.util.HashMap;

import delta.util.Common;
import delta.util.Config;

/*************************************************
 *
 * The objects of this class represent a mask of
 * the collapsed state.  It shows which individual
 * states are enabled in the collapsed one.
 *
 *************************************************/

public class StateMask implements Cloneable {

  private static StateMask stateMask = null;
  public StateMaskRepresentation repr;

  public static enum KINDS {DENSE_REPRESENTATION,
                            SPARSE_REPRESENTATION,
                            SHARED_SPARSE_REPRESENTATION,
                            CONTIGUOUS_REPRESENTATION}

  String warningMsg = "please change the code that relies on "+
  "this interface.  Making the internal representation of the " +
  "state mask visible (public) breaks the encapsulation of this " +
  "component.";

  public StateMask(StateMaskRepresentation representation) {
    this.repr = representation;
  }

  public StateMask(KINDS kindRepresentation, int size) {
    switch(kindRepresentation) {
    case DENSE_REPRESENTATION :
      this.repr = new StateMaskDenseRepresentation (size);
      break;       
    case SPARSE_REPRESENTATION :
      this.repr = new StateMaskSparseRepresentation(size);
      break;
    case SHARED_SPARSE_REPRESENTATION :
      this.repr = new StateMaskSharedRepresentation(size);
      break;
    case CONTIGUOUS_REPRESENTATION :
      this.repr = new StateMaskContiguousRepresentation(size);
      break;
    default : throw new RuntimeException("no valid matching");
    }
  }

  public StateMask(int size) {
    this(Common.getDeltaStateMaskRepresentation(), size);
  }

  /********************************************************/
  public StateMask(boolean[] mask) {
    throw new RuntimeException(warningMsg);
  }

  public void set(boolean[] arg) {
    throw new RuntimeException(warningMsg);
  }

  /**
   *@TODO: please be careful with memory leak.  a finished thread
   *should remove its entry from this table!
   */
  private static HashMap<Thread, StateMask> smThread = new HashMap<Thread, StateMask>();
  
  /********************************************************/
  public static StateMask getStateMask() {
    if (Config.PARALLEL_DE) {
      /**
       *@TODO: check better implementation.  i am concerned about
       * the cost of too may call to map.get  
       */
      Thread t = Thread.currentThread();
      StateMask sm = smThread.get(t);
      if (sm == null) {      
        throw new RuntimeException("could not find mask for thread " + t);
      }
      return sm;
    } else {
      if (stateMask == null) {
        throw new RuntimeException("Please, set the state mask!");
      }
      return stateMask;
    }
  }
  
  
  /********************************************************/
  public static void setStateMask(StateMask arg) {
    if (Config.PARALLEL_DE) {
      Thread t = Thread.currentThread(); 
      smThread.put(t, arg);
//System.out.println("> " + t);      
    } else {
      stateMask = arg;
    }
  }
  

  public static void clearThreadMap(Thread t) {
//System.out.println("< " + t);
    smThread.remove(t);
  }

  /******** these methods operated on the actual representation ******/

  /********************************************************/
  public void setAll() {
    this.repr.setAll();
  }

  /********************************************************/
  public int size() {
    return this.repr.size();
  }

  /********************************************************/
  public boolean get(int i) {
    throw new RuntimeException(warningMsg);
  }

  /********************************************************/
  public void setAt(int i) {
    this.repr.setAt(i);
  }

  /********************************************************/
  public void unsetAt(int i) {         
    try {
      this.repr.unsetAt(i);
    } catch (StateMaskContiguousRepresentation.ContiguousException _) {
      // dynamic configuration of state masks
      int size = this.repr.numberOfEnabledStates() ; // either 0 or all
      StateMask sMask = new StateMask(size) ;
      sMask.setAll(); // it must be all set in this context
      // clients have a hold of the current state mask!
      // so we need to replace the internal representation
      this.repr = sMask.repr;
      StateMask.setStateMask(sMask);
      sMask.unsetAt(i);            
    }
  }

  /********************************************************/
  public int numberOfEnabledStates() {
    return this.repr.numberOfEnabledStates();
  }

  /********************************************************/
  public int[] arrayOfEnabledStates() {
    return this.repr.arrayOfEnabledStates();
  }

  /********************************************************/
  public StateMaskIterator intIterator() {
    return this.repr.intIterator();
  }
  
  public int[] toIntArray() {
       
    return repr.toArray();
  }

  /********************************************************/
  public Object clone() {
    StateMaskRepresentation rep = (StateMaskRepresentation) this.repr.clone();
    return new StateMask(rep);
  }

  /********************************************************/
  public MaskPair genMasksByPredicate(MaskPredicate pred) {
    MaskPair result ;
    try {
      result = this.repr.genMasksByPredicate(pred);
    } catch (StateMaskContiguousRepresentation.ContiguousException _) {
      // dynamic configuration of state masks
      int size = this.repr.numberOfEnabledStates() ; // either 0 or all
      StateMask sMask = new StateMask(size) ;
      sMask.setAll(); // it must be all set in this context
      // clients have a hold of the current state mask!
      // so we need to replace the internal representation
      this.repr = sMask.repr ;
      StateMask.setStateMask(sMask);
      result = sMask.genMasksByPredicate(pred);       
    }
    return result ;
  }

  public StateMask buildAnother() {
    StateMask result = new StateMask(this.repr);
    return result;
  }

  public String toString() {
    return this.repr.toString() ;
  }

  public boolean isSetAt(int i) {
    return this.repr.isSetAt(i);
  }


}