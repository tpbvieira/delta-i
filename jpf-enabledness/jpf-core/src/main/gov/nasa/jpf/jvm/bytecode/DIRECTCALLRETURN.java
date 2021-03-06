//
// Copyright (C) 2010 United States Government as represented by the
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

package gov.nasa.jpf.jvm.bytecode;

import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.StackFrame;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;

/**
 * this is used to return from a DirectCallStackFrame
 *
 * Note that it is NOT a ReturnInstruction, in case listeners monitor these
 * and expect corresponding InvokeInstructions. Although this would seem intuitive, it
 * would be pointless to derive because the ReturnInstruction.execute() does
 * a lot of things we would have to cut off, i.e. it would require more effort
 * to undo this (no sync, no return value, no pc advance on the returned-to
 * stackframe etc.)
 *
 * However, having a dedicated direct call return instruction makes sense so
 * that the ReturnInstruction of the called method does not have to handle
 * direct calls specifically
 */
public class DIRECTCALLRETURN extends Instruction implements gov.nasa.jpf.jvm.ReturnInstruction {

  @Override
  public boolean isExtendedInstruction() {
    return true;
  }

  public static final int OPCODE = 261;

  @Override
  public int getByteCode () {
    return OPCODE;
  }

  @Override
  public void accept(InstructionVisitor insVisitor) {
	  insVisitor.visit(this);
  }

  @Override
  public Instruction execute (SystemState ss, KernelState ks, ThreadInfo ti) {
    // pop the current frame but do not advance the new top frame, and do
    // not touch its operand stack
    
    if (ti.getStackDepth() == 1){ // thread exit point (might be re-executed)
    
      if (!ti.exit()){
        return this; // repeat, we couldn't get the lock
      } else {
        return null;
      }      
      
    } else {
      StackFrame frame = ti.popDirectCallFrame();
      return frame.getPC();
    }
  }
}
