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
package gov.nasa.jpf.jvm;

import gov.nasa.jpf.JPF;
import gov.nasa.jpf.JPFException;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import gov.nasa.jpf.util.JPFLogger;


/**
 * MJI NativePeer class for java.lang.Thread library abstraction
 */
public class JPF_java_lang_Thread {

  static JPFLogger log = JPF.getLogger("gov.nasa.jpf.jvm.ThreadInfo");


  public static boolean isAlive____Z (MJIEnv env, int objref) {
    return getThreadInfo(objref).isAlive();
  }

  public static void setDaemon0__Z__V (MJIEnv env, int objref, boolean isDaemon) {
    ThreadInfo ti = getThreadInfo(objref);
    ti.setDaemon(isDaemon);
  }

  public static void dumpStack____V (MJIEnv env, int clsObjRef){
    ThreadInfo ti = env.getThreadInfo();
    ti.printStackTrace(); // this is not correct, we should go through JVM.print
  }

  public static void setName0__Ljava_lang_String_2__V (MJIEnv env, int objref, int nameRef) {
    // it bails if you try to set a null name
    if (nameRef == -1) {
      env.throwException("java.lang.IllegalArgumentException");

      return;
    }

    // we have to intercept this to cache the name as a Java object
    // (to be stored in ThreadData)
    // luckily enough, it's copied into the java.lang.Thread object
    // as a char[], i.e. does not have to preserve identity
    // Note the nastiness in here - the java.lang.Thread object is only used
    // to get the initial values into ThreadData, and gets inconsistent
    // if this method is called (just works because the 'name' field is only
    // directly accessed from within the Thread ctors)
    ThreadInfo ti = getThreadInfo(objref);
    ti.setName(env.getStringObject(nameRef));
  }

  public static void setPriority0__I__V (MJIEnv env, int objref, int prio) {
    // again, we have to cache this in ThreadData for performance reasons
    ThreadInfo ti = getThreadInfo(objref);
    ti.setPriority(prio);
  }

  public static int countStackFrames____I (MJIEnv env, int objref) {
    return getThreadInfo(objref).countStackFrames();
  }

  public static int currentThread____Ljava_lang_Thread_2 (MJIEnv env, int clsObjRef) {
    ThreadInfo ti = env.getThreadInfo();

    return ti.getThreadObjectRef();
  }

  public static boolean holdsLock__Ljava_lang_Object_2__Z (MJIEnv env, int clsObjRef, int objref) {
    ThreadInfo  ti = env.getThreadInfo();
    ElementInfo ei = env.getElementInfo(objref);

    return ei.isLockedBy(ti);
  }

  /**
   * This method is the common initializer for all Thread ctors, and the only
   * single location where we can init our ThreadInfo, but it is PRIVATE
   */

  // wow, that's almost like C++
  public static void init0__Ljava_lang_ThreadGroup_2Ljava_lang_Runnable_2Ljava_lang_String_2J__V (MJIEnv env,
                                                                                                  int objref,
                                                                                                  int rGroup,
                                                                                                  int rRunnable,
                                                                                                  int rName,
                                                                                                  long stackSize) {
    ThreadInfo newThread = createThreadInfo(env, objref);
    newThread.init(rGroup, rRunnable, rName, stackSize, true);
  }

  public static void interrupt____V (MJIEnv env, int objref) {
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();

    ThreadInfo interruptedThread = getThreadInfo(objref);

    if (!ti.isFirstStepInsn()) {
      interruptedThread.interrupt();
      
      // the interrupted thread can re-acquire the lock and therefore is runnable again
      // hence we should give it a chance to do so (Thread.interrupt() does not require
      // holding a lock)
      if (interruptedThread.isUnblocked()){
        ChoiceGenerator<?> cg = ss.getSchedulerFactory().createInterruptCG(interruptedThread);
        if (ss.setNextChoiceGenerator(cg)) {
          env.repeatInvocation();
        }        
      }

    }
  }

  // these could be in the model, but we keep it symmetric, which also saves
  // us the effort of avoiding unwanted shared object field access CGs
  public static boolean isInterrupted____Z (MJIEnv env, int objref) {
    ThreadInfo ti = getThreadInfo(objref);
    return ti.isInterrupted(false);
  }

  public static boolean interrupted____Z (MJIEnv env, int clsObjRef) {
    ThreadInfo ti = env.getThreadInfo();
    return ti.isInterrupted(true);
  }


