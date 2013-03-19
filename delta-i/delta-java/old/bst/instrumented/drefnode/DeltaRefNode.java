package bst.instrumented.drefnode;

import bst.instrumented.Node;
import deltalib.dint.DeltaInt;
import deltalib.dref.DeltaRef;

public interface DeltaRefNode extends DeltaRef<Node> {

  // --- general operations
  DeltaRefNode NULL = new DeltaRefConstNode(null);

  boolean isNull();

  boolean eq(DeltaRefNode current);
  
  // support merging
  public DeltaRefNode update(DeltaRefNode dNew);
  
  public DeltaRefNode copy();
  
  public void undoConstant();

  // --- getters and setters of Node fields
  DeltaInt get_info();

  DeltaRefNode get_right();

  DeltaRefNode get_left();

  void set_left(DeltaRefNode change);

  void set_right(DeltaRefNode change);
  
  void set_info(DeltaInt get_info);


}
