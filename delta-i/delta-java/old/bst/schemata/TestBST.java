package bst.schemata;

import java.util.Random;

public class TestBST {

  public static void main(String[] args) throws Exception {
    if (true) {
      BinarySearchTree bst1 = new BinarySearchTree();
      bst1.add(0);
      bst1.add(-1);
      bst1.add(2);   
      bst1.add(3);
      bst1.add(2);
      bst1.add(1);
    } else {
      BinarySearchTree bst1 = new BinarySearchTree();
      int times = Integer.parseInt(args[0]);
      Random r = new Random(90281);
      for (int i = 0; i < times; i++) {
        bst1.add(r.nextInt());
      }
    }
  }

}