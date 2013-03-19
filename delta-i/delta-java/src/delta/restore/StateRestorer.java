package delta.restore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import delta.statemask.StateMask;



public class StateRestorer {
  
  public static void clear() {
    doMap = new HashMap<Integer, Doable<?>>();
    redo = new HashMap<StateMask,Collection<Doable<?>>>();
  }

  public static SimpleStack<Undoable<?>> undoStack = 
    new SimpleStack<Undoable<?>>();

  public static void restoreInitialAndSaveFinals() {
    // restore initial state
    while (undoStack.size() > 0) {
      Undoable<?> undoable = undoStack.pop();
      undoable.undoit();
    }
    // save new values!
    redo.put(StateMask.getStateMask(),doMap.values());
  }

  public static void add(Undoable<?> undoItem) {
    undoStack.push(undoItem);
  }

  public static void mergeChanges(){
    for (Map.Entry<StateMask, Collection<Doable<?>>> entry : redo.entrySet()) {
      StateMask sm = entry.getKey();
      for (Doable<?> _do : entry.getValue()) {
        _do.doit(sm);
      }
    }    
  }

  /***************************************
   * only the last change in a field is 
   * of interest.  that change will persist
   * even with reduced state masks.
   *************************************/
  public static Map<StateMask,Collection<Doable<?>>> redo = 
    new HashMap<StateMask,Collection<Doable<?>>>();
  public static Map<Integer,Doable<?>> doMap = 
    new HashMap<Integer, Doable<?>>();

  public static void add(int hash, Doable<?> _do) {
    doMap.put(hash, _do);
  }

}