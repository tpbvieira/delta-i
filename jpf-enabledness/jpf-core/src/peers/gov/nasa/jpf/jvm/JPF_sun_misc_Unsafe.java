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

import gov.nasa.jpf.JPFException;
import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * we don't want this class! This is a hodgepodge of stuff that shouldn't be in Java, but
 * is handy for some hacks. The reason we have it here - very rudimentary - is that
 * java.util.concurrent makes use of the atomic compare&swap which is in it.
 * The choice was to duplicate a lot of relatively difficult code in the "right" class
 * (java.util.concurrent.locks.AbstractQueuedSynchronizer) or a small amount of straight forward
 * code in the "wrong" class (sun.misc.Unsafe). Knowing a bit about the "library chase" game,
 * we opt for the latter
 *
 * <2do> this might change with better modeling of high level java.util.concurrent constructs
 */
public class JPF_sun_misc_Unsafe {

  public  int getUnsafe____Lsun_misc_Unsafe_2 (MJIEnv env, int clsRef) {
    int objRef = env.getStaticReferenceField("sun.misc.Unsafe", "theUnsafe");
    return objRef;
  }

  public static long objectFieldOffset__Ljava_lang_reflect_Field_2__J (MJIEnv env, int unsafeRef, int fieldRef) {
    return fieldOffset__Ljava_lang_reflect_Field_2__I(env, unsafeRef, fieldRef);
  }

  /**
   * we don't really return an offset here, since that would be useless. What we really want is
   * to identify the corresponding FieldInfo, and that's much easier done with the Field
   * registration id
   */
  public static int fieldOffset__Ljava_lang_reflect_Field_2__I (MJIEnv env, int unsafeRef, int fieldRef) {
    //FieldInfo fi = JPF_java_lang_reflect_Field.getFieldInfo(env, fieldRef);
    //return fi.getStorageOffset();
    return env.getIntField(fieldRef, "regIdx");
  }

  public static boolean compareAndSwapObject__Ljava_lang_Object_2JLjava_lang_Object_2Ljava_lang_Object_2__Z (MJIEnv env, int unsafeRef,
                                                                                                             int objRef, long fieldOffset,
                                                                                                             int expectRef, int updateRef) {
    int actual = getObject__Ljava_lang_Object_2J__Ljava_lang_Object_2(env, unsafeRef, objRef, fieldOffset);
    if (actual == expectRef) {
      putObject__Ljava_lang_Object_2JLjava_lang_Object_2__V(env, unsafeRef, objRef, fieldOffset, updateRef);
      return true;
    }
    return false;
  }

  public static boolean compareAndSwapInt__Ljava_lang_Object_2JII__Z (MJIEnv env, int unsafeRef,
                                                                      int objRef, long fieldOffset, int expect, int update) {
    int actual = getInt__Ljava_lang_Object_2J__I(env, unsafeRef, objRef, fieldOffset);
    if (actual == expect) {
      putInt__Ljava_lang_Object_2JI__V(env, unsafeRef, objRef, fieldOffset, update);
      return true;
    }
    return false;
  }

  public static boolean compareAndSwapLong__Ljava_lang_Object_2JJJ__Z (MJIEnv env, int unsafeRef,
                                                                       int objRef, long fieldOffset, long expect, long update) {
    long actual = getLong__Ljava_lang_Object_2J__J(env, unsafeRef, objRef, fieldOffset);
    if (actual == expect) {
      putLong__Ljava_lang_Object_2JJ__V(env, unsafeRef, objRef, fieldOffset, update);
      return true;
    }
    return false;
  }


  // this is a specialized, native wait that does not require a lock, and that can
  // be turned off by a preceding unpark() call (which is not accumulative)
  // park can be interrupted, but it doesn't throw an InterruptedException, and it doesn't clear the status

