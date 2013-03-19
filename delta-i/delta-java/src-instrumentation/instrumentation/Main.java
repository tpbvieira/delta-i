package instrumentation;

import japaparser.JavaParser;
import japaparser.ParseException;
import japaparser.ast.CompilationUnit;
import japaparser.ast.visitor.DumpVisitor;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {

  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws Exception {
    String javaFile;
    javaFile = "/home/damorim/workspace/mu-delta/src/delta/instrumentation/Sample.java";
//    javaFile = "/home/damorim/workspace/mu-delta/src/muexamples/bst/original/Node.java";
//    javaFile = "/home/damorim/workspace/mu-delta/src/muexamples/bst/original/BinarySearchTree.java";
    // original compilation unit
    CompilationUnit cu0 = loadUnit(javaFile);
    // check validity
    new CheckerVisitor().main(cu0);
    // encapsulate fields    
    LocalVariableState st = new LocalVariableState();
    new LocalVarsVisitor().main(cu0, st);
    CompilationUnit cu1 = new EncapsulateFieldVisitor().main(cu0, st);
    CompilationUnit cu6 = cu1;
    // getters and setters
//    CompilationUnit cu2 = new AddGetSetVisitor().main(cu1);
//    // change types
//    CompilationUnit cu3 = new TypeVisitor().main(cu2);
//    // change constants
//    CompilationUnit cu4 = new ConstantVisitor().main(cu3);
//    // change arithmetic
//    CompilationUnit cu5 = new ArithmeticVisitor().main(cu4);
//    // branch visitor
//    CompilationUnit cu6 = new BranchVisitor().main(cu5);
    
    
    // final visitor
    CompilationUnit finalUnit = cu6;
    // dumper
    DumpVisitor dumper = new DumpVisitor();
    finalUnit.accept(dumper, null);
    System.out.println(dumper.getSource());      
  }
  
  protected static CompilationUnit loadUnit(String name) throws IOException, ParseException {
    FileInputStream in = new FileInputStream(name);
    CompilationUnit cu;
    try {
      // parse the file
      cu = JavaParser.parse(in);
    } finally {
      in.close();
    }
    return cu;
  }

}