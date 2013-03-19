package bst.instrumented;

import bst.instrumented.drefnode.DeltaRefConstNode;
import bst.instrumented.drefnode.DeltaRefNode;
import delta.restore.Doable;
import delta.restore.StateRestorer;
import delta.restore.Undoable;
import delta.statemask.StateMask;
import delta.util.Common;
import deltalib.dboolean.DeltaBoolean;
import deltalib.dboolean.DeltaBooleanCommon;
import deltalib.dboolean.DeltaBooleanSparsed;
import deltalib.dint.DeltaInt;
import deltalib.dint.DeltaIntCommon;

/***********************************************************
 *  BinarySearchTree class
 ***********************************************************/
public class BinarySearchTree /*implements AccessibleValues<BinarySearchTree>*/ {

  private DeltaInt size = DeltaIntCommon.createConstant(0);
  private DeltaRefNode root = DeltaRefNode.NULL;

  /****************** no-arg constructor *****************/
  public BinarySearchTree() {}

  @SuppressWarnings("unused")
  private DeltaBoolean exp1(DeltaInt finfo, DeltaInt info) {
    if (true) {
      return DeltaBooleanCommon.createConstant(finfo.lt(info));
    }
    
    boolean isCte1 = finfo.isStrictConstant();
    int cte1 = finfo.getStrictConstant();
    boolean isCte2 = info.isStrictConstant();
    int cte2 = info.getStrictConstant();

    StateMask sm = StateMask.getStateMask();

    boolean noMutation = 
      sm.isSetAt(0) &&
      (isCte1 ? cte1 : finfo.getValueAt(0)) < 
      (isCte2 ? cte2 : info.getValueAt(0));

    return DeltaBooleanSparsed.createForMerger(
        new int[]{0,1,2,3,4,5,6,7,8,9,10},
        new boolean[]{
            noMutation,
            sm.isSetAt(1) && 
            (isCte1 ? cte1 : finfo.getValueAt(1)) <= 
              (isCte2 ? cte2 : info.getValueAt(1)), // only for state 1
              sm.isSetAt(2) && 
              (isCte1 ? cte1 : finfo.getValueAt(2)) == 
                (isCte2 ? cte2 : info.getValueAt(2)), // only for state 2
                sm.isSetAt(3) && 
                (isCte1 ? cte1 : finfo.getValueAt(3)) >= 
                  (isCte2 ? cte2 : info.getValueAt(3)), // and so on...
                  sm.isSetAt(4) && 
                  (isCte1 ? cte1 : finfo.getValueAt(4)) != 
                    (isCte2 ? cte2 : info.getValueAt(4)),
                    sm.isSetAt(5) && 
                    (isCte1 ? cte1 : finfo.getValueAt(5)) > 
                (isCte2 ? cte2 : info.getValueAt(5)),
                noMutation,
                noMutation,
                noMutation,
                noMutation,
                noMutation,
                noMutation
        });     
  }

  @SuppressWarnings("unused")
  private DeltaBoolean exp2(DeltaInt finfo, DeltaInt info) {
    
    if (true) {
      return DeltaBooleanCommon.createConstant(finfo.gt(info));
    }

    
    boolean isCte1 = finfo.isStrictConstant();
    int cte1 = finfo.getStrictConstant();
    boolean isCte2 = info.isStrictConstant();
    int cte2 = info.getStrictConstant();

    StateMask sm = StateMask.getStateMask();

    boolean noMutation = 
      sm.isSetAt(0) && (isCte1 ? cte1 : finfo.getValueAt(0)) >
    (isCte2 ? cte2 : info.getValueAt(0));     

    return DeltaBooleanSparsed.createForMerger(        
        new int[]{0,1,2,3,4,5,6,7,8,9,10},
        new boolean[]{
            noMutation,
            noMutation,
            noMutation,
            noMutation,
            noMutation,
            noMutation,
            noMutation,
            sm.isSetAt(6) && (isCte1 ? cte1 : finfo.getValueAt(6)) <= 
              (isCte2 ? cte2 : info.getValueAt(6)), // only for state 1
              sm.isSetAt(7) && (isCte1 ? cte1 : finfo.getValueAt(7)) == 
                (isCte2 ? cte2 : info.getValueAt(7)), // only for state 2
                sm.isSetAt(8) && (isCte1 ? cte1 : finfo.getValueAt(8)) >= 
                  (isCte2 ? cte2 : info.getValueAt(8)), // and so on...
                  sm.isSetAt(9) && (isCte1 ? cte1 : finfo.getValueAt(9)) != 
                    (isCte2 ? cte2 : info.getValueAt(9)),
                    sm.isSetAt(10) && (isCte1 ? cte1 : finfo.getValueAt(10)) < 
                    (isCte2 ? cte2 : info.getValueAt(10))
        });     
  }

