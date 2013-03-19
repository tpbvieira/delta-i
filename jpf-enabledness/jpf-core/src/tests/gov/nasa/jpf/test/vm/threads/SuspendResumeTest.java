//
// Copyright (C) 2008 United States Government as represented by the
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
package gov.nasa.jpf.test.vm.threads;

import gov.nasa.jpf.jvm.Verify;
import gov.nasa.jpf.util.test.TestJPF;
import org.junit.*;

/**
 * regression test for suspend/resume
 */
@SuppressWarnings("deprecation")
public class SuspendResumeTest extends TestJPF {

  static boolean isRunning;
  static boolean pass = false;
  
  static class T1 extends Thread {
    public void run(){
      System.out.println("t1 running");
      isRunning = true;
      while (!pass){
        Thread.yield();
      }
      System.out.println("t1 terminating");
    }
  }

  @Test
  public void testBasicSuspendDeadlock(){
    if (verifyDeadlock("+cg.threads.break_yield")) {
      Thread t1 = new T1();
      t1.start();

      while (!isRunning) {
        Thread.yield();
      }

      t1.suspend();
      assertTrue(t1.getState() == Thread.State.RUNNABLE);

      pass = true;
      
      // without resuming, T1 should not be scheduled again, despite being in a RUNNABLE state
      //t1.resume();
    }
  }
  
  @Test
  public void testBasicSuspendResume(){
    if (verifyNoPropertyViolation("+cg.threads.break_yield")) {
      Thread t1 = new T1();
      t1.start();

      while (!isRunning) {
        Thread.yield();
      }

      System.out.println("main suspending t1");
      t1.suspend();
      assertTrue(t1.getState() == Thread.State.RUNNABLE);

      pass = true;
      
      System.out.println("main resuming t1");
      t1.resume();
      try {
        System.out.println("main joining t1");
        t1.join();
      } catch (InterruptedException ix){
        fail("t1.join got interrupted");
      }
      
      System.out.println("main terminating after t1.join");
    }
  }

  //---------------
  // this is the main reason to model suspend/resume, since suspension does
  // *not* give up any held locks, and hence is very prone to creating deadlocks
  
  static class T2 extends Thread {
    public synchronized void run(){
      System.out.println("t2 running with lock");
      isRunning = true;
      while (!pass){
        Thread.yield();
      }
      System.out.println("t2 terminating");
    }
  }
  
  @Test
  public void testLockholderSuspendDeadlock(){

    if (verifyDeadlock("+cg.threads.break_yield")) {
      Thread t2 = new T2();
      t2.start();

      while (!isRunning) {
        Thread.yield();
      }

      System.out.println("main suspending t2");
      t2.suspend();
      // now t2 should hold and never give up its lock 
      
      synchronized (t2){
        fail("main should never get here");
      }
    }
  }
  
  //------------
  
  static class T3 extends Thread {
    public synchronized void run(){
      System.out.println("t3 running");
      isRunning = true;
      try {
        wait();
      } catch (InterruptedException ix){
        fail("t3 got interrupted");
      }
      System.out.println("t3 terminating");
    }
  }

  @Test
  public void testWaitingSuspendNotifyDeadlock(){
    if (verifyDeadlock("+cg.threads.break_yield")) {
      Thread t3 = new T3();
      t3.start();

      while (!isRunning) {
        Thread.yield();
      }
      
      synchronized (t3){
        assertTrue( t3.getState() == Thread.State.WAITING);
        
        System.out.println("main suspending t3");
        t3.suspend();
        
        System.out.println("main notifying t3");
        t3.notify();
        // t3 should be still suspended, despite being notified
      }
    }    
  }
  
  @Test
  public void testWaitingSuspendNotifyResume(){
    if (verifyNoPropertyViolation("+cg.threads.break_yield")) {
      Thread t3 = new T3();
      t3.start();

      while (!isRunning) {
        Thread.yield();
      }
      
      synchronized (t3){
        assertTrue( t3.getState() == Thread.State.WAITING);
        
        System.out.println("main suspending t3");
        t3.suspend();
        
        System.out.println("main notifying t3");
        t3.notify();
        // t3 should be still suspended, despite being notified
        
        System.out.println("main resuming t3");
        t3.resume();
        try {
          System.out.println("main joining t3");
          t3.join();
        } catch (InterruptedException ix) {
          fail("t3.join got interrupted");
        }

        System.out.println("main terminating after t3.join");
      }
    }    
  }
  
  
  //----------------
  
  static class T4 extends Thread {
    public void run(){
      System.out.println("t4 running ");
      isRunning = true;
      while (!pass){
        Thread.yield();
      }
      
      System.out.println("t4 trying to obtain lock");      
      synchronized (this){
        System.out.println("t4 obtained lock");
      }
      System.out.println("t4 terminating");
    }
  }
  
  @Test
  public void testBlockSuspendUnblockDeadlock(){
    if (verifyDeadlock("+cg.threads.break_yield")) {
      Thread t4 = new T4();
      t4.start();

      while (!isRunning) {
        Thread.yield();
      }
      
      synchronized (t4){
        pass = true;
        
        while (t4.getState() != Thread.State.BLOCKED){
          Thread.yield();
        }
        
        System.out.println("main suspending t4");
        t4.suspend();
      }
      System.out.println("main released t4 lock");
    }
  }

  
  @Test
  public void testBlockSuspendUnblockResume(){
    if (verifyNoPropertyViolation("+cg.threads.break_yield")) {
      Thread t4 = new T4();
      t4.start();

      while (!isRunning) {
        Thread.yield();
      }
      
      synchronized (t4){
        pass = true;
        
        while (t4.getState() != Thread.State.BLOCKED){
          Thread.yield();
        }
        
        System.out.println("main suspending t4");
        t4.suspend();
      }
      System.out.println("main released t4 lock");

      System.out.println("main resuming t4");
      t4.resume();
      try {
        System.out.println("main joining t4");
        t4.join();
      } catch (InterruptedException ix) {
        fail("t4.join got interrupted");
      }

      System.out.println("main terminating after t4.join");
    }
  }
}
