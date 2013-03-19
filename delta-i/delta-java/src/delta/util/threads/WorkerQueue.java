//package delta.util.threads;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class WorkerQueue {
//  
//  public static int numSpawnedThreads = 1; 
//  
//  private static List<Runnable> workerQueue = new ArrayList<Runnable>();
//  
//  public static void add(Runnable run) {
//    //@TODO please, try to use thread pools from java API
//    (new Thread(run)).start();
//    // workerQueue.add(run);
//    synchronized (workerQueue) {
//      numSpawnedThreads++;
//    }
//  }
//  
//  public static Runnable getOne() {
//    return workerQueue.remove(0);
//  }
//  
//  
//}
