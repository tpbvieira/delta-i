package delta.util.threads;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Util {
  
  private static final int NUM_THREADS = 2;
  
  private static final ThreadPoolExecutor tpExec = 
//    (ThreadPoolExecutor) Executors.newCachedThreadPool();
    (ThreadPoolExecutor) Executors.newFixedThreadPool(NUM_THREADS);

  public static void spawnWorker(Runnable runnable) {
    tpExec.execute(runnable);
  }
  
  public static void shutdown() {
    tpExec.shutdown();
  }
  
}
