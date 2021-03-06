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

/**
 * A specialized version of ElementInfo for use in the DynamicArea.
 * DynamicElementInfo objects represent heap objects
 * @see gov.nasa.jpf.jvm.ElementInfo
 */
public class DynamicElementInfo extends ElementInfo implements Restorable<ElementInfo> {

  // our default memento implementation
  static class DEIMemento extends EIMemento<DynamicElementInfo> {
    DEIMemento (DynamicElementInfo ei) {
      super(ei);
    }

    @Override
    public ElementInfo restore (ElementInfo ei){
      DynamicElementInfo dei = (ei != null) ? (DynamicElementInfo) ei : get();
      if (dei == null){
        dei = new DynamicElementInfo();
      }

      super.restore(dei);
      return dei;
    }
  }


  public DynamicElementInfo () {
  }

  public DynamicElementInfo (ClassInfo ci, Fields f, Monitor m, int tid) {
    super(ci, f, m, tid);

    attributes = ci.getElementInfoAttrs();
  }

  public Memento<ElementInfo> getMemento(MementoFactory factory) {
    return factory.getMemento(this);
  }

  public Memento<ElementInfo> getMemento(){
    return new DEIMemento(this);
  }

  @Override
  protected int getNumberOfFieldsOrElements(){
    if (fields instanceof ArrayFields){
      return ((ArrayFields)fields).arrayLength();
    } else {
      return ci.getNumberOfInstanceFields();
    }
  }

  protected void markAreaChanged(){
    JVM.getVM().getHeap().markChanged(index);
  }

  public void setIntField(FieldInfo fi, int value) {
    //checkFieldInfo(fi); // in case somebody caches and uses the wrong FieldInfo

    if (!fi.isReference()) {
      cloneFields().setIntValue( fi.getStorageOffset(), value);
    } else {
      throw new JPFException("reference field: " + fi.getName());
    }
  }

  public int getNumberOfFields () {
    return getClassInfo().getNumberOfInstanceFields();
  }

  public FieldInfo getFieldInfo (int fieldIndex) {
    return getClassInfo().getInstanceField(fieldIndex);
  }

  public ElementInfo getReferencedElementInfo (FieldInfo fi) {
    assert fi.isReference();
    return JVM.getVM().getHeap().get(getIntField(fi));
  }

  public FieldInfo getFieldInfo (String fname) {
    return getClassInfo().getInstanceField(fname);
  }
  protected FieldInfo getDeclaredFieldInfo (String clsBase, String fname) {
    return ClassInfo.getResolvedClassInfo(clsBase).getDeclaredInstanceField(fname);
  }

  protected ElementInfo getElementInfo (ClassInfo ci) {
    // DynamicElementInfo fields are always flattened, so there is no need to
    // look up a Fields container
    return this;
  }

  protected Ref getRef () {
    return new ObjRef(getIndex());
  }

  public ElementInfo getEnclosingElementInfo(){
    for (FieldInfo fi : getClassInfo().getDeclaredInstanceFields()){
      if (fi.getName().startsWith("this$")){
        return getReferencedElementInfo(fi);
      }
    }
    return null;
  }

  public String asString() {
    if (!ClassInfo.isStringClassInfo(ci)) {
      throw new JPFException("object is not of type java.lang.String");
    }

    int vref = getDeclaredReferenceField("value", "java.lang.String");
    int length = getDeclaredIntField("count", "java.lang.String");
    int offset = getDeclaredIntField("offset", "java.lang.String");

    if (vref != -1){
      ElementInfo eVal = JVM.getVM().getHeap().get(vref);
      char[] value = eVal.asCharArray();
      return new String(value, offset, length);
    } else {
      // can happen if 'asString' is called during the String construction itself
      // (e.g. from a careless listener)
      return "";
    }
  }

  /**
   * just a helper to avoid creating objects just for the sake of comparing
   */
  public boolean equalsString (String s) {
    if (!ClassInfo.isStringClassInfo(ci)) {
      return false;
    }

    int vref = getDeclaredReferenceField("value", "java.lang.String");
    int length = getDeclaredIntField("count", "java.lang.String");
    int offset = getDeclaredIntField("offset", "java.lang.String");

    ElementInfo e = JVM.getVM().getHeap().get(vref);
    CharArrayFields cf = (CharArrayFields)e.getFields();

    return cf.asString(offset,length).equals(s);
  }

}
