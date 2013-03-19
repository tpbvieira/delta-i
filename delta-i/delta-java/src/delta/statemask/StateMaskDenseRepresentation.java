package delta.statemask;

import java.util.Arrays;


public class StateMaskDenseRepresentation implements StateMaskRepresentation {

  private boolean[] mask;
  private int[] arrayOfEnabledStates;

  public StateMaskDenseRepresentation (boolean[] MaskArray) {
    if (MaskArray == null) {
      throw new IllegalArgumentException("Cannot override empty masks");
    }

    this.mask = MaskArray;
    resetCacheForIndicesOfEnabledStates();
  }


  /********************************************************/
  public StateMaskDenseRepresentation (int numStates) {
    this(new boolean[numStates]);
  }

  /********************************************************/
  public void set(boolean[] arg) {
    throw new RuntimeException("this should not be called");
  }

  /********************************************************/
  public void setAll() {
    Arrays.fill(mask, true);
    resetCacheForIndicesOfEnabledStates();
  }

  /********************************************************/
  public int size() {
    return mask.length;
  }

  /********************************************************/
  public boolean get(int i) {
    throw new RuntimeException("not used here...");
//  return mask[i];
  }

  /********************************************************/
  public void setAt(int i) {
//  throw new RuntimeException("not used here...");
    mask[i] = true;
    resetCacheForIndicesOfEnabledStates();
  }

  /********************************************************/
  // this is used by the subsumption..
  public void unsetAt(int i) {
    mask[i] = false;
    resetCacheForIndicesOfEnabledStates();
  }

  /********************************************************/
  public int numberOfEnabledStates() {
    if (arrayOfEnabledStates != null) {
      return arrayOfEnabledStates.length;
    }
    
    int result = 0;
    
    for (int i = 0; i < mask.length; i++) {
      result += (mask[i] ? 1 : 0);
    }
    return result;
  }

  /********************************************************/
  public void resetCacheForIndicesOfEnabledStates() {
    arrayOfEnabledStates = null;
  }

  /********************************************************/
  public int[] arrayOfEnabledStates() {//TODO
    if (arrayOfEnabledStates != null) {
      return arrayOfEnabledStates;
    }
    
    int[] temp = new int[mask.length];
    int j = 0;
    
    for (int i = 0; i < mask.length; i++) {
      if (mask[i]){
        temp[j++] = i;
      }
    }

    int[] result = new int[j];
    System.arraycopy(temp, 0, result, 0, j);
    arrayOfEnabledStates = result;
    
    return result;
  }

  /********************************************************/
  public StateMaskIterator intIterator() {
    final int[] refCopy = arrayOfEnabledStates();
    StateMaskIterator it = new StateMaskIterator() {
      int current = 0;
      int size = refCopy.length;
      public boolean hasNext() {
        return current < size;
      }
      public int next() {
        return refCopy[current++];
      }
      public int size(){
        return size;
      }
    };
    return it;
  }

  public int[] toArray(){
    return arrayOfEnabledStates();
  }
  
  public int getCurrentIndex(){
    return -1;
  }
  
  /********************************************************/
  public Object clone() {
    boolean[] newMask = new boolean[mask.length];
    System.arraycopy(mask, 0, newMask, 0, mask.length);
//  return new StateMask(newMask);
    return new StateMaskDenseRepresentation(newMask);
  }


  /********************************************************/
  private static MaskPair result = new MaskPair();
  public MaskPair genMasksByPredicate(MaskPredicate maskPred) {
    StateMask trueMask = new StateMask(this.size());
    StateMask falseMask = new StateMask(this.size());
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


  @Override
  public boolean isSetAt(int i) {
    throw new UnsupportedOperationException();
  }
  
  public StateMask.KINDS getKind(){
    return StateMask.KINDS.DENSE_REPRESENTATION;
  }

}