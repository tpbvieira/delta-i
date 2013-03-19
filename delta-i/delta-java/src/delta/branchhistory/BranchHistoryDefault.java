package delta.branchhistory;

import java.util.ArrayList;
import java.util.List;

import delta.statemask.StateMask;
import deltalib.dref.DeltaRefCommon;


public class BranchHistoryDefault implements BranchHistory {    

  /**
   * did not use a real stack because we need indexing
   */
  private static List<BranchInfo> infoStack = new ArrayList<BranchInfo>();

  private static int ourCurrent = 0;

  /********************************************************/
  public boolean isBranchHistory() {
    return ourCurrent < infoStack.size();
  }

  /********************************************************/
  public boolean getBranchChoice() {
    return ((BranchInfo)infoStack.get(ourCurrent++)).branchTaken;
  }

  /********************************************************/
  public void pushBranch(boolean choice) {
    infoStack.add(new BranchInfo(choice, true /* done */, null));
    ourCurrent++;
  }

  /********************************************************/
  public void pushBranchAndMask(boolean choice, StateMask mask) {
    infoStack.add(new BranchInfo(!choice, false /* not done */, mask));
    ourCurrent++;
  }

  //TODO: change this name
  public static class BranchInfo {
    
    boolean branchTaken, done;
    public StateMask nextMask;
    //    LinkedList<DeltaRef<?>> constants;
    
    BranchInfo(boolean branchTaken, boolean done, StateMask nextMask) {
      this.branchTaken = branchTaken;
      this.done = done;
      this.nextMask = nextMask;
    }
    
    @SuppressWarnings("static-access")
    public String toString() {
      return nextMask.getStateMask().toString();
    }
    
  }

  public void becameConstant(DeltaRefCommon<?> mr) {
    //    BranchInfo oo = ourStack.get(ourStack.size() - 1);
    //    if (oo.constants == null) {
    //      oo.constants = new LinkedList<DeltaRef<?>>();
    //    }
    //    oo.constants.add(mr);
  }

  /********************************************************/
  public void clearHistory() {
    infoStack = new ArrayList<BranchInfo>(100);
    ourCurrent = 0;
  }

  /********************************************************/
  public void popAndSetHistory() {
    if (infoStack.size() > 0) {
      StateMask.setStateMask(infoStack.get(infoStack.size() - 1).nextMask);
      ourCurrent = 0;
    }
  }

  /**
   * restore the stack up to the first non-visited branch
   */
  public boolean backtrack() {
    boolean result = false;
    
    for (int i = infoStack.size() - 1; i >= 0; i--) {
      
      BranchInfo branchInfo = infoStack.get(i);
      if (branchInfo.done) {
        infoStack.remove(i);
      } else {
        branchInfo.done = true;
        branchInfo.branchTaken = !branchInfo.branchTaken;
        result = true;
        break;
      }
    }
    
    return result;
  }

}