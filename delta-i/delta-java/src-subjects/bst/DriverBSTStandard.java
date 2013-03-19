package bst;
import java.util.Random;

import deltalib.General;

public class DriverBSTStandard {

  private static int hi = 9999999;
  private static int SEED = 5029385;

  private static int NUM_NODES = 100;
  private static int NUM_INPUTS = 0;

  public static void main(String[] args) {
    General.C = false;
    NUM_NODES = Integer.valueOf(args[0]).intValue();
    NUM_INPUTS = Integer.valueOf(args[1]).intValue();

    // building initial state
    Random r = new Random(SEED);
    int[] randomNumbers = new int[NUM_NODES];
    for (int i = 1; i < NUM_NODES; i++) {
      randomNumbers[i] = r.nextInt(hi);
    }

    // building standard BST object
    bst.original.BST bstStd = new bst.original.BST();
    for (int i = 1; i < NUM_NODES; i++) {
      bstStd.add(randomNumbers[i]);
    }

    // building long delta input
    int[] input = new int[NUM_INPUTS];
    for (int i = 1; i < NUM_INPUTS; i++) {
      input[i] = (r.nextInt(hi));
    }

    // standard tree
    long l2 = System.nanoTime();
    for (int i = 0; i < input.length; i++) {
      bstStd.add(input[i]);
    }
    long l3 = System.nanoTime();

    System.out.println(NUM_NODES + "\t" + NUM_INPUTS + "\t" + (l3-l2));
  }
}