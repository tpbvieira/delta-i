package instrumentation;

import japaparser.ast.CompilationUnit;
import japaparser.ast.Node;
import japaparser.ast.expr.BooleanLiteralExpr;
import japaparser.ast.expr.Expression;
import japaparser.ast.expr.FieldAccessExpr;
import japaparser.ast.expr.IntegerLiteralExpr;
import japaparser.ast.expr.MethodCallExpr;
import japaparser.ast.expr.NameExpr;
import japaparser.ast.expr.NullLiteralExpr;
import japaparser.ast.expr.ObjectCreationExpr;
import japaparser.ast.visitor.ModifierVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ConstantVisitor<A> extends ModifierVisitorAdapter<A> {
  
  @SuppressWarnings("unchecked")
  public CompilationUnit main(CompilationUnit cu) {
    return (CompilationUnit) cu.accept(this, null);
  }

  @Override
  public Node visit(NullLiteralExpr nullLit, A arg) {
//    Expression scope = new NameExpr("DeltaFactory");
//    List<Expression> args = new ArrayList<Expression>();
//    args.add(nullLit);
//    return new MethodCallExpr(scope,"createConstant", args);
    return new FieldAccessExpr(new NameExpr("DeltaRef"),"NULL");    
  }
  
  @Override
  public Node visit(IntegerLiteralExpr intLit, A arg) {
    if (intLit.getValue().trim().equals("0")) {
      return new FieldAccessExpr(new NameExpr("DeltaInt"),"ZERO");  
    } else {
      Expression scope = new NameExpr("DeltaIntCommon");
      List<Expression> args = new ArrayList<Expression>();
      args.add(intLit);
      return new MethodCallExpr(scope,"createConstant", args);      
    }
  }
  
  @Override
  public Node visit(BooleanLiteralExpr boolLit, A arg) {
    String token = boolLit.getValue() ? "TRUE" : "FALSE";
    return new FieldAccessExpr(new NameExpr("DeltaBoolean"),token);
  }
  
  @Override
  public Node visit(ObjectCreationExpr objCreation, A arg) {
    Expression scope = new NameExpr("Deltafactory");
    List<Expression> args = new ArrayList<Expression>();
    args.add(objCreation);
    return new MethodCallExpr(scope,"createConstant", args);
  }
  
}
