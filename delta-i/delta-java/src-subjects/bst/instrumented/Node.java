package bst.instrumented;
import deltalib.dint.DeltaInt;

public class Node {
  

  public Node left;   // left child
  public Node right;  // right child
  public DeltaInt info;    // data

  Node(DeltaInt info) {
    this.info = info;
  }
  
  Node(Node left, Node right, DeltaInt info) {
    this.left = left;
    this.right = right;
    this.info = info;
  }

}
