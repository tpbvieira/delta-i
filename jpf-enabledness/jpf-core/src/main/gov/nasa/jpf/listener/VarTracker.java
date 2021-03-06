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
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.jvm.ThreadInfo;
import java.util.ArrayList;
import gov.nasa.jpf.jvm.bytecode.FieldInstruction;
import gov.nasa.jpf.jvm.ElementInfo;
import gov.nasa.jpf.jvm.MethodInfo;
import gov.nasa.jpf.jvm.bytecode.VariableAccessor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import gov.nasa.jpf.jvm.bytecode.StoreInstruction;
import gov.nasa.jpf.jvm.bytecode.ArrayStoreInstruction;
import gov.nasa.jpf.jvm.bytecode.GETFIELD;
import gov.nasa.jpf.jvm.bytecode.GETSTATIC;
import gov.nasa.jpf.jvm.bytecode.ALOAD;
import gov.nasa.jpf.report.ConsolePublisher;
import gov.nasa.jpf.report.Publisher;
import gov.nasa.jpf.util.MethodSpec;
import gov.nasa.jpf.util.StringSetMatcher;
import java.io.PrintWriter;


/**
 * simple listener tool to find out which variables (locals and fields) are
 * changed how often and from where. This should give a good idea if a state
 * space blows up because of some counter/timer vars, and where to apply the
 * necessary abstractions to close/shrink it
 *
 */
public class VarTracker extends ListenerAdapter {

  // our matchers to determine which variables we have to report
  StringSetMatcher includeVars = null; //  means all
  StringSetMatcher excludeVars = null; //  means none

  // filter methods from which access happens
  MethodSpec methodSpec;

  int maxVars; // maximum number of variables shown
  
  ArrayList<VarChange> queue = new ArrayList<VarChange>();
  ThreadInfo lastThread;
  HashMap<String, VarStat> stat = new HashMap<String, VarStat>();
  int nStates = 0;
  int maxDepth;


  public VarTracker (Config config, JPF jpf){

    includeVars = StringSetMatcher.getNonEmpty(config.getStringArray("vt.include"));
    excludeVars = StringSetMatcher.getNonEmpty(config.getStringArray("vt.exclude",
            new String[] {"java.*", "javax.*"} ));

    maxVars = config.getInt("vt.max_vars", 25);

    methodSpec = MethodSpec.createMethodSpec(config.getString("vt.methods", "!java.*.*"));

    jpf.addPublisherExtension(ConsolePublisher.class, this);
  }

  public void publishPropertyViolation (Publisher publisher) {
    PrintWriter pw = publisher.getOut();
    publisher.publishTopicStart("field access ");

    report(pw);
  }

  void print (PrintWriter pw, int n, int length) {
    String s = Integer.toString(n);
    int l = length - s.length();
    
    for (int i=0; i<l; i++) {
      pw.print(' ');
    }
    
    pw.print(s);
  }
  
  void report (PrintWriter pw) {
    pw.println();
    pw.println("      change    variable");
    pw.println("---------------------------------------");
    
    Collection<VarStat> values = stat.values();
    List<VarStat> valueList = new ArrayList<VarStat>();
    valueList.addAll(values);
    Collections.sort(valueList);

    int n = 0;
    for (VarStat s : valueList) {
      
      if (n++ > maxVars) {
        break;
      }
      
      print(pw, s.nChanges, 12);
      pw.print("    ");
      pw.println(s.id);
    }
  }
  
  public void stateAdvanced(Search search) {
    
    if (search.isNewState()) { // don't count twice
      int stateId = search.getStateId();
      nStates++;
      int depth = search.getDepth();
      if (depth > maxDepth) maxDepth = depth;
      
      if (!queue.isEmpty()) {
        for (Iterator<VarChange> it = queue.iterator(); it.hasNext(); ){
          VarChange change = it.next();
            String id = change.getVariableId();
            VarStat s = stat.get(id);
            if (s == null) {
              s = new VarStat(id, stateId);
              stat.put(id, s);
            } else {
              // no good - we should filter during reg (think of large vectors or loop indices)
              if (s.lastState != stateId) { // count only once per new state
                s.nChanges++;
                s.lastState = stateId;
              }
            }
        }
      }
    }

    queue.clear();
  }
  
  
  // <2do> - general purpose listeners should not use types such as String for storing
  // attributes, there is no good way to make sure you retrieve your own attributes
      
