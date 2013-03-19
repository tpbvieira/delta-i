class BST {
  Node root; int size;
  static class Node {
    int info; Node left, right;
    Node(int info) {
      this.info = info;
    }
  }
  void add(int info) {
    if (root == null) {
      root = new Node(info); 
    } else { Node temp = root;
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
        } else { return; } 
      }
    }
    size++;
  }
}