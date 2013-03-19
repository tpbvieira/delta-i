package instrumentation;

import japaparser.ast.CompilationUnit;
import japaparser.ast.Node;
import japaparser.ast.body.BodyDeclaration;
import japaparser.ast.body.ClassOrInterfaceDeclaration;
import japaparser.ast.body.FieldDeclaration;
import japaparser.ast.visitor.ModifierVisitorAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddGetSetVisitor<A> extends ModifierVisitorAdapter<A> {
    
  @SuppressWarnings("unchecked")
  public CompilationUnit main(CompilationUnit cu) {
    return (CompilationUnit) cu.accept(this, null);
  }
  
  private Set<FieldDeclaration> fieldDeclarations;
  @SuppressWarnings("unused")
  private void addIfNot(FieldDeclaration field) {
    fieldDeclarations.add(field);
  }  
  
  @Override
  public Node visit(FieldDeclaration fieldDec, A arg) {
    Node result = super.visit(fieldDec, arg);
    addIfNot(fieldDec);
    return result;
  }
  
  public Node visit(ClassOrInterfaceDeclaration n, A arg) {
    fieldDeclarations = new HashSet<FieldDeclaration>();
    ClassOrInterfaceDeclaration result = (ClassOrInterfaceDeclaration) super.visit(n, arg);    
    addSetGet(result.getMembers());
    return result;
  }

  private void addSetGet(List<BodyDeclaration> members) {
    
    for (FieldDeclaration fd : fieldDeclarations) {
      System.err.println("get+set for " + fd);      
    }
    
//    throw new UnsupportedOperationException();
  }
  
}
