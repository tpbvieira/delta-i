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
package gov.nasa.jpf.test.vm.basic;

import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.jvm.AnnotationInfo;
import gov.nasa.jpf.jvm.FieldInfo;
import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.jvm.bytecode.GETFIELD;
import gov.nasa.jpf.jvm.bytecode.Instruction;
import java.lang.annotation.*;
import gov.nasa.jpf.util.test.TestJPF;
import java.lang.reflect.*;
import org.junit.Test;


public class AnnotationTest extends TestJPF {

  @Test //----------------------------------------------------------------------
  @A1("foo")
  public void testStringValueOk () {
    if (verifyNoPropertyViolation()) {
      try {
        java.lang.reflect.Method method =
                AnnotationTest.class.getMethod("testStringValueOk");
        A1 annotation = method.getAnnotation(A1.class);

        assert ("foo".equals(annotation.value()));
        
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface A1 {
    String value();
  }


  @Test //----------------------------------------------------------------------
  @A2({"foo", "boo"})
  public void testStringArrayValueOk () {
    if (verifyNoPropertyViolation()) {
      try {
        java.lang.reflect.Method method =
                AnnotationTest.class.getMethod("testStringArrayValueOk");
        A2 annotation = method.getAnnotation(A2.class);

        Object v = annotation.value();
        assert v instanceof String[];

        String[] a = (String[])v;
        assert a.length == 2;

        assert "foo".equals(a[0]);
        assert "boo".equals(a[1]);

      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface A2 {
    String[] value();
  }

  @Test //----------------------------------------------------------------------
  @A3(Long.MAX_VALUE)
  public void testLongValueOk () {
    if (verifyNoPropertyViolation()) {
      try {
        java.lang.reflect.Method method =
                AnnotationTest.class.getMethod("testLongValueOk");
        A3 annotation = method.getAnnotation(A3.class);

        assert (annotation.value() == Long.MAX_VALUE);
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface A3 {
    long value();
  }


  @Test //----------------------------------------------------------------------
  @A4(a="one",b=42.0)
  public void testNamedParamsOk () {
    if (verifyNoPropertyViolation()) {
      try {
        java.lang.reflect.Method method =
                AnnotationTest.class.getMethod("testNamedParamsOk");
        A4 annotation = method.getAnnotation(A4.class);

        assert ("one".equals(annotation.a()));
        assert ( 42.0 == annotation.b());

        System.out.println(annotation);

      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface A4 {
    String a();
    double b();
  }


  @Test //----------------------------------------------------------------------
  @A5(b="foo")
  public void testPartialDefaultParamsOk () {
    if (verifyNoPropertyViolation()) {
      try {
        java.lang.reflect.Method method =
                AnnotationTest.class.getMethod("testPartialDefaultParamsOk");
        A5 annotation = method.getAnnotation(A5.class);

        assert ("whatever".equals(annotation.a()));

        System.out.println(annotation);

      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface A5 {
    String a() default "whatever";
    String b();
  }

  @Test //----------------------------------------------------------------------
  @A6
  public void testSingleDefaultParamOk () {
    if (verifyNoPropertyViolation()) {
      try {
        java.lang.reflect.Method method =
                AnnotationTest.class.getMethod("testSingleDefaultParamOk");
        A6 annotation = method.getAnnotation(A6.class);
        
        assert ("whatever".equals(annotation.value()));

        System.out.println(annotation);

      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface A6 {
    String value() default "whatever";
  }

  @A6
  @Test
  public void testAnnotationClass() throws ClassNotFoundException, NoSuchMethodException {
    if (verifyNoPropertyViolation()) { 
      Class clazz = Class.forName("gov.nasa.jpf.test.vm.basic.AnnotationTest");
      Method method = clazz.getDeclaredMethod("testAnnotationClass");
      Annotation annotations[] = method.getAnnotations();
      
      for (int i=0; i<annotations.length; i++){
        System.out.printf("  a[%d] = %s\n", i, annotations[i].toString());
      }
      
      assertEquals(2, annotations.length);
      assertNotNull(annotations[0]);
      assertNotNull(annotations[1]);      

      assertTrue(annotations[0] instanceof A6);
      assertTrue(annotations[1] instanceof Test);
    }
  }

  //--------------------------------------------------------------------

  public enum MyEnum {
    ONE, TWO
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface A7 {
    MyEnum value();
  }

  @Test
  @A7(MyEnum.ONE)
  public void testEnumValue() throws ClassNotFoundException, NoSuchMethodException {
    if (verifyNoPropertyViolation()){
      Class clazz = Class.forName("gov.nasa.jpf.test.vm.basic.AnnotationTest");  // Any class outside of this file will do.
      Method method = clazz.getDeclaredMethod("testEnumValue");               // Any method with an annotation will do.
      Annotation annotations[] = method.getAnnotations();

      assertEquals(2, annotations.length);
      assertNotNull(annotations[1]);

      assertTrue(annotations[1] instanceof A7);
      A7 ann = (A7)annotations[1];
      assertTrue( ann.value() == MyEnum.ONE);
    }
  }

  //--------------------------------------------------------------------

  @Retention(RetentionPolicy.RUNTIME)
  @interface A8 {
    Class value();
  }

  @Test
  @A8(AnnotationTest.class)
  public void testClassValue() throws ClassNotFoundException, NoSuchMethodException {
    if (verifyNoPropertyViolation()){
      Class clazz = Class.forName("gov.nasa.jpf.test.vm.basic.AnnotationTest");  // Any class outside of this file will do.
      Method method = clazz.getDeclaredMethod("testClassValue");               // Any method with an annotation will do.
      Annotation annotations[] = method.getAnnotations();

      assertEquals(2, annotations.length);
      assertNotNull(annotations[1]);

      assertTrue(annotations[1] instanceof A8);
      A8 ann = (A8)annotations[1];
      assertTrue( ann.value() == AnnotationTest.class);
    }
  }

  //-------------------------------------------------------------------
  static class MyClass {
    @A1("the answer")
    int data = 42;
  }
  
  public static class DataListener extends ListenerAdapter {
    public void executeInstruction(JVM vm){
      Instruction insn = vm.getLastInstruction();
      if (insn instanceof GETFIELD){
        FieldInfo fi = ((GETFIELD)insn).getFieldInfo();
        if (fi.getName().equals("data")){
          AnnotationInfo ai = fi.getAnnotation("gov.nasa.jpf.test.vm.basic.AnnotationTest$A1");
          System.out.println("annotation for " + fi.getFullName() + " = " + ai);
          
          if (ai != null){
            String val = ai.getValueAsString("value");
            System.out.println("   value = " + val);
            
            if (val == null || !val.equals("the answer")){
              fail("wrong @A1 value = " + val);
            }
          } else {
            fail("no @A1 annotation for field " + fi.getFullName());
          }
        }
      }
    }
  }
  
  @Test
  public void testFieldAnnotation(){
    if (verifyNoPropertyViolation("+listener=.test.vm.basic.AnnotationTest$DataListener")){
      MyClass obj = new MyClass();
      int d = obj.data;
    }
  }
}