  public static void start____V (MJIEnv env, int objref) {
    ThreadInfo tiCurrent = env.getThreadInfo();
    SystemState ss = env.getSystemState();
    JVM vm = tiCurrent.getVM();
    ThreadInfo tiStartee = getThreadInfo(objref);


    if (!tiCurrent.isFirstStepInsn()) { // first time we see this (may be the only time)

      if (tiStartee.isStopped()) {
        // don't do anything but set it terminated - it hasn't acquired any resources yet.
        // note that apparently host VMs don't schedule this thread, so it never
        // gets a handler in its miRun() invoked
        tiStartee.setTerminated();
        return;
      }
      
      // check if this thread was already started. If it is still running, this
      // is a IllegalThreadStateException. If it already terminated, it just gets
      // silently ignored in Java 1.4, but the 1.5 spec explicitly marks this
      // as illegal, so we adopt this by throwing an IllegalThreadState, too
      if (! tiStartee.isNew()) {
        env.throwException("java.lang.IllegalThreadStateException");
        return;
      }

      // Ouch - that's bad. we have to dig this out from the innards
      // of the java.lang.Thread class
      int targetRef = tiStartee.getTarget();

      if (targetRef == -1) {
        // note that we don't set the 'targetRef' field, since java.lang.Thread doesn't
        targetRef = objref;

        // better late than never
        tiStartee.setTarget(targetRef);
      }

      // we don't do this during thread creation because the thread isn't in
      // the GC root set before it actually starts to execute. Until then,
      // it's just an ordinary object

      vm.notifyThreadStarted(tiStartee);

      ElementInfo eiTarget = env.getElementInfo(targetRef);
      ClassInfo   ci = eiTarget.getClassInfo();
      MethodInfo  miRun = ci.getMethod("run()V", true);

      // we do direct call run() invocation so that we have a well defined
      // exit point (DIRECTCALLRETURN) in case the thread is stopped or there is
      // a fail-safe UncaughtExceptionHandler set
      MethodInfo runStub = miRun.createDirectCallStub("[run]");
      DirectCallStackFrame runFrame = new DirectCallStackFrame(runStub, 1, 0);
      runFrame.pushRef(targetRef);
      // we need this in case of a synchronized run(), for which the invokes would
      // always be the firstStepInsn
      runFrame.setPC( MethodInfo.getInstructionFactory().runstart(runStub));
      
      tiStartee.pushFrame(runFrame);
      tiStartee.setState(ThreadInfo.State.RUNNING);

      
      // now we have a new thread, create a CG for scheduling it
      ChoiceGenerator<?> cg = ss.getSchedulerFactory().createThreadStartCG(tiStartee);
      if (ss.setNextChoiceGenerator(cg)) {
        env.repeatInvocation();
      } else {
        Instruction insn = tiCurrent.getPC();
        log.info(tiStartee.getName(), " start not a scheduling point in ", insn.getMethodInfo().getFullName());
      }
      
    } else {
      // nothing to do in the bottom half
    }
  }

  public static void yield____V (MJIEnv env, int clsObjRef) {
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();

    if (!ti.isFirstStepInsn()) { // first time we see this (may be the only time)
      ChoiceGenerator<?> cg = ss.getSchedulerFactory().createThreadYieldCG( ti);
      if (ss.setNextChoiceGenerator(cg)) {
        env.repeatInvocation();
      }
    } else {
      // nothing to do, this was just a forced reschedule
    }
  }

  public static void sleep__JI__V (MJIEnv env, int clsObjRef, long millis, int nanos) {
    ThreadInfo ti = env.getThreadInfo();
    SystemState ss = env.getSystemState();

    if (!ti.isFirstStepInsn()) { // first time we see this (may be the only time)
      ChoiceGenerator<?> cg = ss.getSchedulerFactory().createThreadSleepCG( ti, millis, nanos);
      if (ss.setNextChoiceGenerator(cg)) {
        env.repeatInvocation();
        ti.setSleeping();
      }
    } else {
      // this seems asymmetric (since we only set it to sleeping when we register
      // a CG) but it isn't. This is only the firstStepInsn if we had a CG
      ti.setRunning();
    }
  }

  public static void suspend____ (MJIEnv env, int threadObjRef) {
    ThreadInfo currentThread = env.getThreadInfo();
    ThreadInfo target = getThreadInfo(threadObjRef);
    SystemState ss = env.getSystemState();

    if (target.isTerminated()) {
      return;
    }

    if (!currentThread.isFirstStepInsn()) {
      if (target.suspend()){
        ChoiceGenerator<?> cg = ss.getSchedulerFactory().createThreadSuspendCG();
        if (ss.setNextChoiceGenerator(cg)) {
          env.repeatInvocation();
          return;
        }
      }
    }
  }

  public static void resume____ (MJIEnv env, int threadObjRef) {
    ThreadInfo currentThread = env.getThreadInfo();
    ThreadInfo target = getThreadInfo(threadObjRef);
    SystemState ss = env.getSystemState();

    if (currentThread == target){
      // no self resume prior to suspension
      return;
    }

    if (target.isTerminated()) {
      return;
    }

    if (!currentThread.isFirstStepInsn()) {
      if (target.resume()){
        ChoiceGenerator<?> cg = ss.getSchedulerFactory().createThreadResumeCG();
        if (ss.setNextChoiceGenerator(cg)) {
          env.repeatInvocation();
          return;
        }
      }
    }
  }