  /*******************************************************/
  public void add(DeltaInt info) {
    if (get_root().isNull()) {
      this.set_root(new DeltaRefConstNode(new Node(info)));
    } else {
      DeltaRefNode temp = get_root();
      while (true) {
        if (exp1(temp.get_info(),info).eval()/*temp.get_info().lt(info)*/) {
          if (temp.get_right().isNull()) {
            temp.set_right(new DeltaRefConstNode(new Node(info)));
            break;
          } else {
            temp = temp.get_right();
          }
        } else if (exp2(temp.get_info(),info).eval()/*temp.get_info().gt(info)*/) {
          if (temp.get_left().isNull()) {
            temp.set_left(new DeltaRefConstNode(new Node(info)));
            break;
          } else {
            temp = temp.get_left();
          }
        } else {
          return;
        }
      }
    }
    this.set_size(this.get_size().add(1));
  }

  /*******************************************************/
  public boolean remove(DeltaInt info) {
    DeltaRefNode parent = DeltaRefNode.NULL;
    DeltaRefNode current = this.get_root();

//  TODO: FIX THIS... Add ne to DeltaRef
//  while (current.neq(DeltaRefNodeCommon.createConstant(null))) {
    while (!(current.isNull())) {
      if (info.lt(current.get_info())) {
        parent = current;
        current = current.get_left();
      } else if (info.gt(current.get_info())) {
        parent = current;
        current = current.get_right();
      } else {
        break;
      }
    }
    if (current.isNull()) return false;

    DeltaRefNode change = removeNode(current);
    if (parent.isNull()) {
      this.set_root(change);
    } else if (parent.get_left().eq(current)) {
      parent.set_left(change);
    } else {
      parent.set_right(change);
    }
    return true;
  }

  /*******************************************************/
  DeltaRefNode removeNode(DeltaRefNode current) {
    this.set_size(this.get_size().sub(1));
    DeltaRefNode left = current.get_left();
    DeltaRefNode right = current.get_right();
    if (left.isNull()) return right;
    if (right.isNull()) return left;
    if (left.get_right().isNull()) {
      current.set_info(left.get_info());
      current.set_left(left.get_left());
      return current;
    }

    DeltaRefNode temp = left;
    while (!(temp.get_right().get_right().isNull())) {
      temp = temp.get_right();
    }
    current.set_info(temp.get_right().get_info());
    temp.set_right(temp.get_right().get_left());
    return current;
  }

  /*******************************************************/
  public String toString() {
    return "size=" + get_size() + " [" + get_root() + "]" ;
  }

  /**************** get methods for this *****************/
  public DeltaRefNode get_root() {
    return root;
  }
  public DeltaInt get_size() {
    return size;
  }

  /**************** set methods for this *****************/
  private int hash1;
  public void set_root(final DeltaRefNode newValue) {
    if (!Common.DOUNDO) {
      root = root.update(newValue);
    } else {
      Doable<Node> _do = new Doable<Node>() {
        DeltaRefNode newv = newValue;
        @Override
        public void doit(StateMask sm) {
          root = root.update(newv);
        }
      };
      Undoable<Node> _undo = new Undoable<Node>() {
        DeltaRefNode old = root;
        @Override
        public void undoit() {
          old.undoConstant();
          root = old;
        }
      };
      if (hash1==0) {
        hash1 = hashCode()+"root".hashCode();
      }
      StateRestorer.add(hash1,_do);
      StateRestorer.add(_undo);
      root = newValue;
    }
    root = newValue;
  }
  
  private int hash2;
  public void set_size(final DeltaInt newValue) {
    if (!Common.DOUNDO) {
      size = size.update(newValue);
    } else {
      Doable<?> _do = new Doable() {
        DeltaInt newv = newValue;
        @Override
        public void doit(StateMask sm) {        
          size = size.update(newv);
        }
      };
      Undoable<?> _undo = new Undoable() {
        DeltaInt old = size;
        @Override
        public void undoit() {
          old.undoConstant();
          size = old;
        }
      };
      if (hash2==0) {
        hash2 = hashCode()+"size".hashCode();
      }
      StateRestorer.add(hash2, _do);
      StateRestorer.add(_undo);
      size = newValue;
    }
    size = newValue;
  }

}