//
// Copyright (C) 2006 United States Government as represented by the
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

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFConfigException;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.jvm.ClassInfo;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.ArrayStoreInstruction;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.bytecode.InvokeInstruction;
import gov.nasa.jpf.util.DynamicObjectArray;

import java.util.logging.Logger;

/**
 * simple combined listener that checks if a thread seems to do idle loops that
 * might starve other threads or JPF. The most classical case is a "busy wait" loop
 * like
 *
 *   for (long l=0; l<1000000; l++);
 *
 * which would give us a pretty long path. Even worse, things like
 *
 *   while (true);
 *
 * would (just like in a normal VM) never terminate in JPF, even though people
 * familiar with model checking would expect state matching. Only that without
 * a transition break, JPF has no reason to match states, so we have to
 * automatically add a break on the backjump. We shouldn't add one on every
 * backjump though because that might cause a lot of overhead in programs that
 * do terminate.
 *
 * IdleFilter has two options:
 *   idle.max_backjumps : sets the number of backjumps after which we break
 *   idle.action : what to do if max_backjumps are exceeded in the same thread
 *                 on the same location and stackframe
 *     warn : only print warning for backjumps exceeding the max_backjumps
 *     break : break the transition to allow state matching
 *     prune : unconditionally prune the search
 *     jump : jump past the backjump (this is dangerous if the loop has side effects)
 */
public class IdleFilter extends PropertyListenerAdapter {

  static Logger log = JPF.getLogger("gov.nasa.jpf.listener.IdleFilter");

  static class ThreadStat {
    String tname;

    int backJumps;

    boolean isCleared = false;

    int loopStartPc;

    int loopEndPc;

    int loopStackDepth;

    ThreadStat(String tname) {
      this.tname = tname;
    }
  }

  static enum Action { JUMP, PRUNE, BREAK, WARN }

  DynamicObjectArray<ThreadStat> threadStats = new DynamicObjectArray<ThreadStat>(4,16);

  ThreadStat ts;

  // we use this to remember that we just broke the transition
  boolean brokeTransition;

  int maxBackJumps;
  Action action;


  // ----------------------------------------------------- SearchListener
  // interface

  public IdleFilter(Config config) {
    maxBackJumps = config.getInt("idle.max_backjumps", 500);

    String act = config.getString("idle.action", "break");
    if ("warn".equalsIgnoreCase(act)){
      action = Action.WARN;
    } else if ("break".equalsIgnoreCase(act)){
      action = Action.BREAK;
    } else if ("prune".equalsIgnoreCase(act)){
      action = Action.PRUNE;
    } else if ("jump".equalsIgnoreCase(act)){
      action = Action.JUMP;
    } else {
      throw new JPFConfigException("unknown IdleFilter action: " +act);
    }

  }

  public void stateAdvanced(Search search) {
    ts.backJumps = 0;
    ts.isCleared = false;
    ts.loopStackDepth = 0;
    ts.loopStartPc = ts.loopEndPc = 0;

    brokeTransition = false;
  }

  public void stateBacktracked(Search search) {
    ts.backJumps = 0;
    ts.isCleared = false;
    ts.loopStackDepth = 0;
    ts.loopStartPc = ts.loopEndPc = 0;
  }

  // ----------------------------------------------------- VMListener interface
  public void instructionExecuted(JVM jvm) {
    Instruction insn = jvm.getLastInstruction();
    ThreadInfo ti = jvm.getLastThreadInfo();

    int tid = ti.getIndex();
    ts = threadStats.get(tid);
    if (ts == null) {
      ts = new ThreadStat(ti.getName());
      threadStats.set(tid, ts);
    }

    if (insn.isBackJump()) {
      ts.backJumps++;

      int loopStackDepth = ti.getStackDepth();
      int loopPc = jvm.getNextInstruction().getPosition();

      if ((loopStackDepth != ts.loopStackDepth) || (loopPc != ts.loopStartPc)) {
        // new loop, reset
        ts.isCleared = false;
        ts.loopStackDepth = loopStackDepth;
        ts.loopStartPc = loopPc;
        ts.loopEndPc = insn.getPosition();
        ts.backJumps = 0;
      } else {
        if (!ts.isCleared) {
          if (ts.backJumps > maxBackJumps) {

            ti.reschedule(false); // this breaks the executePorStep loop
            MethodInfo mi = insn.getMethodInfo();
            ClassInfo ci = mi.getClassInfo();
            int line = mi.getLineNumber(insn);
            String file = ci.getSourceFileName();

            switch (action) {
              case JUMP:
                // pretty bold, we jump past the loop end and go on from there

                Instruction next = insn.getNext();
                ti.setNextPC(next);

                log.warning("jumped past loop in: " + ti.getName() +
                        "\n\tat " + ci.getName() + "." + mi.getName() + "(" + file + ":" + line + ")");
                break;

              case PRUNE:
                // cut this sucker off - we declare this a visited state
                jvm.ignoreState();
                log.warning("pruned thread: " + ti.getName() +
                        "\n\tat " + ci.getName() + "." + mi.getName() + "(" + file + ":" + line + ")");
                break;

              case BREAK:
                // just break the transition and let the state matching take over
                brokeTransition = true;
                ti.breakTransition();

                log.warning("breaks transition on suspicious loop in thread: " + ti.getName() +
                        "\n\tat " + ci.getName() + "." + mi.getName() + "(" + file + ":" + line + ")");

                break;

              case WARN:
                log.warning("detected suspicious loop in thread: " + ti.getName() +
                        "\n\tat " + ci.getName() + "." + mi.getName() + "(" + file + ":" + line + ")");
                break;

            }
          }
        }
      }

    } else if (!ts.isCleared) {
      // if we call methods or set array elements inside the loop in question,
      // we assume this is not an idle loop and terminate the checks
      if ((insn instanceof InvokeInstruction)
          || (insn instanceof ArrayStoreInstruction)) {
        int stackDepth = ti.getStackDepth();
        int pc = insn.getPosition();

        if (stackDepth == ts.loopStackDepth) {
          if ((pc >= ts.loopStartPc) && (pc < ts.loopEndPc)) {
            ts.isCleared = true;
          }
        }
      }
    }
  }

}
