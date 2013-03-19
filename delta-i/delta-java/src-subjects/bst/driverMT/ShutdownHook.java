package bst.driverMT;


public class ShutdownHook implements Runnable {
  
  public long start;
  public int numStates;
  
  public ShutdownHook(long startTime, int num) {
    start = startTime;
    numStates = num;
  }
  
  @Override
  public void run() {
//    System.out.printf("DELTA:\n numExecutions: %d\n numDeltaOps: " +
//        "%d\n timeExecution: %d (millis)\n/", 
//        WorkerQueue.numSpawnedThreads, General.countDeltaOperations, System.currentTimeMillis()-l1);
    System.out.println(System.currentTimeMillis() - start);
    //      long l3 = System.currentTimeMillis();
    //      long deltaCost = l3 - l2;
    //      System.out.printf("DELTA:\n numExecutions: %d\n numDeltaOps: " +
    //          "%d\n timeExecution: %d (millis)\n timeTotal: %d (millis)", 
    //          numExecutions, General.countDeltaOperations, deltaCost, l3 - l1);    
  }

}
