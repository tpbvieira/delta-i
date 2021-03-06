//
// Copyright (C) 2007 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA-1.3-JPF at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package gov.nasa.jpf.listener;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.ListIterator;
import java.util.Stack;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.EXECUTENATIVE;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.report.ConsolePublisher;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.search.Search;

/**
 * example of a listener that creates property specific traces. The interesting
 * thing is that it does so without the need to store steps, i.e. it maintains
 * it's own transition stack.
 * this is still work in progress, analyzing the trace can be much more
 * elaborate (we just dump up to a max history size for now)
 */
public class DeadlockAnalyzer extends ListenerAdapter {

  enum OpType { block, lock, unlock, wait, notify, notifyAll, started, terminated };
  static String[] opTypeMnemonic = { "B", "L", "U", "W", "N", "A", "S", "T" };
  
  static class ThreadOp {  
    ThreadInfo ti;
    ElementInfo ei;
    Instruction insn;
    
    // kind of redundant, but there might be context attributes in addition
    // to the insn itself
    OpType opType;
    
    // we could identify this with the insn, but only in case this is
    // a transition boundary, which is far less general than we can be
    int stateId;
    ThreadOp prevTransition;
    ThreadOp prevOp;

    ThreadOp (JVM vm, OpType type) {
      ti = vm.getLastThreadInfo();
      ei = vm.getLastElementInfo();
      insn = getReportInsn(ti); // we haven't had the executeInsn notification yet
      opType = type;
      
      prevOp = null;
    }

    Instruction getReportInsn(ThreadInfo ti){
      StackFrame frame = ti.getTopFrame();
      if (frame != null) {
        Instruction insn = frame.getPC();
        if (insn instanceof EXECUTENATIVE) {
          frame = frame.getPrevious();
          if (frame != null) {
            insn = frame.getPC();
          }
        }

        return insn;
      } else {
        return null;
      }
    }

    void printLocOn (PrintWriter pw) {
      pw.print(String.format("%6d", new Integer(stateId)));
      
      if (insn != null) {
        pw.print(String.format(" %18.18s ", insn.getMnemonic()));
        pw.print(insn.getFileLocation());
        String line = insn.getSourceLine();
        if (line != null){
          pw.print( " : ");
          pw.print(line.trim());
          //pw.print(insn);
        }
      }
    }
    
    void printOn (PrintWriter pw){
      pw.print( stateId);
      pw.print( " : ");
      pw.print( ti.getName());
      pw.print( " : ");
      pw.print( opType.name());
      pw.print( " : ");
      pw.println(ei);
    }
    
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append( stateId);
      sb.append( " : ");
      sb.append( ti.getName());
      sb.append( " : ");
      sb.append( opType.name());
      sb.append( " : ");
      sb.append(ei);
      return sb.toString();
    }
    
