package instrumentation;

import japaparser.ast.CompilationUnit;
import japaparser.ast.body.FieldDeclaration;
import japaparser.ast.body.MethodDeclaration;
import japaparser.ast.body.VariableDeclaratorId;
import japaparser.ast.expr.NameExpr;
import japaparser.ast.expr.QualifiedNameExpr;
import japaparser.ast.stmt.BlockStmt;
import japaparser.ast.stmt.SwitchEntryStmt;
import japaparser.ast.visitor.GenericVisitorAdapter;

public class LocalVarsVisitor<R> extends GenericVisitorAdapter <R, LocalVariableState> {

  @SuppressWarnings("unchecked")
  public CompilationUnit main(CompilationUnit cu, LocalVariableState state) {
    return (CompilationUnit) cu.accept(this, state);
  }

  private boolean isMethodBody;
  public R visit(MethodDeclaration n, LocalVariableState arg) {
    arg.check(); // stack should be clear
    isMethodBody = true;
    arg.entryBlock();
    R result = super.visit(n, arg);
    arg.exitBlock();
    isMethodBody = false;
    return result;
  }

  private boolean isFieldDec;
  public R visit(FieldDeclaration n, LocalVariableState arg) {
    isFieldDec = true;
    R result = super.visit(n, arg);
    isFieldDec = false;
    return result;
  }
  
  public R visit(VariableDeclaratorId n, LocalVariableState arg) {
    if (isFieldDec) {
      arg.addField(n);
    } else {
      arg.addLocal(n);
    }
    return super.visit(n, arg);    
  }

  //- Expression ----------------------------------------

  public R visit(NameExpr n, LocalVariableState arg) {
    if (isMethodBody) {
      arg.notify(n);
    }
    return super.visit(n, arg);
  }

  public R visit(QualifiedNameExpr n, LocalVariableState arg) {
    if (isMethodBody) {
      arg.notify(n);
    }
    return super.visit(n, arg);
  }

  //- Statements ----------------------------------------

  // only two statements declare List<Statement>

  public R visit(BlockStmt n, LocalVariableState arg) {
    arg.entryBlock();
    R result = super.visit(n, arg);
    arg.exitBlock();
    return result;
  }

  public R visit(SwitchEntryStmt n, LocalVariableState arg) {
    arg.entryBlock();
    R result = super.visit(n, arg);
    arg.exitBlock();
    return result;
  }

}