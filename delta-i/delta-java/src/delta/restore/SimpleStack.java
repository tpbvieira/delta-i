package delta.restore;

public class SimpleStack<E> {

  protected class StackNode<T> {
    E data;
    StackNode<E> next;
    StackNode(E data, StackNode<E> next) {
      this.data = data;
      this.next = next;
    }
  }

  protected StackNode<E> head;

  protected int size;

  public SimpleStack() {
    head = null;
    size = 0;
  }

  public void push(E elem) {
    head = new StackNode<E>(elem, head);
    size++;
  }

  public E pop() {
    E ret = null;
    if (head != null) {
      ret = head.data;
      head = head.next;
      size--;
    }
    return ret;
  }

  public int size() {
    return size;
  }

  public void clear() {
    head = null;
    size = 0;
  }

//  public MyStack(MyStack<E> undoStack) {
//    head = cloneStack(undoStack.head);
//    size = undoStack.size;
//  }
//
//  private StackNode<E> cloneStack(StackNode<E> original){
//    StackNode<E> ret = null;
//    if (original != null) {
//      ret = new StackNode<E>(original.data, cloneStack(original.next));
//    }
//    return ret;
//  }
  
}