package delta.util;

import delta.branchhistory.BranchHistory;
import delta.branchhistory.BranchHistoryDefault;
import delta.statemask.StateMask.KINDS;

public class Common {
  
  @SuppressWarnings("unchecked")
  public static boolean DOUNDO = true;

  public static KINDS getDeltaStateMaskRepresentation() {
    return KINDS.SHARED_SPARSE_REPRESENTATION;
  }
  
  public static enum DELTA_STATE_REPRESENTATION {DENSE_ARRAY, SPARSE};

  public static DELTA_STATE_REPRESENTATION getDeltaStateRepresentation() {
    return DELTA_STATE_REPRESENTATION.DENSE_ARRAY;
  }
  
  public static boolean isPooledAllocation() {
    return false;
  }
  public static boolean isExecution() {
    return false;
  }
  
  public final static BranchHistory bh = new BranchHistoryDefault();
//  public final static BranchHistory bh = new BranchHistoryDummy();
  
  public static int NUM_STATES = 11;
  public static int NUM_MUTATIONS = NUM_STATES - 1; 
  /** one is for the original program */
  
  private static long SLEEP_TIME = 0;
  public static boolean SLOW1 = false;
  public static boolean SLOW2 = false;
  public static boolean SLOW3 = false;
  public static boolean SLOW4 = false;
  public static boolean SLOW5 = false;
  public static boolean SLOW6 = false;
  public static boolean SLOW7 = false;  
  @SuppressWarnings("static-access")
  public static void slowdown() {
    try { // added to simulate longer test executions
      Thread.currentThread().sleep(SLEEP_TIME);
    } catch (Exception _) {}
  }
  public static void setSLEEP_TIME(int sleepTime) {
    SLEEP_TIME = sleepTime;
  }
  public static void setSLOW(int slow) {
    switch (slow) {
    case 1:
      Common.SLOW1 = true;
      break;
    case 2:
      Common.SLOW2 = true;
      break;
    case 3:
      Common.SLOW3 = true;
      break;
    case 4:
      Common.SLOW4 = true;
      break;
    case 5:
      Common.SLOW5 = true;
      break;
    case 6:
      Common.SLOW6 = true;
      break;
    case 7:
      Common.SLOW7 = true;
      break;
    default:
      break;
    }
  }

  public static void setNumStates(int i) {
    NUM_STATES = i;
    NUM_MUTATIONS = NUM_STATES - 1;
  }
    
}