  /**
   * this is here so that we don't have to break the transition on a synchronized call
   */
  static void join0 (MJIEnv env, int joineeRef, long timeout){
    ThreadInfo ti = env.getThreadInfo(); // this is the CURRENT thread (joiner)
    ElementInfo ei = env.getElementInfo(joineeRef); // the thread object to wait on
    boolean isAlive = getThreadInfo(joineeRef).isAlive();
    SystemState ss = env.getSystemState();

    if (ti.isInterrupted(true)){ // interrupt status is set, throw and bail
      
      // since we use lock-free joins, we need to remove ourselves from the
      // lock contender list
      ei.setMonitorWithoutLocked(ti);
      
      // note that we have to throw even if the thread to join to is not alive anymore
      env.throwInterrupt();
      return;
    }

    //--- the join
    if (ti.isFirstStepInsn()) { // re-execution, we already have a CG

      switch (ti.getState()){
        case UNBLOCKED:
          // Thread was owning the lock when it joined - we have to wait until
          // we can reacquire it
          ei.lockNotified(ti);
          break;

        case TIMEDOUT:
          ei.resumeNonlockedWaiter(ti);
          break;

        case RUNNING:
          if (isAlive) { // we still need to wait
            ei.wait(ti, timeout, false); // no need for a new CG
            env.repeatInvocation();
          }
          break;
      }

    } else { // first time exec, create a CG if the thread is still alive

      if (timeout < 0){
        env.throwException("java.lang.IllegalArgumentException", "timeout value is negative");
        return;
      }

      if (isAlive) {

        ei.wait(ti, timeout, false);
        ChoiceGenerator<ThreadInfo> cg = ss.getSchedulerFactory().createWaitCG(ei, ti, timeout);

        env.setMandatoryNextChoiceGenerator(cg, "no CG for blocking join()");
        env.repeatInvocation();

      } else {
        // nothing to do, thread is already terminated
      }
    }
  }

  // the old generic version that was based on a synchronized method, which
  // is bad because it leads to superfluous transitions
  /*
  public static void join__ (MJIEnv env, int objref) {
    ThreadInfo tiStop = getThreadInfo(env,objref);

    if (tiStop.isAlive()) {
      env.wait(objref);
    }
  }
   */


  public static void join____V (MJIEnv env, int objref){
    join0(env,objref,0);
  }

  public static void join__J__V (MJIEnv env, int objref, long millis) {
    join0(env,objref,millis);

  }

  public static void join__JI__V (MJIEnv env, int objref, long millis, int nanos) {
    join0(env,objref,millis); // <2do> we ignore nanos for now
  }


  public static long getId____J (MJIEnv env, int objref) {
    // doc says it only has to be valid and unique during lifetime of thread, hence we just use
    // the ThreadList index
    ThreadInfo ti = getThreadInfo(objref);
    return ti.getIndex();
  }

  public static int getState0____I (MJIEnv env, int objref) {
    // return the state index with respect to one of the public Thread.States
    ThreadInfo ti = getThreadInfo(objref);

    switch (ti.getState()) {
    case NEW:                return 1;
    case RUNNING:            return 2;
    case BLOCKED:            return 0;
    case UNBLOCKED:          return 2;
    case WAITING:            return 5;
    case TIMEOUT_WAITING:    return 4;
    case SLEEPING:           return 4;
    case NOTIFIED:           return 0;
    case INTERRUPTED:        return 0;
    case TIMEDOUT:           return 2;
    case TERMINATED:         return 3;
    default:
      throw new JPFException("illegal thread state: " + ti.getState());
    }
  }


  public static void stop____V (MJIEnv env, int threadRef) {
    stop__Ljava_lang_Throwable_2__V(env, threadRef, -1);
  }

  public static void stop__Ljava_lang_Throwable_2__V(MJIEnv env, int threadRef, int throwableRef) {
    ThreadInfo tiStop = getThreadInfo(threadRef);  // the thread to stop
    ThreadInfo tiCurrent = env.getThreadInfo(); // the currently executing thread

    if (tiStop.isTerminated() || tiStop.isStopped()) {
      // no need to kill it twice
      return;
    }

    if (!tiCurrent.isFirstStepInsn()) {
      // since this is usually not caught (it shouldn't, at least not without
      // rethrowing), we might turn this into a right mover since it terminates tiStop

      SystemState ss = env.getSystemState();
      ChoiceGenerator<?> cg = ss.getSchedulerFactory().createThreadStopCG();
      if (ss.setNextChoiceGenerator(cg)) {
        env.repeatInvocation();
        return;
      }
    }

    tiStop.setStopped(throwableRef);
  }

  protected static ThreadInfo createThreadInfo (MJIEnv env, int objref) {
    return ThreadInfo.createThreadInfo(env.getVM(), objref);
  }
  
  static ThreadInfo getThreadInfo (int objref) {
    return ThreadInfo.getThreadInfo(objref);     
  }
}
