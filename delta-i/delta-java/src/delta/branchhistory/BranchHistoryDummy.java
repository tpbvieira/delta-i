package delta.branchhistory;

import delta.statemask.StateMask;
import delta.util.ArrayStack;
import deltalib.dref.DeltaRefCommon;

public class BranchHistoryDummy implements BranchHistory {

  static class Info {
    StateMask next;
    
    public Info(StateMask next) {
      super();
      this.next = next;
    }
    
  }

  private ArrayStack<Info> choiceStack;

  public void clearHistory() {
    choiceStack = new ArrayStack<Info>();
  }

  public boolean backtrack() {
    boolean result = true;
    
    try {
      Info info = choiceStack.pop();
      StateMask.setStateMask(info.next);
    } catch (ArrayStack.EmptyStack _) {
      result = false;
    }
    return result;
  }
  
  //--------------------------------
  @Override
  public boolean getBranchChoice() {
    throw new UnsupportedOperationException();
  }
  @Override
  public boolean isBranchHistory() {
    return false;
  }
  //--------------------------------


  @Override
  public void becameConstant(DeltaRefCommon<?> mr) {}

  @Override
  public void popAndSetHistory() {}

  @Override
  public void pushBranch(boolean choice) {
    //throw new UnsupportedOperationException();
  }

  @Override
  public void pushBranchAndMask(boolean choice, StateMask nextMask) {
    //    throw new UnsupportedOperationException();
    choiceStack.push(new Info(nextMask));
  }

}