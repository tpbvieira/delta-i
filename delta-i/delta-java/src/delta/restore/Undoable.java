package delta.restore;


public interface Undoable<T> {

  void undoit();

}
