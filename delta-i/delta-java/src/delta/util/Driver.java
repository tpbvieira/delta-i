package delta.util;

import delta.branchhistory.BranchHistory;
import delta.restore.StateRestorer;
import delta.statemask.StateMask;

public class Driver {
  
  public static interface Runner {
    public void run();
  }
  
  @SuppressWarnings("unused")
  public void startup() {
    StateMask sm = 
      new StateMask(StateMask.KINDS.CONTIGUOUS_REPRESENTATION, Common.NUM_STATES);
    StateMask.setStateMask(sm);    
  }

  public void step(Runner runner) {
    StateMask.getStateMask().setAll();
    BranchHistory bh = Common.bh;
    bh.clearHistory(); // clear the branch history stack
    int numExecutions = 0;
    StateRestorer.clear();
    assert(StateRestorer.undoStack.size()==0);
    assert(StateRestorer.doMap.size()==0);
    assert(StateRestorer.redo.size()==0);
    do {
      if (numExecutions > 0) {        
        bh.popAndSetHistory();
      }
//      System.out.println(StateMask.getStateMask());
      runner.run();
      if (Common.DOUNDO) {
        StateRestorer.restoreInitialAndSaveFinals();
      }
      numExecutions++;
//      System.out.println(StateMask.getStateMask());
    } while (bh.backtrack());
    if (Common.DOUNDO) {
      StateRestorer.mergeChanges();
    }
  }
  
}