package delta.util;

public class ArrayStack<T> {    

  private static final int EMPTY = -1;
  private static final int MAX_SIZE = 100;    
  private Object[] elems = new Object[MAX_SIZE];
  private int lastIndex = EMPTY;

  public void push(T a) { elems[++lastIndex]=a; }

  @SuppressWarnings("serial")
  public static class EmptyStack extends Exception {
    public EmptyStack(String msg) {
      super(msg);
    }
  }

  @SuppressWarnings("unchecked")
  public T pop() throws EmptyStack {
    if (lastIndex==EMPTY) {
      throw new EmptyStack("stack is empty");
    }
    return (T) elems[lastIndex--];
  }

  @SuppressWarnings("unchecked")
  public T peek() { return (T) elems[lastIndex]; }

  public int size() { return lastIndex+1; }

}
