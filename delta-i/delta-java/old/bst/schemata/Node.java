package bst.schemata;


/*******************************************************
 *  Node class (for use with BinarySearchTree)
 *******************************************************/
public class Node {

  public Node left;   // left child
  public Node right;  // right child
  public int info;    // data

  Node(int info) {
    this.info = info;
  }
  Node(Node left, Node right, int info) {
    this.left = left;
    this.right = right;
    this.info = info;
  }


  /***************************************************/
  public String toString() {
    return "info=" + info + ", [" + left + "], [" + right + "]";
  }

}