  public static void park__ZJ__V (MJIEnv env, int unsafeRef, boolean isAbsoluteTime, long timeout) {
    ThreadInfo ti = env.getThreadInfo();
    int objRef = ti.getThreadObjectRef();
    int permitRef = env.getReferenceField( objRef, "permit");
    ElementInfo ei = env.getElementInfo(permitRef);

    if (ti.isFirstStepInsn()){ // re-executed

      //assert ti.getLockObject() == null : "private 'permit' object locked";

      // notified | timedout | interrupted -> running
      switch (ti.getState()) {
        case NOTIFIED:
        case TIMEDOUT:
        case INTERRUPTED:
          ti.resetLockRef();
          ti.setRunning();
        default:
      }

    } else { // first time

      if (ti.isInterrupted(false)) {
        // there is no lock, so we go directly back to running and therefore
        // have to remove ourself from the contender list
        ei.setMonitorWithoutLocked(ti);
        
        // note that park() does not throw an InterruptedException
        return;
      }

      if (ei.getBooleanField("blockPark")) { // we have to wait, but don't need a lock

        // running -> waiting | timeout_waiting
        ei.wait(ti, timeout, false);

        assert ti.isWaiting();


        // note we pass in the timeout value, since this might determine the type of CG that is created
        ChoiceGenerator<?> cg = env.getSchedulerFactory().createWaitCG(ei, ti, timeout);
        env.setMandatoryNextChoiceGenerator(cg, "no CG on blocking park()");
        env.repeatInvocation();
  
      } else {
        ei.setBooleanField("blockPark", true); // next time
      }
    }
  }

  public static void unpark__Ljava_lang_Object_2__V (MJIEnv env, int unsafeRef, int objRef) {
    ThreadInfo ti = env.getThreadInfo();
    ThreadInfo tiParked = JPF_java_lang_Thread.getThreadInfo(objRef);
    
    if (tiParked.isTerminated()){
      return;
    }
    
    SystemState ss = env.getSystemState();

    int permitRef = env.getReferenceField( objRef, "permit");
    ElementInfo ei = env.getElementInfo(permitRef);

    if (tiParked.getLockObject() == ei){
      ei.notifies(ss, ti, false);
    } else {
      ei.setBooleanField("blockPark", false);
    }
  }

  public static void ensureClassInitialized__Ljava_lang_Class_2__V (MJIEnv env, int unsafeRef, int clsObjRef) {
    // <2do> not sure if we have to do anyting here - if we have a class object, the class should already
    // be initialized
  }

