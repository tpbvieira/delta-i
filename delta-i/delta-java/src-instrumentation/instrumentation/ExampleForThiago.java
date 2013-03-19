package instrumentation;

import japaparser.ast.CompilationUnit;
import japaparser.ast.Node;
import japaparser.ast.body.ClassOrInterfaceDeclaration;
import japaparser.ast.body.MethodDeclaration;
import japaparser.ast.type.PrimitiveType;
import japaparser.ast.type.PrimitiveType.Primitive;
import japaparser.ast.visitor.DumpVisitor;
import japaparser.ast.visitor.ModifierVisitorAdapter;

public class ExampleForThiago {
  
  public static void main(String[] args) throws Exception {
    String javaFile;
    javaFile = "/home/thiago/java/projects/delta-i/trunk/delta-java/src-instrumentation/instrumentation/SampleForMutation.java";
    // original compilation unit
    CompilationUnit cu0 = Main.loadUnit(javaFile);
    
    // visitor 1: change type
    ChangeTypeVisitor<Void> vis = new ChangeTypeVisitor<Void>();
    cu0.accept(vis, null);
    
    // visitor 2: pretty print
    DumpVisitor dumper = new DumpVisitor();
    cu0.accept(dumper, null);
    System.out.println(dumper.getSource());  
  }
  
  private static class ChangeTypeVisitor<T> extends ModifierVisitorAdapter<T> {
    
    MethodDeclaration md;
    ClassOrInterfaceDeclaration cid;
    
    public Node visit(ClassOrInterfaceDeclaration n, T arg) {
      cid = n;
      return super.visit(n, arg);
    }
    
    public Node visit(MethodDeclaration n, T arg) {
      md = n;
      return super.visit(n, arg);
    }  
    
    public Node visit(PrimitiveType n, T arg) {
      super.visit(n, arg);
     
      
      System.out.println(">>>");
      System.out.printf("class %s\n, method %s\n, line %s\n, column %s\n", 
          cid.getName(), md, 
          "["+ n.getBeginLine() + "," + n.getEndLine() +"]",
          "["+ n.getBeginColumn() + "," + n.getEndColumn() +"]"

          );
      System.out.println("<<<");
      return new PrimitiveType(Primitive.Float);
    }
  }

}
