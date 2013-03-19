package delta.statemask;

/**********************************************
 * We did not use java.util.Iterator because it
 * enforces the use of Objects and we want to 
 * avoid casting and calling intValue() for the 
 * state ids returned by the iterator.
 *********************************************/

public interface StateMaskIterator {

  public int next() ;

  public boolean hasNext() ;
  
  public int size();

}
