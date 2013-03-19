package instrumentation;

import japaparser.ast.CompilationUnit;
import japaparser.ast.Node;
import japaparser.ast.body.MethodDeclaration;
import japaparser.ast.expr.AssignExpr;
import japaparser.ast.expr.Expression;
import japaparser.ast.expr.FieldAccessExpr;
import japaparser.ast.expr.MethodCallExpr;
import japaparser.ast.expr.NameExpr;
import japaparser.ast.expr.ThisExpr;
import japaparser.ast.visitor.ModifierVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class EncapsulateFieldVisitor extends ModifierVisitorAdapter<LocalVariableState> {

  @SuppressWarnings("unchecked")
  public CompilationUnit main(CompilationUnit cu, LocalVariableState st) {
    return (CompilationUnit) cu.accept(this, st);
  }
  
  private boolean isInMethod = false;
  @Override
  public Node visit(MethodDeclaration n, LocalVariableState arg) {
    isInMethod = true;
    Node result = super.visit(n, arg);
    isInMethod = false;
    return result;
  }

  @Override
  public Node visit(AssignExpr n, LocalVariableState arg) {
    if (n.getOperator() != AssignExpr.Operator.assign) {
      throw new RuntimeException("not supported.  please, rewrite");
    }
    Expression e1 = n.getTarget();
    Expression e2 = (Expression) n.getValue().accept(this, arg);
    MethodCallExpr result;
    List<Expression> args = new ArrayList<Expression>();
    if (e1 instanceof FieldAccessExpr) {
      FieldAccessExpr fae = (FieldAccessExpr) e1;
      Expression scope = (Expression)fae.getScope().accept(this, arg);
      args.add(scope);
      result = new MethodCallExpr(new ThisExpr(), "set_" + fae.getField(), args);
    } else if (e1 instanceof NameExpr) {
      NameExpr name = (NameExpr) e1;
      if (isLocalAccess(name, arg)) {
        n.setValue(e2);
        return n;
      } else {
        result = new MethodCallExpr(new ThisExpr(), "set_" + name.getName());
      }
    } else {
      throw new UnsupportedOperationException("not yet supported!" + e1.getClass());
    }
    args.add(e2);
    result.setArgs(args);
    return result;
  }

  @Override
  public Node visit(NameExpr n, LocalVariableState arg) {
    Node result;
    String name = n.getName();
    if (isInMethod && !isLocalAccess(n, arg)) {
      result = new MethodCallExpr(new ThisExpr(), "get_" + name, new ArrayList<Expression>());
    } else {
      result = super.visit(n, arg);
    }
    return result;
  }
  
  @Override
  public Node visit(FieldAccessExpr n, LocalVariableState arg) { 
    // format: x.a -> visit(v).get_a();
    FieldAccessExpr fieldAccess = (FieldAccessExpr) super.visit(n, arg);
    String fieldName = fieldAccess.getField();
    fieldAccess.setField("get_" +  fieldName + "()");
    return fieldAccess;
  }
  

  // mock up
  private boolean isLocalAccess(NameExpr nmExpr, LocalVariableState arg) {
    return arg.isLocalAccess(nmExpr);
  }

}