  public static int getObject__Ljava_lang_Object_2J__Ljava_lang_Object_2 (MJIEnv env, int unsafeRef,
                                                                          int objRef, long fieldOffset) {
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      return ei.getReferenceField(fi);
    } else {
      return ei.getReferenceElement((int)fieldOffset);
    }
  }

  public static void putObject__Ljava_lang_Object_2JLjava_lang_Object_2__V (MJIEnv env, int unsafeRef,
                                                                            int objRef, long fieldOffset, int valRef) {
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      ei.setReferenceField(fi, valRef);
    } else {
      ei.setReferenceElement((int)fieldOffset, valRef);
    }
  }

  public static void putOrderedObject__Ljava_lang_Object_2JLjava_lang_Object_2__V(
                                                                                  MJIEnv env,
                                                                                  int unsafeRef,
                                                                                  int objRef,
                                                                                  long fieldOffset,
                                                                                  int valRef) {
    putObject__Ljava_lang_Object_2JLjava_lang_Object_2__V(env, unsafeRef, objRef, fieldOffset, valRef);
  }
  
  public static boolean getBoolean__Ljava_lang_Object_2J__Z(MJIEnv env,
                                                            int unsafeRef,
                                                            int objRef,
                                                            long fieldOffset) {
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      return ei.getBooleanField(fi);
    } else {
      return ei.getBooleanElement((int)fieldOffset);
    }
  }
  
  public static void putBoolean__Ljava_lang_Object_2JZ__V (MJIEnv env, int unsafeRef,
                                                       int objRef, long fieldOffset, boolean val){
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      ei.setBooleanField(fi, val);
    } else {
      ei.setBooleanElement((int)fieldOffset, val);
    }
  }

  public static byte getByte__Ljava_lang_Object_2J__B(MJIEnv env,
                                                      int unsafeRef,
                                                      int objRef,
                                                      long fieldOffset) {
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      return ei.getByteField(fi);
    } else {
      return ei.getByteElement((int)fieldOffset);
    }
  }
  
  public static void putByte__Ljava_lang_Object_2JB__V (MJIEnv env, int unsafeRef,
                                                       int objRef, long fieldOffset, byte val){
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      ei.setByteField(fi, val);
    } else {
      ei.setByteElement((int)fieldOffset, val);
    }
  }

  public static char getChar__Ljava_lang_Object_2J__C(MJIEnv env,
                                                      int unsafeRef,
                                                      int objRef,
                                                      long fieldOffset) {
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      return ei.getCharField(fi);
    } else {
      return ei.getCharElement((int)fieldOffset);
    }
  }
  
  public static void putChar__Ljava_lang_Object_2JC__V (MJIEnv env, int unsafeRef,
                                                       int objRef, long fieldOffset, char val){
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      ei.setCharField(fi, val);
    } else {
      ei.setCharElement((int)fieldOffset, val);
    }
  }

  public static short getShort__Ljava_lang_Object_2J__S(MJIEnv env,
                                                        int unsafeRef,
                                                        int objRef,
                                                        long fieldOffset) {
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      return ei.getShortField(fi);
    } else {
      return ei.getShortElement((int)fieldOffset);
    }
  }

  public static void putShort__Ljava_lang_Object_2JS__V (MJIEnv env, int unsafeRef,
                                                       int objRef, long fieldOffset, short val){
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      ei.setShortField(fi, val);
    } else {
      ei.setShortElement((int)fieldOffset, val);
    }
  }

  public static int getInt__Ljava_lang_Object_2J__I(MJIEnv env, int unsafeRef,
                                                    int objRef, long fieldOffset) {
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      return ei.getIntField(fi);
    } else {
      return ei.getIntElement((int)fieldOffset);
    }
  }

  public static void putInt__Ljava_lang_Object_2JI__V (MJIEnv env, int unsafeRef,
                                                       int objRef, long fieldOffset, int val){
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      ei.setIntField(fi, val);
    } else {
      ei.setIntElement((int)fieldOffset, val);
    }
  }

  public static void putOrderedInt__Ljava_lang_Object_2JI__V(MJIEnv env,
                                                             int unsafeRef,
                                                             int objRef,
                                                             long fieldOffset,
                                                             int val) {
    // volatile?
    putInt__Ljava_lang_Object_2JI__V(env, unsafeRef, objRef, fieldOffset, val);
  }

  public static float getFloat__Ljava_lang_Object_2J__F(MJIEnv env,
                                                        int unsafeRef,
                                                        int objRef,
                                                        long fieldOffset) {
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      return ei.getFloatField(fi);
    } else {
      return ei.getFloatElement((int)fieldOffset);
    }
  }

  public static void putFloat__Ljava_lang_Object_2JF__V (MJIEnv env, int unsafeRef,
                                                       int objRef, long fieldOffset, float val){
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      ei.setFloatField(fi, val);
    } else {
      ei.setFloatElement((int)fieldOffset, val);
    }
  }

  public static long getLong__Ljava_lang_Object_2J__J(MJIEnv env,
                                                      int unsafeRef,
                                                      int objRef,
                                                      long fieldOffset) {
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      return ei.getLongField(fi);
    } else {
      return ei.getLongElement((int)fieldOffset);
    }
  }

  public static void putLong__Ljava_lang_Object_2JJ__V (MJIEnv env, int unsafeRef,
                                                       int objRef, long fieldOffset, long val){
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      ei.setLongField(fi, val);
    } else {
      ei.setLongElement((int)fieldOffset, val);
    }
  }

  public static void putOrderedLong__Ljava_lang_Object_2JJ__V (MJIEnv env, int unsafeRef,
                                                        int objRef, long fieldOffset, long val) {
    putLong__Ljava_lang_Object_2JJ__V(env, unsafeRef, objRef, fieldOffset, val);
  }

  public static double getDouble__Ljava_lang_Object_2J__D(MJIEnv env,
                                                         int unsafeRef,
                                                         int objRef,
                                                         long fieldOffset) {
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      return ei.getDoubleField(fi);
    } else {
      return ei.getDoubleElement((int)fieldOffset);
    }
  }

  public static void putDouble__Ljava_lang_Object_2JD__V (MJIEnv env, int unsafeRef,
                                                       int objRef, long fieldOffset, double val){
    ElementInfo ei = env.getElementInfo(objRef);
    if (!ei.isArray()) {
      FieldInfo fi = getRegisteredFieldInfo(fieldOffset);
      ei.setDoubleField(fi, val);
    } else {
      ei.setDoubleElement((int)fieldOffset, val);
    }
  }

  public static int arrayBaseOffset__Ljava_lang_Class_2__I (MJIEnv env, int unsafeRef, int clazz) {
    return 0;
  }

  public static int arrayIndexScale__Ljava_lang_Class_2__I (MJIEnv env, int unsafeRef, int clazz) {
    return 1;
  }

  private static FieldInfo getRegisteredFieldInfo(long fieldOffset) {
    return JPF_java_lang_reflect_Field.getRegisteredFieldInfo((int)fieldOffset);
  }

  
  //--- the explicit memory buffer allocation/free + access methods - evil pointer arithmetic

  /*
   * we shy away from maintaining our own address table by means of knowing that
   * the byte[] object stored in the ArrayFields will not be recycled, and hashCode() will
   * return its address, so the start/endAdr pairs we get from that have to be
   * non-overlapping. Of course that falls apart if  hashCode() would do something
   * different, which is the case for any address that exceeds 32bit
   */
  
  static class Alloc {
    int objRef;
    
    int startAdr;
    int endAdr;
    
    Alloc next;
    
    Alloc (MJIEnv env, int baRef, long length){
      this.objRef = baRef;

      ElementInfo ei = env.getElementInfo(baRef);
      ArrayFields afi = (ArrayFields) ei.getFields();
      byte[] mem = afi.asByteArray();

      startAdr = mem.hashCode();
      endAdr = startAdr + (int)length -1;
    }
    
    public String toString(){
      return String.format("Alloc[objRef=%x,startAdr=%x,endAdr=%x]", objRef, startAdr, endAdr);
    }
  }
  
  static Alloc firstAlloc;
  
  // for debugging purposes only
  private static void dumpAllocs(){
    System.out.println("Unsafe allocated memory blocks:{");
    for (Alloc a = firstAlloc; a != null; a = a.next){
      System.out.print("  ");
      System.out.println(a);
    }
    System.out.println('}');
  }
  
  private static void sortInAlloc(Alloc newAlloc){
    int startAdr = newAlloc.startAdr;
    
    if (firstAlloc == null){
      firstAlloc = newAlloc;
      
    } else {
      Alloc prev = null;
      for (Alloc a = firstAlloc; a != null; prev = a, a = a.next){
        if (startAdr < a.startAdr){
          newAlloc.next = a;
          if (prev == null){
            firstAlloc = newAlloc;
          } else {
            prev.next = newAlloc;
          }
        }
      }
    }
  }
  
  private static Alloc getAlloc (int address){
    for (Alloc a = firstAlloc; a != null; a = a.next){
      if (address >= a.startAdr && address <= a.endAdr){
        return a;
      }
    }
    
    return null;
  }
  
  private static Alloc removeAlloc (int startAddress){
    Alloc prev = null;
    for (Alloc a = firstAlloc; a != null; prev = a, a = a.next) {
      if (a.startAdr == startAddress){
        if (prev == null){
          firstAlloc = a.next;
        } else {
          prev.next = a.next;
        }
        
        return a;
      }
    }
    
    return null;
  }
  
  
  public static long allocateMemory__J__J (MJIEnv env, int unsafeRef, long nBytes) {
    if (nBytes < 0 || nBytes > Integer.MAX_VALUE) {
      env.throwException("java.lang.IllegalArgumentException", "invalid memory block size: " + nBytes);
      return 0;
    }
    
    // <2do> we should probably also throw OutOfMemoryErrors on configured thresholds 
    
    int baRef = env.newByteArray((int) nBytes);
    // the corresponding objects have to be freed explicitly
    env.registerPinDown(baRef);
    
    Alloc alloc = new Alloc(env, baRef, nBytes);
    sortInAlloc(alloc);
    
    return alloc.startAdr;
  }
  
  public static void freeMemory__J__V (MJIEnv env, int unsafeRef, long startAddress) {
    int addr = (int)startAddress;

    if (startAddress != MJIEnv.NULL){
      Alloc a = removeAlloc(addr);
      if (a == null){
        env.throwException("java.lang.IllegalArgumentException", "invalid memory address: " + Integer.toHexString(addr));
      } else {
        env.releasePinDown(a.objRef);
      }
    }
  }
  
  public static byte getByte__J__B (MJIEnv env, int unsafeRef, long address) {
    int addr = (int)address;
    Alloc a = getAlloc(addr);
    
    if (a == null) {
      env.throwException("java.lang.IllegalArgumentException", "invalid memory address: " + Integer.toHexString(addr));
      return 0;
    }
    
    ElementInfo ei = env.getElementInfo(a.objRef);
    return ei.getByteElement(addr - a.startAdr);
  }
  public static void putByte__JB__V (MJIEnv env, int unsafeRef, long address, byte val) {
    int addr = (int)address;
    Alloc a = getAlloc(addr);
    
    if (a == null) {
      env.throwException("java.lang.IllegalArgumentException", "invalid memory address: " + Integer.toHexString(addr));
      return;
    }
    
    ElementInfo ei = env.getElementInfo(a.objRef);
    ei.setByteElement(addr - a.startAdr, val);
  }
  
  public static char getChar__J__C (MJIEnv env, int unsafeRef, long address) {
    int addr = (int)address;
    Alloc a = getAlloc(addr);
    
    if (a == null) {
      env.throwException("java.lang.IllegalArgumentException", "invalid memory address: " + Integer.toHexString(addr));
      return 0;
    }
    
    ElementInfo ei = env.getElementInfo(a.objRef);
    byte[] ba = ei.asByteArray();
    
    byte b0 = ba[addr];
    byte b1 = ba[addr+1];
    
    char val;
    if (env.isBigEndianPlatform()){
      val = (char) ((b0 << 8) | b1);
    } else {
      val = (char) ((b1 << 8) | b0);      
    }
    
    return val;
  }  
  public static void putChar__JC__V (MJIEnv env, int unsafeRef, long address, char val) {
    int addr = (int)address;
    Alloc a = getAlloc(addr);
    
    if (a == null) {
      env.throwException("java.lang.IllegalArgumentException", "invalid memory address: " + Integer.toHexString(addr));
      return;
    }
        
    byte b1 = (byte)(0xff & val);
    byte b0 = (byte)(0xff & (val >>> 8));
    
    ElementInfo ei = env.getElementInfo(a.objRef);

    if (env.isBigEndianPlatform()){
      ei.setByteElement(addr,   b0);
      ei.setByteElement(addr+1, b1);
    } else {
      ei.setByteElement(addr,   b1);
      ei.setByteElement(addr+1, b0);      
    }
  }
  
  public static int getInt__J__I (MJIEnv env, int unsafeRef, long address) {
    int addr = (int)address;
    Alloc a = getAlloc(addr);
    
    if (a == null) {
      env.throwException("java.lang.IllegalArgumentException", "invalid memory address: " + Integer.toHexString(addr));
      return 0;
    }
    
    ElementInfo ei = env.getElementInfo(a.objRef);
    byte[] ba = ei.asByteArray();
    
    byte b0 = ba[addr];
    byte b1 = ba[addr+1];
    byte b2 = ba[addr+2];
    byte b3 = ba[addr+3];
    
    int val;
    if (env.isBigEndianPlatform()){
      val = b0;
      val = (val << 8) | b1;
      val = (val << 8) | b2;
      val = (val << 8) | b3;

    } else {
      val = b3;
      val = (val << 8) | b2;
      val = (val << 8) | b1;
      val = (val << 8) | b0;
    }
    
    return val;
  }  
  public static void putInt__JI__V (MJIEnv env, int unsafeRef, long address, int val) {
    int addr = (int)address;
    Alloc a = getAlloc(addr);
    
    if (a == null) {
      env.throwException("java.lang.IllegalArgumentException", "invalid memory address: " + Integer.toHexString(addr));
      return;
    }
        
    byte b3 = (byte)(0xff & val);
    byte b2 = (byte)(0xff & (val >>> 8));
    byte b1 = (byte)(0xff & (val >>> 16));
    byte b0 = (byte)(0xff & (val >>> 24));    
    
    ElementInfo ei = env.getElementInfo(a.objRef);

    if (env.isBigEndianPlatform()){
      ei.setByteElement(addr,   b0);
      ei.setByteElement(addr+1, b1);
      ei.setByteElement(addr+2, b2);
      ei.setByteElement(addr+3, b3);
    } else {
      ei.setByteElement(addr,   b3);
      ei.setByteElement(addr+1, b2);
      ei.setByteElement(addr+2, b1);
      ei.setByteElement(addr+3, b0);
    }
  }

  public static long getLong__J__J (MJIEnv env, int unsafeRef, long address) {
    int addr = (int)address;
    Alloc a = getAlloc(addr);
    
    if (a == null) {
      env.throwException("java.lang.IllegalArgumentException", "invalid memory address: " + Integer.toHexString(addr));
      return 0;
    }
    
    ElementInfo ei = env.getElementInfo(a.objRef);
    byte[] ba = ei.asByteArray();
    int offset = addr - a.startAdr;
    
    byte b0 = ba[offset];
    byte b1 = ba[offset+1];
    byte b2 = ba[offset+2];
    byte b3 = ba[offset+3];
    byte b4 = ba[offset+4];
    byte b5 = ba[offset+5];
    byte b6 = ba[offset+6];
    byte b7 = ba[offset+7];
    
    int val;
    if (env.isBigEndianPlatform()){
      val = b0;
      val = (val << 8) | b1;
      val = (val << 8) | b2;
      val = (val << 8) | b3;
      val = (val << 8) | b4;
      val = (val << 8) | b5;
      val = (val << 8) | b6;
      val = (val << 8) | b7;

    } else {
      val = b7;
      val = (val << 8) | b6;
      val = (val << 8) | b5;
      val = (val << 8) | b4;
      val = (val << 8) | b3;
      val = (val << 8) | b2;
      val = (val << 8) | b1;
      val = (val << 8) | b0;
    }
    
    return val;
  }  
  public static void putLong__JJ__V (MJIEnv env, int unsafeRef, long address, long val) {
    int addr = (int)address;
    Alloc a = getAlloc(addr);
    
    if (a == null) {
      env.throwException("java.lang.IllegalArgumentException", "invalid memory address: " + Integer.toHexString(addr));
      return;
    }
        
    byte b7 = (byte)(0xff & val);
    byte b6 = (byte)(0xff & (val >>> 8));
    byte b5 = (byte)(0xff & (val >>> 16));
    byte b4 = (byte)(0xff & (val >>> 24));    
    byte b3 = (byte)(0xff & (val >>> 32));    
    byte b2 = (byte)(0xff & (val >>> 40));    
    byte b1 = (byte)(0xff & (val >>> 48));    
    byte b0 = (byte)(0xff & (val >>> 56));    

    ElementInfo ei = env.getElementInfo(a.objRef);
    int offset = addr - a.startAdr;
    
    if (env.isBigEndianPlatform()){
      ei.setByteElement(offset,   b0);
      ei.setByteElement(offset+1, b1);
      ei.setByteElement(offset+2, b2);
      ei.setByteElement(offset+3, b3);
      ei.setByteElement(offset+4, b4);
      ei.setByteElement(offset+5, b5);
      ei.setByteElement(offset+6, b6);
      ei.setByteElement(offset+7, b7);
      
    } else {
      ei.setByteElement(offset,   b7);
      ei.setByteElement(offset+1, b6);
      ei.setByteElement(offset+2, b5);
      ei.setByteElement(offset+3, b4);
      ei.setByteElement(offset+4, b3);
      ei.setByteElement(offset+5, b2);
      ei.setByteElement(offset+6, b1);
      ei.setByteElement(offset+7, b0);
    }
  }

}