  public void instructionExecuted(JVM jvm) {
    Instruction insn = jvm.getLastInstruction();
    ThreadInfo ti = jvm.getLastThreadInfo();
    String varId;
    
    if ( ((((insn instanceof GETFIELD) || (insn instanceof GETSTATIC)))
            && ((FieldInstruction)insn).isReferenceField()) ||
         (insn instanceof ALOAD)) {
      // a little extra work - we need to keep track of variable names, because
      // we cannot easily retrieve them in a subsequent xASTORE, which follows
      // a pattern like:  ..GETFIELD.. some-stack-operations .. xASTORE
      int objRef = ti.peek();
      if (objRef != -1) {
        ElementInfo ei = jvm.getHeap().get(objRef);
        if (ei.isArray()) {
          varId = ((VariableAccessor)insn).getVariableId();
          
          // <2do> unfortunately, we can't filter here because we don't know yet
          // how the array ref will be used (we would only need the attr for
          // subsequent xASTOREs)
          ti.addOperandAttr( varId);
        }
      }
    }
    // here come the changes - note that we can't update the stats right away,
    // because we don't know yet if the state this leads into has already been
    // visited, and we want to detect only var changes that lead to *new* states
    // (objective is to find out why we have new states)
    else if (insn instanceof StoreInstruction) {
      if (insn instanceof ArrayStoreInstruction) {
        // did we have a name for the array?
        // stack is ".. ref idx [l]value => .."
        // <2do> String is not a good attribute type to retrieve
        String attr = ti.getOperandAttr(1, String.class);
        if (attr != null) {
          varId = attr + "[]";
        } else {
          varId = "?[]";
        }
      } else {
        varId = ((VariableAccessor)insn).getVariableId();
      }
      
      if (isMethodRelevant(insn.getMethodInfo()) && isVarRelevant(varId)) {
        queue.add(new VarChange(varId));
        lastThread = ti;
      }
    }
  }

  boolean isMethodRelevant (MethodInfo mi){
    return methodSpec.matches(mi);
  }

  boolean isVarRelevant (String varId) {
    if (!StringSetMatcher.isMatch(varId, includeVars, excludeVars)){
      return false;
    }
    
    // filter subsequent changes in the same transition (to avoid gazillions of
    // VarChanges for loop variables etc.)
    // <2do> this is very inefficient - improve
    for (int i=0; i<queue.size(); i++) {
      VarChange change = queue.get(i);
      if (change.getVariableId().equals(varId)) {
        return false;
      }
    }
    
    return true;
  }
}

// <2do> expand into types to record value ranges
class VarStat implements Comparable<VarStat> {
  String id;               // class[@objRef].field || class[@objref].method.local
  int nChanges;
  
  int lastState;           // this was changed in (<2do> don't think we need this)
  
  // might have more info in the future, e.g. total number of changes vs.
  // number of states incl. this var change, source locations, threads etc.
  
  VarStat (String varId, int stateId) {
    id = varId;
    nChanges = 1;
    
    lastState = stateId;
  }
  
  int getChangeCount () {
    return nChanges;
  }
  
  public int compareTo (VarStat other) {
    if (other.nChanges > nChanges) {
      return 1;
    } else if (other.nChanges == nChanges) {
      return 0;
    } else {
      return -1;
    }
  }
}

// <2do> expand into types to record values
class VarChange {
  String id;
  
  VarChange (String varId) {
    id = varId;
  }
  
  String getVariableId () {
    return id;
  }
}
