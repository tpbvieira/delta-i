package bst.instrumented;
import deltalib.General;
import deltalib.dint.DeltaInt;

public class BST {
  
  private Node root; // root node
  private DeltaInt size = General.ZERO;  // number of nodes in the tree
  
  public static boolean modify = true;
  
  /*******************************************************/
  public void add(DeltaInt info) {
    if (root == null) {
      this.root = new Node(info);
    } else {
      Node temp = root;
      while (true) {
        if (General.split(General.lt(temp.info,info))) {
          if (temp.right == null) {
            if (modify) temp.right = new Node(info);
            break;
          } else {
            temp = temp.right;
          }
        } else if (General.split(General.gt(temp.info,info))) {
          if (temp.left == null) {
            if (modify) temp.left = new Node(info);
            break;
          } else {
            temp = temp.left;
          }
        } else {
          return;
        }
      }
    }
    if (modify) size = General.add(size,General.constant(1));    
  }
}