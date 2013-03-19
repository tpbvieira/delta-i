package instrumentation;

import japaparser.ParseException;
import japaparser.ast.CompilationUnit;
import japaparser.ast.expr.UnaryExpr;
import japaparser.ast.expr.UnaryExpr.Operator;
import japaparser.ast.visitor.GenericVisitorAdapter;

import java.io.IOException;

public class CheckerVisitor<R, A> extends GenericVisitorAdapter<R, A> {

  
  @SuppressWarnings("unchecked")
  public CompilationUnit main(CompilationUnit cu) throws IOException, ParseException {
    return (CompilationUnit) cu.accept(this, null);
  }

  
  @Override
  public R visit(UnaryExpr n, A arg) {
    Operator op = n.getOperator();
    if (op == Operator.posIncrement || op == Operator.posDecrement ||
        op == Operator.preIncrement || op == Operator.preDecrement) {
      throw new UnsupportedOperationException("not supported! "+ n + " please rewrite code manually.");
    }
    return super.visit(n,arg);
  }

  
}
