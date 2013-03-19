package delta.branchhistory;

import delta.statemask.StateMask;
import deltalib.dref.DeltaRefCommon;

public interface BranchHistory {

  // reads branch decisions along common prefix
  public boolean isBranchHistory();
  public boolean getBranchChoice();
  public void pushBranch(boolean choice);
  public void pushBranchAndMask(boolean choice, StateMask mask);
  
  // clear history
  public void clearHistory();
  
  // restore state 
  public boolean backtrack();
  
  // make a delta reference constant
  public void becameConstant(DeltaRefCommon<?> mr);
  public void popAndSetHistory();
  
}