    void printColumnOn(PrintWriter pw, Collection<ThreadInfo> tlist){
      for (ThreadInfo t : tlist) {
        if (ti == t) {
          if (opType == OpType.started || opType == OpType.terminated) {
            pw.print(String.format("   %1$s    ", opTypeMnemonic[opType.ordinal()]));
          } else {
            pw.print(String.format("%1$s:%2$-5d ", opTypeMnemonic[opType.ordinal()], ei.getIndex()));
          }
          //break;
        } else {
          pw.print("   |    ");
        }
      }      
    }
  }
  
  ThreadOp lastOp;
  ThreadOp lastTransition;
  
  int maxHistory;
  String format;
  
  JVM vm;
  Search search;
  
  public DeadlockAnalyzer (Config config, JPF jpf){
    jpf.addPublisherExtension(ConsolePublisher.class, this);
    
    maxHistory = config.getInt("deadlock.max_history", Integer.MAX_VALUE);
    format = config.getString("deadlock.format", "essential");
    
    vm = jpf.getVM();
    search = jpf.getSearch();
  }
  
  boolean requireAllOps() {
    return (format.equalsIgnoreCase("essential"));
  }
  
  void addOp (JVM vm, OpType opType){
    ThreadOp op = new ThreadOp(vm, opType);
    if (lastOp == null){
      lastOp = op;
    } else {
      assert lastOp.stateId == 0;
      
      op.prevOp = lastOp;
      lastOp = op;
    }
  }
  
  void printRawOps (PrintWriter pw) {
    int i=0;
    
    for (ThreadOp tOp = lastTransition; tOp != null; tOp = tOp.prevTransition){
      for (ThreadOp op = tOp; op != null; op=op.prevOp) {
        if (i++ >= maxHistory){
          pw.println("...");
          return;
        }
        op.printOn(pw);
      }
    }
  }
      
  /**
   * include all threads that are currently blocked or waiting, and
   * all the threads that had the last interaction with them. Note that
   * we do this completely on the basis of the recorded ThreadOps, i.e.
   * don't rely on when this is called
   */
  void printEssentialOps(PrintWriter pw) {
    LinkedHashSet<ThreadInfo> threads = new LinkedHashSet<ThreadInfo>();
    ArrayList<ThreadOp> ops = new ArrayList<ThreadOp>();
    HashMap<ElementInfo,ThreadInfo> waits = new HashMap<ElementInfo,ThreadInfo>();
    HashMap<ElementInfo,ThreadInfo> blocks = new HashMap<ElementInfo,ThreadInfo>();
    HashSet<ThreadInfo> runnables = new HashSet<ThreadInfo>();
    
    // collect all relevant threads and ops
    for (ThreadOp trans = lastTransition; trans != null; trans = trans.prevTransition){
      for (ThreadOp tOp = trans; tOp != null; tOp = tOp.prevOp) {
        OpType ot = tOp.opType;
        ThreadInfo oti = tOp.ti;
        
        if (ot == OpType.wait || ot == OpType.block) {
          if (!runnables.contains(oti) && !threads.contains(oti)){
            HashMap<ElementInfo, ThreadInfo> map = (ot == OpType.block) ? blocks : waits;
            threads.add(oti);
            map.put(tOp.ei, oti);
            ops.add(tOp);
          }
          
        } else if (ot == OpType.notify || ot == OpType.notifyAll || ot == OpType.lock) {
          HashMap<ElementInfo, ThreadInfo> map = (ot == OpType.lock) ? blocks : waits;
          ThreadInfo ti = map.get(tOp.ei);
          
          if (ti != null && ti != oti){
            if (!threads.contains(oti)){
              threads.add(oti);
            }
            map.remove(tOp.ei);
            ops.add(tOp);
          }
          
          runnables.add(oti);

        } else if (ot == OpType.unlock) {
          // not relevant
          runnables.add(oti);
          
        } else if (ot == OpType.terminated || ot == OpType.started) {
          ops.add(tOp); // might be removed later-on
        }
      }
    }

    // remove all starts/terminates of irrelevant threads
    for (ListIterator<ThreadOp> it = ops.listIterator(); it.hasNext(); ){
      ThreadOp tOp = it.next();
      if (tOp.opType == OpType.terminated || tOp.opType == OpType.started) {
        if (!threads.contains(tOp.ti)){
          it.remove();
        }
      }
    }
    
    // now we are ready to print
    printHeader(pw,threads);

    for (ThreadOp tOp : ops) {
      tOp.printColumnOn(pw,threads);
      tOp.printLocOn(pw);
      pw.println();          
    }
  }
    
  
  Collection<ThreadInfo> getThreadList() {
    ArrayList<ThreadInfo> tcol = new ArrayList<ThreadInfo>();
    boolean allOps = requireAllOps();
    int i=0;
    
    prevTrans:
    for (ThreadOp tOp = lastTransition; tOp != null; tOp = tOp.prevTransition){
      i++;
      if (!allOps && (i >= maxHistory)){
        break;
      }
      
      for (ThreadInfo ti : tcol) {
        if (ti == tOp.ti) continue prevTrans;
      }
      tcol.add(tOp.ti);
    }
    
    return tcol;
  }
  
  void printHeader (PrintWriter pw, Collection<ThreadInfo> tlist){
    for (ThreadInfo ti : tlist){
      pw.print(String.format("  %1$2d    ", ti.getIndex()));
    }
    pw.print(" trans      insn          loc                : stmt");
    pw.println();
        
    for (int i=0; i<tlist.size(); i++){
      pw.print("------- ");
    }
    pw.print("---------------------------------------------------");
    pw.println();
  }

  
  void printColumnOps (PrintWriter pw){
    int i = 0;
    Collection<ThreadInfo> tlist = getThreadList();
    printHeader(pw,tlist);
    
    // and now the data
    for (ThreadOp tOp = lastTransition; tOp != null; tOp = tOp.prevTransition){
      for (ThreadOp op = tOp; op != null; op=op.prevOp) {
        if (i++ >= maxHistory){
          pw.println("...");
          return;
        }
        
        op.printColumnOn(pw,tlist);
        op.printLocOn(pw);
        pw.println();
      }
    }
  }
    
  /**
   * this is the workhorse - filter which ops should be shown, and which
   * are irrelevant for the deadlock
   */
  boolean showOp (ThreadOp op, ThreadInfo[] tlist,
                  boolean[] waitSeen, boolean[] notifySeen,
                  boolean[] blockSeen, boolean[] lockSeen,
                  Stack<ElementInfo>[] unlocked) {
    ThreadInfo ti = op.ti;
    ElementInfo ei = op.ei;
    int idx;
    for (idx=0; idx < tlist.length; idx++){
      if (tlist[idx] == ti) break;
    }
    
    // we could delegate this to the enum type, but let's not be too fancy
    switch (op.opType) {
    case block:
      // only report the last one if thread is blocked
      if (ti.isBlocked()) {
        if (!blockSeen[idx]) {
          blockSeen[idx] = true;
          return true;
        }        
      }
      return false;
    
    case unlock:
      unlocked[idx].push(ei);
      return false;
      
    case lock:
      // if we had a corresponding unlock, ignore
      if (!unlocked[idx].isEmpty() && (unlocked[idx].peek() == ei)) {
        unlocked[idx].pop();
        return false;
      }
      
      // only report the last one if there is a thread that's currently blocked on it
      for (int i=0; i<tlist.length; i++){
        if ((i != idx) && tlist[i].isBlocked() && (tlist[i].getLockObject() == ei)) {
          if (!lockSeen[i]){
            lockSeen[i] = true;
            return true;
          }
        }
      }
      
      return false;
      
    case wait:
      if (ti.isWaiting()){ // only show the last one if this is a waiting thread
        if (!waitSeen[idx]) {
          waitSeen[idx] = true;
          return true;
        }
      }
      
      return false;
      
    case notify:
    case notifyAll:
      // only report the last one if there's a thread waiting on it
      for (int i=0; i<tlist.length; i++){
        if ((i != idx) && tlist[i].isWaiting() && (tlist[i].getLockObject() == ei)) {
          if (!notifySeen[i]) {
            notifySeen[i] = true;
            return true;
          }
        }
      }

      return false;
      
    case started:
    case terminated:
      return true;
    }
    
    return false;
  }

  void storeLastTransition(){
    if (lastOp != null) {
      int stateId = search.getStateId();
      ThreadInfo ti = lastOp.ti;

      for (ThreadOp op = lastOp; op != null; op = op.prevOp) {
        assert op.stateId == 0;

        op.stateId = stateId;
      }

      lastOp.prevTransition = lastTransition;
      lastTransition = lastOp;

      lastOp = null;
    }
  }

  //--- VM listener interface
  
  public void objectLocked (JVM vm) {
    addOp(vm,OpType.lock);
  }

  public void objectUnlocked (JVM vm) {
    addOp(vm,OpType.unlock);
  }

  public void objectWait (JVM vm) {
    addOp(vm,OpType.wait);
  }

  public void objectNotify (JVM vm) {
    addOp(vm,OpType.notify);
  }

  public void objectNotifyAll (JVM vm) {
    addOp(vm,OpType.notifyAll);
  }

  public void threadBlocked (JVM vm){
    addOp(vm,OpType.block);
  }
  
  public void threadStarted (JVM vm){
    addOp(vm,OpType.started);    
  }
  
  public void threadTerminated (JVM vm){
    addOp(vm,OpType.terminated);
  }
  
  //--- SearchListener interface

  public void stateAdvanced (Search search){
    if (search.isNewState()) {
      storeLastTransition();
    }
  }

  public void stateBacktracked (Search search){
    int stateId = search.getStateId();
    while ((lastTransition != null) && (lastTransition.stateId > stateId)){
      lastTransition = lastTransition.prevTransition;
    }
    lastOp = null;
  }
  
  // for HeuristicSearches. Ok, that's braindead but at least no need for cloning
  HashMap<Integer,ThreadOp> storedTransition = new HashMap<Integer,ThreadOp>();
  
  public void stateStored (Search search) {
    // always called after stateAdvanced
    storedTransition.put(search.getStateId(), lastTransition);
  }
  
  public void stateRestored (Search search) {
    int stateId = search.getStateId();
    ThreadOp op = storedTransition.get(stateId);
    if (op != null) {
      lastTransition = op;
      storedTransition.remove(stateId);  // not strictly required, but we don't come back
    }
  }
  
  
  public void publishPropertyViolation (Publisher publisher) {
    PrintWriter pw = publisher.getOut();
    publisher.publishTopicStart("thread ops " + publisher.getLastErrorId());
    
    if ("column".equalsIgnoreCase(format)){
      printColumnOps(pw);
    } else if ("essential".equalsIgnoreCase(format)) {
      printEssentialOps(pw);
    } else {
      printRawOps(pw);
    }
  }
}
