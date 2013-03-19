package bst.instrumented.drefnode;

import bst.instrumented.Node;
import delta.statemask.StateMask;
import delta.statemask.StateMaskIterator;
import deltalib.dint.DeltaInt;
import deltalib.dref.DeltaRefCommon;
import deltalib.dref.DeltaRefConst;

public class DeltaRefConstNode extends DeltaRefConst<Node> 
  implements DeltaRefNode {

  public DeltaRefConstNode(Node object) {
    super(object);
  }
  
  @Override
  public DeltaRefNode copy() {
    return new DeltaRefConstNode(constant);
  }
  
  //-- update
  
  @SuppressWarnings("unchecked")
  public DeltaRefNode update(DeltaRefNode dNew) {
    StateMask sm = StateMask.getStateMask();
    if (sm.numberOfEnabledStates() == sm.size()) {
      return dNew;
    }
    // expansion
    DeltaRefNode result = new DeltaRefArrayNode(getStrictConstant());    
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