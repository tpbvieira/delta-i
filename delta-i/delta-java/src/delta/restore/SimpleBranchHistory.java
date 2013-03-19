package delta.restore;

import delta.branchhistory.BranchHistory;
import delta.statemask.StateMask;
import deltalib.dref.DeltaRefCommon;

public class SimpleBranchHistory implements BranchHistory {

  @Override
  public boolean backtrack() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void becameConstant(DeltaRefCommon<?> mr) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clearHistory() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean getBranchChoice() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isBranchHistory() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void popAndSetHistory() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void pushBranch(boolean choice) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void pushBranchAndMask(boolean choice, StateMask mask) {
    throw new UnsupportedOperationException();
  }

}
