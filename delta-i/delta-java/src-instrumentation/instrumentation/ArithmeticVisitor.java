package instrumentation;

import japaparser.JavaParser;
import japaparser.ParseException;
import japaparser.ast.CompilationUnit;
import japaparser.ast.Node;
import japaparser.ast.expr.BinaryExpr;
import japaparser.ast.expr.Expression;
import japaparser.ast.expr.MethodCallExpr;
import japaparser.ast.visitor.ModifierVisitorAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * visitor to replace important operation on standard
 * semantics with counterpart on delta semantics.  for
 * instance, replace "+" with "add". 
 * 
 * @author damorim
 *
 * @param <A>
 */
public class ArithmeticVisitor<A> extends ModifierVisitorAdapter<A> {
  
  @SuppressWarnings("unchecked")
  public CompilationUnit main(CompilationUnit cu) {
    return (CompilationUnit) cu.accept(this, null);
  }
    
  @SuppressWarnings("unchecked")
  public CompilationUnit main(String compilationUnitFileName) throws IOException, ParseException {
    FileInputStream in = new FileInputStream(compilationUnitFileName);
    CompilationUnit cu;
    try {
      // parse the file
      cu = JavaParser.parse(in);
    } finally {
      in.close();
    }
    return (CompilationUnit) cu.accept(this, null);
  }

  @Override
  public Node visit(BinaryExpr bexp, A arg) {
    Node l = bexp.getLeft().accept(this, arg);
    Node r = bexp.getRight().accept(this, arg);

    Node result;
    switch (bexp.getOperator()) {
    case or:
    case and:
    case binOr:
    case binAnd:
    case xor:
      throw new UnsupportedOperationException("please, implement this operator");
    case equals:
      result = bin((Expression)l, "eq", (Expression)r);
      break;
    case notEquals:
      result = bin((Expression)l, "neq", (Expression)r);
      break;
    case less:
      result = bin((Expression)l, "lt", (Expression)r);
      break;
    case greater:
      result = bin((Expression)l, "gt", (Expression)r);
      break;
    case lessEquals:
      result = bin((Expression)l, "leq", (Expression)r);
      break;
    case greaterEquals:
      result = bin((Expression)l, "geq", (Expression)r);
      break;
    case lShift:
    case rSignedShift:
    case rUnsignedShift:
      throw new UnsupportedOperationException("please, implement this operator");
    case plus:
      result = bin((Expression)l, "add", (Expression)r);
      break;
    case minus:
      result = bin((Expression)l, "sub", (Expression)r);
      break;
    case times:
      result = bin((Expression)l, "mult", (Expression)r);
      break;
    case divide:
      result = bin((Expression)l, "div", (Expression)r);
      break;
    case remainder:
    default : throw new UnsupportedOperationException("please, implement this operator");
    }
    return result;
  }
  
  private Node bin(Expression operator, String op, Expression operand) {
    List<Expression> args = new ArrayList<Expression>();
    args.add(operand);
    return new MethodCallExpr(operator, op , args);
  }
  
}