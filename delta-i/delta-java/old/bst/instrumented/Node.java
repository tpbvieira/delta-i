package bst.instrumented;

import bst.instrumented.drefnode.DeltaRefNode;
import deltalib.dint.DeltaInt;
import deltalib.dint.DeltaIntCommon;

/*******************************************************
 *  Node class (for use with BinarySearchTree)
 *******************************************************/
public class Node /*implements AccessibleValues<Node>*/ {

  public DeltaRefNode left = DeltaRefNode.NULL;
  public DeltaRefNode right = DeltaRefNode.NULL;
  public DeltaInt info = DeltaIntCommon.createConstant(0);

  Node (DeltaInt info) {
    this.set_info(info);
  }

  Node (DeltaRefNode left, DeltaRefNode right, DeltaInt info) {
    this.set_left(left);
    this.set_right(right);
    this.set_info(info);
  }

  /****************** no-arg constructor *****************/
  public Node() {}

  /**************** get methods for this *****************/
  public DeltaRefNode get_left() {
    return left;
  }
  public DeltaRefNode get_right() {
    return right;
  }
  public DeltaInt get_info() {
    return info;
  }

  /**************** set methods for this *****************/
//  int hash1;
  public void set_left(final DeltaRefNode newValue) {
//    if (!Common.DOUNDO) {
//      left = Updator.update(Types.Node, left, newValue);
//    } else {
//      Doable<Node> _do = new Doable<Node>() {
//        DeltaRefNode newv = newValue;
//        @Override
//        public void doit(StateMask sm) {        
//          left = Updator.update(sm, Types.Node, left/*old*/, newv);
//        }
//      };
//      Undoable<Node> _undo = new Undoable<Node>() {
//        DeltaRefNode old = left;
//        @Override
//        public void undoit() {
//          old.undoConstant();
//          left = old;
//        }
//      };
//      if (hash1==0) {
//        hash1 = hashCode()+"left".hashCode();
//      }
//      StateRestorer.add(hash1, _do);
//      StateRestorer.add(_undo);
//      left = newValue;
//    }
    left = newValue;
  }

//  int hash2;
  public void set_right(final DeltaRefNode newValue) {
//    if (!Common.DOUNDO) {
//      right = Updator.update(Types.Node, right, newValue);
//    } else {
//      Doable<Node> _do = new Doable<Node>() {
//        DeltaRefNode newv = newValue;
//        @Override
//        public void doit(StateMask sm) {        
//          right = Updator.update(sm, Types.Node, right/*old*/, newv);
//        }
//      };
//      Undoable<Node> _undo = new Undoable<Node>() {
//        DeltaRefNode old = right;
//        @Override
//        public void undoit() {
//          old.undoConstant();
//          right = old;
//        }
//      };
//      if (hash2==0) {
//        hash2 = hashCode()+"right".hashCode();
//      }
//      StateRestorer.add(hash2, _do);
//      StateRestorer.add(_undo);
//      right = newValue;
//    }
    right = newValue;
  }

//  int hash3;
  public void set_info(final DeltaInt newValue) {
//    if (!Common.DOUNDO) {
//      info = Updator.update(info, newValue);
//    } else {
//      Doable<?> _do = new Doable() {
//        DeltaInt newv = newValue;
//        @Override
//        public void doit(StateMask sm) {        
//          info = Updator.update(sm, info/*old*/, newv);
//        }
//      };
//      Undoable<?> _undo = new Undoable() {
//        DeltaInt old = info;
//        @Override
//        public void undoit() {
//          old.undoConstant();
//          info = old;
//        }
//      };
//      if (hash3==0) {
//        hash3 = hashCode()+"info".hashCode();
//      }
//      StateRestorer.add(hash3, _do);
//      StateRestorer.add(_undo);
//      info = newValue;
//    }
    info = newValue;
  }

}