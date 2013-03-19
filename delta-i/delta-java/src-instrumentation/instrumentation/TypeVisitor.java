package instrumentation;

import japaparser.ast.CompilationUnit;
import japaparser.ast.ImportDeclaration;
import japaparser.ast.Node;
import japaparser.ast.body.FieldDeclaration;
import japaparser.ast.body.VariableDeclarator;
import japaparser.ast.expr.Expression;
import japaparser.ast.expr.IntegerLiteralExpr;
import japaparser.ast.expr.NameExpr;
import japaparser.ast.expr.NullLiteralExpr;
import japaparser.ast.expr.ObjectCreationExpr;
import japaparser.ast.type.ClassOrInterfaceType;
import japaparser.ast.type.PrimitiveType;
import japaparser.ast.type.ReferenceType;
import japaparser.ast.type.Type;
import japaparser.ast.visitor.ModifierVisitorAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * replace types and initializers
 * 
 * @author damorim
 *
 * @param <A>
 */
public class TypeVisitor<A> extends ModifierVisitorAdapter<A> {
    
  @SuppressWarnings("unchecked")
  public CompilationUnit main(CompilationUnit cu) {
    CompilationUnit result = (CompilationUnit) cu.accept(this, null);
    // update imports
    List<ImportDeclaration> imports = result.getImports();
    if (imports == null) {
      imports = new ArrayList<ImportDeclaration>();  
    }
    for (String pack : packages) {
      imports.add(new ImportDeclaration(new NameExpr(pack),false,false));
    }
    result.setImports(imports);
    return result;
  }
  
  private Set<String> packages = new HashSet<String>();
  @SuppressWarnings("unused")
  private void addIfNot(String packageName, String cname) {
    String name = packageName + "." + cname;
    packages.add(name);
  }
  private void addIfNot(String cname) {
    packages.add(cname);
  }
  private static final String dint = "delta.lib.dint.DeltaInt";
  private static final String dboolean = "delta.lib.dboolean.DeltaBoolean";
  
  private boolean isInObjecCreation;
  @Override
  public Node visit(ObjectCreationExpr objCreation, A arg) {
    isInObjecCreation = true;
    Node result = super.visit(objCreation, arg);
    isInObjecCreation = false;
    return result;
  }

  @Override
  public Node visit(ClassOrInterfaceType ciType, A arg) {
    Node result;
    if (isInObjecCreation) {
      result = super.visit(ciType, arg);
    } else {
      result = new ClassOrInterfaceType("DeltaRef<"+ciType.getName()+">");
    }
    return result;
  }
  
  boolean isInFieldDeclaration = false;
  Type tp = null; 
  public Node visit(FieldDeclaration fDec, A arg) {
    isInFieldDeclaration = true;
    tp = fDec.getType();
    Node result = super.visit(fDec, arg);
    tp = null;
    isInFieldDeclaration = false;
    return result;
  }
  
  public Node visit(VariableDeclarator vDec, A arg) {
    if (!isInFieldDeclaration) {
      return super.visit(vDec, arg);
    }
    Expression init = vDec.getInit(); 
    if (init != null) {
      return super.visit(vDec, arg);
    }
    if (tp instanceof PrimitiveType) {
      switch (((PrimitiveType)tp).getType()) {
      case Int:
        init = new IntegerLiteralExpr("0");
        break;
      default:
        throw new UnsupportedOperationException("not yet supported!");
      };
    } else if (tp instanceof ClassOrInterfaceType) {
      init = new NullLiteralExpr();      
    } else if (tp instanceof ReferenceType) {
      init = new NullLiteralExpr();
    } else {
      throw new UnsupportedOperationException("not yet supported" + tp.getClass());
    }    
    vDec.setInit(init);
    return vDec;
  }
  
  @Override
  public Node visit(PrimitiveType fd, A arg) {
    String name;
    switch (fd.getType()) {
    case Int:
      addIfNot(dint);
      name = "DeltaInt";
      break;
    case Boolean:
      addIfNot(dboolean);
      name = "DeltaBoolean";
      break;
    default : throw new UnsupportedOperationException("please, translate this type");
    }
    return new ClassOrInterfaceType(name);
  }
  
}
