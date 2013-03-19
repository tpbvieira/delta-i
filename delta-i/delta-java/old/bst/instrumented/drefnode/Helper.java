package bst.instrumented.drefnode;

import bst.instrumented.Node;
import delta.statemask.StateMask;
import delta.statemask.StateMaskIterator;
import delta.util.IdentityMap;
import deltalib.dint.DeltaInt;
import deltalib.dint.DeltaIntCommon;
import deltalib.dint.DeltaIntConst;
import deltalib.dref.ConstantInfoRef;


public class Helper {
    
  //-- getters and setters

  public static DeltaRefNode get_left(DeltaRefNode thizz) {
    DeltaRefNode result;
    StateMask sm = StateMask.getStateMask();
    ConstantInfoRef<Node> constInfo = thizz.getConstantInfo(sm);
    if (constInfo.isConstant()) {
      result = constInfo.getConstant().get_left();
    } else {
      result = new DeltaRefArrayNode(new Node[sm.size()]);
      StateMaskIterator it = sm.intIterator();
      while (it.hasNext()) {
        int index = it.next();
        Node subject = thizz.getValueAt(index);
        DeltaRefNode temp = subject.get_left();
        if (temp.isStrictConstant()) {
          result.setValueAt(index, temp.getStrictConstant());
        } else {
          result.setValueAt(index, temp.getValueAt(index));
        }
      }
    }
    return result;
  }

  public static DeltaRefNode get_right(DeltaRefNode thizz) {
    DeltaRefNode result;
    StateMask sm = StateMask.getStateMask();
    ConstantInfoRef<Node> constInfo = thizz.getConstantInfo(sm);
    if (constInfo.isConstant()) {
      result = constInfo.getConstant().get_right();
    } else {
      result = new DeltaRefArrayNode(new Node[sm.size()]);
      StateMaskIterator it = sm.intIterator();
      while (it.hasNext()) {
        int index = it.next();
        Node subject = thizz.getValueAt(index);
        DeltaRefNode temp = subject.get_right();
        if (temp.isStrictConstant()) {
          result.setValueAt(index, temp.getStrictConstant());
        } else {
          result.setValueAt(index, temp.getValueAt(index));
        }
      }
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static void set_left(DeltaRefNode thizz, DeltaRefNode arg) {
    StateMask sm = StateMask.getStateMask();
    ConstantInfoRef<Node> constInfo = thizz.getConstantInfo(sm);
    if (constInfo.isConstant()) {
      constInfo.getConstant().set_left(arg);
    } else {
      // build different arrays for different objects (but just once)
      IdentityMap map = new IdentityMap();
      StateMaskIterator it = sm.intIterator();
      while (it.hasNext()) {
        int index = it.next();
        Node temp = thizz.getValueAt(index);
        DeltaRefNode newRefs;
        if (map.containsKey(temp)) {
          newRefs = (DeltaRefNode) map.get(temp);
        } else {
          DeltaRefNode old = temp.get_left();
          if (old.isStrictConstant()) {
            newRefs = new DeltaRefArrayNode(old.getStrictConstant());
          } else {
            newRefs = old.copy();
          }
          map.put(temp, newRefs);
        }
        newRefs.setValueAt(index, 
            arg.isStrictConstant() ? 
                arg.getStrictConstant() : 
                  arg.getValueAt(index));
      }

      for (int i = 0; i < map.size(); i++) {
        IdentityMap.Entry entry = (IdentityMap.Entry) map.get(i);
        Node subject = (Node) entry.getKey();
        subject.set_left(((DeltaRefNode) entry.getValue()));        
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static void set_right(DeltaRefNode thizz, DeltaRefNode arg) {
    StateMask sm = StateMask.getStateMask();
    ConstantInfoRef<Node> constInfo = thizz.getConstantInfo(sm);
    if (constInfo.isConstant()) {
      constInfo.getConstant().set_right(arg);
    } else {
      // build different arrays for different objects (but just once)
      IdentityMap map = new IdentityMap();
      StateMaskIterator it = sm.intIterator();
      while (it.hasNext()) {
        int index = it.next();
        Node temp = thizz.getValueAt(index);
        DeltaRefNode newRefs;
        if (map.containsKey(temp)) {
          newRefs = (DeltaRefNode) map.get(temp);
        } else {
          DeltaRefNode old = temp.get_right();
          if (old.isStrictConstant()) {
            newRefs = new DeltaRefArrayNode(old.getStrictConstant());
          } else {
            newRefs = old.copy();
          }
          map.put(temp, newRefs);
        }
        newRefs.setValueAt(index, 
            arg.isStrictConstant() ? 
                arg.getStrictConstant() : 
                  arg.getValueAt(index));
      }

      for (int i = 0; i < map.size(); i++) {
        IdentityMap.Entry entry = (IdentityMap.Entry) map.get(i);
        Node subject = (Node) entry.getKey();
        subject.set_right(((DeltaRefNode) entry.getValue()));        
      }
    }
  }


  public static DeltaInt get_info(DeltaRefNode thizz) {
    DeltaInt result;
    StateMask sm = StateMask.getStateMask();
    ConstantInfoRef<Node> constInfo = thizz.getConstantInfo(sm);
    if (constInfo.isConstant()) {
      Node subject = (Node) constInfo.getConstant();
      result = subject.get_info();
    } else {
      result = DeltaIntCommon.create(sm.size());
      StateMaskIterator it = sm.intIterator();
      while (it.hasNext()) {
        int index = it.next();
        Node subject = thizz.getValueAt(index);
        DeltaInt temp = subject.get_info();
        if (temp.isStrictConstant()) {
          result.setValueAt(index, temp.getStrictConstant());
        } else {
          result.setValueAt(index, temp.getValueAt(index));
        }
      }
    }
    return result;
  }

  /*******************************************************/
  public static void set_info(DeltaRefNode thizz, DeltaInt arg) {
    StateMask sm = StateMask.getStateMask();
    ConstantInfoRef<Node> constInfo = thizz.getConstantInfo(sm);
    if (constInfo.isConstant()) {
      Node subject = (Node) constInfo.getConstant();
      subject.set_info(arg);
    } else {
      // build an array for each different object
      IdentityMap map = new IdentityMap();
      StateMaskIterator it = sm.intIterator();
      while (it.hasNext()) {
        int index = it.next();
        Node temp = thizz.getValueAt(index);
        DeltaInt newInts;
        if (map.containsKey(temp)) {
          newInts = (DeltaInt) map.get(temp);
        } else {          
          DeltaInt old = temp.get_info();
          if (old.isStrictConstant()) {
            newInts = DeltaIntConst.cloneExpanded((DeltaIntConst)old);
          } else {
            newInts = DeltaIntCommon.clone(old);
          }
          map.put(temp, newInts);
        }
        newInts.setValueAt(index, 
            arg.isStrictConstant() ? 
                arg.getStrictConstant() : 
                  arg.getValueAt(index));
      }
      for (int i = 0; i < map.size(); i++) {
        IdentityMap.Entry entry = (IdentityMap.Entry) map.get(i);
        Node subject = (Node) entry.getKey();
        subject.set_info((DeltaInt) entry.getValue());
      }
    }
  }


}