package instrumentation;

import japaparser.ast.body.VariableDeclaratorId;
import japaparser.ast.expr.NameExpr;
import japaparser.ast.expr.QualifiedNameExpr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

//- auxiliary class to hold contextual information during visit ----------------
public class LocalVariableState {

  Set<VariableDeclaratorId> fields = new HashSet<VariableDeclaratorId>();
  Stack<List<VariableDeclaratorId>> locals = new Stack<List<VariableDeclaratorId>>();
  Set<NameExpr> map = new HashSet<NameExpr>();

  void notify(NameExpr nExpr) {
    if (map.contains(nExpr)) return;
    if (nExpr instanceof QualifiedNameExpr) {
      return;
    }
    String name = nExpr.getName();
    for (List<VariableDeclaratorId> l : locals) {
      for (VariableDeclaratorId vd : l) {
        if (vd.getName().equals(name)) {
          map.add(nExpr); // remember that this is a local variable access
          break; 
        }
      }
    }
  }
  boolean isLocalAccess(NameExpr nExpr) {
    return map.contains(nExpr);
  }

  void addLocal(VariableDeclaratorId n) {
    locals.peek().add(n);
  }
  void entryBlock() {
    locals.push(new ArrayList<VariableDeclaratorId>());
  }
  void exitBlock() {
    locals.pop();
  }
  void check() { // expected to be called only from MethodDeclaration
    assert(locals.empty());
  }
  public void addField(VariableDeclaratorId n) {
    fields.add(n);
  }
}

