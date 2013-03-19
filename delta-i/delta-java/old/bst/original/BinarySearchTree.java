package bst.original;


/***********************************************************
 *  BinarySearchTree class
 ***********************************************************/
public class BinarySearchTree {

  private Node root; // root node
  private int size;  // number of nodes in the tree


  /*******************************************************/
  public void add(int info) {
    if (root == null) {
      this.root = new Node(info);
    } else {
      Node temp = root;
      while (true) {
        if (temp.info < info) {
          if (temp.right == null) {
            temp.right = new Node(info);
            break;
          } else {
            temp = temp.right;
          }
        } else if (temp.info > info) {
          if (temp.left == null) {
            temp.left = new Node(info);
            break;
          } else {
            temp = temp.left;
          }
        } else {
          return;
        }
      }
    }
    this.size = this.size + 1;
  }

  /*******************************************************/
  public boolean remove(int info) {
    Node parent = null;
    Node current = this.root;

    while (current != null) {
      if (info < current.info) {
        parent = current;
        current = current.left;
      } else if (info > current.info) {
        parent = current;
        current = current.right;
      } else {
        break;
      }
    }
    if (current == null) return false;

    Node change = removeNode(current);
    if (parent == null) {
      this.root = change;
    } else if (parent.left == current) {
      parent.left = change;
    } else {
      parent.right = change;
    }
    return true;
  }

  /*******************************************************/
  Node removeNode(Node current) {
    this.size = this.size - 1;
    Node left = current.left;
    Node right = current.right;
    if (left == null) return right;
    if (right == null) return left;
    if (left.right == null) {
      current.info = left.info;
      current.left = left.left;
      return current;
    }

    Node temp = left;
    while (temp.right.right != null) {
      temp = temp.right;
    }
    current.info = temp.right.info;
    temp.right = temp.right.left;
    return current;
  }

  /*******************************************************/
  public String toString() {
    return "size=" + size + " [" + root + "]" ;
  }

}
