package instrumentation;

import japaparser.ast.CompilationUnit;
import japaparser.ast.Node;
import japaparser.ast.expr.Expression;
import japaparser.ast.expr.MethodCallExpr;
import japaparser.ast.stmt.ForStmt;
import japaparser.ast.stmt.IfStmt;
import japaparser.ast.stmt.WhileStmt;
import japaparser.ast.visitor.ModifierVisitorAdapter;

import java.util.ArrayList;

/**
 * add eval in the context of an if statement (elseif) or 
 * loop (for, while) 
 * 
 * @author damorim
 *
 * @param <A>
 */
public class BranchVisitor<A> extends ModifierVisitorAdapter<A> {
  
  @SuppressWarnings("unchecked")
  public CompilationUnit main(CompilationUnit cu) {
    return (CompilationUnit) cu.accept(this, null);
  }
    
  @Override
  public Node visit(IfStmt n, A arg) {
    IfStmt ifNode = (IfStmt) super.visit(n, arg);
    Expression exp = eval(ifNode.getCondition());
    ifNode.setCondition(exp);
    return ifNode;
  }
  
  @Override
  public Node visit(WhileStmt n, A arg) {
    WhileStmt whileNode = (WhileStmt) super.visit(n, arg);
    Expression exp = eval(whileNode.getCondition());
    whileNode.setCondition(exp);
    return whileNode;    
  }
  
  
  @Override
  public Node visit(ForStmt n, A arg) {
    ForStmt forStmt = (ForStmt) super.visit(n, arg);
    Expression exp = eval(forStmt.getCompare());
    forStmt.setCompare(exp);
    return forStmt;   
  }
  
  private MethodCallExpr eval(Expression context) {
    return new MethodCallExpr(context, "eval", new ArrayList<Expression>());
  }
  
}