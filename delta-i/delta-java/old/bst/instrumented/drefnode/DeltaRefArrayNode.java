package bst.instrumented.drefnode;

import java.util.Arrays;

import bst.instrumented.Node;

import delta.statemask.StateMask;
import delta.statemask.StateMaskIterator;
import delta.util.Common;
import deltalib.dint.DeltaInt;
import deltalib.dref.DeltaRefArray;
import deltalib.dref.DeltaRefCommon;
import deltalib.dref.DeltaRefConst;

public class DeltaRefArrayNode extends DeltaRefArray<Node> 
implements DeltaRefNode {

  DeltaRefArrayNode(Node[] args) {
    super(args);
  }
  
  @Override
  public DeltaRefNode copy() {
    return new DeltaRefArrayNode(values.clone());
  }
 
  public DeltaRefArrayNode(Node strictConstant) {
    this(new Node[Common.NUM_STATES]);
    Arrays.fill(this.values, strictConstant);
  }
  
  //-- update
  
  public DeltaRefNode update(DeltaRefNode dNew) {
    /**
     * mutate state
     **/
    StateMask sm = StateMask.getStateMask();
    if (sm.numberOfEnabledStates() == sm.size()) {
      return dNew;
    }
    
    if (isStrictConstant()) {
      System.err.println("check this out!");
      undoConstant();
    }

    DeltaRefNode result = this;
    
    boolean dNewIsCte = dNew instanceof DeltaRefConst;
    Node cte = dNewIsCte ? dNew.getStrictConstant() : null /* never used */;
    // update
    StateMaskIterator smit = sm.intIterator();
    while (smit.hasNext()) {
      int stateid = smit.next();
      Node val = dNewIsCte ? cte : dNew.getValueAt(stateid);      
      result.setValueAt(stateid,val);
    }
    return result;    
  }

  //-- ref operations
  @SuppressWarnings("unchecked")
  @Override
  public boolean eq(DeltaRefNode current) {
    return eq((DeltaRefCommon<Node>)current);
  }

  @Override
  public DeltaInt get_info() {
    return Helper.get_info(this);
  }

  @Override
  public DeltaRefNode get_left() {
    return Helper.get_left(this);
  }

  @Override
  public DeltaRefNode get_right() {
    return Helper.get_right(this);
  }

  @Override
  public void set_info(DeltaInt info) {
    Helper.set_info(this, info);
  }

  @Override
  public void set_left(DeltaRefNode left) {
    Helper.set_left(this, left);
  }

  @Override
  public void set_right(DeltaRefNode right) {
    Helper.set_right(this, right);
  }
  
}