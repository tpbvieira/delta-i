package instrumentation;

import japaparser.ast.CompilationUnit;
import japaparser.ast.ImportDeclaration;
import japaparser.ast.Node;
import japaparser.ast.body.VariableDeclarator;
import japaparser.ast.body.VariableDeclaratorId;
import japaparser.ast.expr.AssignExpr;
import japaparser.ast.expr.BinaryExpr;
import japaparser.ast.expr.BooleanLiteralExpr;
import japaparser.ast.expr.Expression;
import japaparser.ast.expr.IntegerLiteralExpr;
import japaparser.ast.expr.MethodCallExpr;
import japaparser.ast.expr.NameExpr;
import japaparser.ast.expr.QualifiedNameExpr;
import japaparser.ast.expr.VariableDeclarationExpr;
import japaparser.ast.stmt.BlockStmt;
import japaparser.ast.stmt.DoStmt;
import japaparser.ast.stmt.EmptyStmt;
import japaparser.ast.stmt.ExpressionStmt;
import japaparser.ast.stmt.ForStmt;
import japaparser.ast.stmt.IfStmt;
import japaparser.ast.stmt.Statement;
import japaparser.ast.stmt.SwitchStmt;
import japaparser.ast.stmt.WhileStmt;
import japaparser.ast.type.ClassOrInterfaceType;
import japaparser.ast.type.PrimitiveType;
import japaparser.ast.type.PrimitiveType.Primitive;
import japaparser.ast.type.ReferenceType;
import japaparser.ast.type.Type;
import japaparser.ast.visitor.DumpVisitor;
import japaparser.ast.visitor.ModifierVisitorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import util.MutationDummy;
import de.unisb.cs.st.javalanche.mutation.results.Mutation;
import de.unisb.cs.st.javalanche.mutation.results.Mutation.MutationType;

public class MutationVisitor {

  private static Map<String, Primitive> changedVariables = new LinkedHashMap<String, Primitive>();
  private static Map<Class, Integer> numberMutations = new HashMap<Class, Integer>();
  private static long varNumber = 0;

  public static void main(String[] args) throws Exception {

    String source = "/home/thiago/java/projects/delta-i/trunk/delta-java/src-instrumentation/instrumentation/SampleForMutation.java";
    CompilationUnit unit = Main.loadUnit(source);

    // visitor 1: change type
    MutatorVisitor<Void> visitor = new MutatorVisitor<Void>();
    unit.accept(visitor, null);

    // visitor 2: pretty print
    DumpVisitor dumper = new DumpVisitor();
    unit.accept(dumper, null);
    System.out.println(dumper.getSource());
  }

  private static class MutatorVisitor<T> extends ModifierVisitorAdapter<T> {

    /**
     * Instumentation to inset package import
     */
    public Node visit(CompilationUnit n, T arg) {
      ImportDeclaration importDec1 = new ImportDeclaration(
          new QualifiedNameExpr(new QualifiedNameExpr(new NameExpr("delta"),
          "statemask"), "StateMask"), false, false);
      ImportDeclaration importDec2 = new ImportDeclaration(
          new QualifiedNameExpr(new QualifiedNameExpr(new NameExpr("delta"),
          "util"), "Common"), false, false);
      ImportDeclaration importDec3 = new ImportDeclaration(
          new QualifiedNameExpr(new NameExpr("deltalib"), "General"), false,
          false);
      ImportDeclaration importDec4 = new ImportDeclaration(
          new QualifiedNameExpr(new QualifiedNameExpr(new NameExpr("deltalib"),
          "dint"), "DeltaInt"), false, false);
      ImportDeclaration importDec5 = new ImportDeclaration(
          new QualifiedNameExpr(new QualifiedNameExpr(new NameExpr("deltalib"),
          "dint"), "DeltaIntArray"), false, false);

      importDec1.accept(this, arg);
      importDec2.accept(this, arg);
      importDec3.accept(this, arg);
      importDec4.accept(this, arg);
      importDec5.accept(this, arg);

      List<ImportDeclaration> imports = n.getImports();
      if (imports == null) {
        imports = new ArrayList<ImportDeclaration>();
        n.setImports(imports);
      }

      imports.add(importDec1);
      imports.add(importDec2);
      imports.add(importDec3);
      imports.add(importDec4);
      imports.add(importDec5);

      // saving number of mutatios of this class
      int numMutations = MutationDummy.getNumberMutations(this.getClass());
      numberMutations.put(getClass(), numMutations);

      return super.visit(n, arg);
    }

    public Node visit(BlockStmt n, T arg) {

      List<ExpressionStmt> listNewExpStmt = new ArrayList<ExpressionStmt>();
      List<IfStmt> listNewIfStmt = new ArrayList<IfStmt>();
      boolean replaceStatement = false;

      List<Statement> stmts = n.getStmts();

      if (stmts != null) {
        for (int i = 0; i < stmts.size(); i++) {
          Statement stm = stmts.get(i);

          if(stm instanceof ExpressionStmt){

            Expression exp = ((ExpressionStmt)stm).getExpression();

            if(exp instanceof VariableDeclarationExpr){

              VariableDeclarationExpr varDec = (VariableDeclarationExpr)exp;
              replaceStatement = false;
              varDecHandle(varDec,listNewExpStmt);

            }else if(exp instanceof AssignExpr){

              AssignExpr assign = (AssignExpr)exp;
              replaceStatement = assignHandle(assign, listNewExpStmt);

            }

          }else if(stm instanceof IfStmt){

            IfStmt ifStmt = (IfStmt)stm;
            Expression exp = ifStmt.getCondition();

            if(exp instanceof BinaryExpr){

              replaceStatement = true;
              BinaryExpr condition = (BinaryExpr)exp;
              MethodCallExpr exprMethod = binaryExprHandle(condition, listNewExpStmt);
              MethodCallExpr splitMethod = generateSplitMethod(exprMethod);
              ifStmt.setCondition(splitMethod);

            }else if(exp instanceof MethodCallExpr){
              //TODO
            }else if(exp instanceof BooleanLiteralExpr){
              //TODO
            }

            listNewIfStmt.add(ifStmt);

          }else if(stm instanceof ForStmt){
            //TODO
          }else if(stm instanceof WhileStmt){
            //TODO
          }else if(stm instanceof SwitchStmt){
            //TODO
          }else if(stm instanceof DoStmt){
            //TODO
          }

          //putting statements to be inserted on code
          if(replaceStatement){
            stmts.set(i, (Statement) new EmptyStmt().accept(this, arg)); 
          }else{
            stmts.set(i, (Statement) stmts.get(i).accept(this, arg));
          }
          replaceStatement = false;


          if(listNewExpStmt.size() > 0){
            for (ExpressionStmt expressionStmt : listNewExpStmt) {
              stmts.add(++i,(Statement) expressionStmt.accept(this, arg));
            }
            listNewExpStmt.clear();
          }


          if(listNewIfStmt.size() > 0){
            for (IfStmt ifStmt : listNewIfStmt) {
              stmts.add(++i,(Statement) ifStmt.accept(this, arg));
            }
            listNewIfStmt.clear();
          }

        }

        removeNulls(stmts);

      }

      return n;
    }

    private MethodCallExpr binaryExprHandle(BinaryExpr binExp, List<ExpressionStmt> listNewExpStmt) {

      Expression expLeft = binExp.getLeft();
      Expression expRight = binExp.getRight();

      if(expLeft instanceof IntegerLiteralExpr){
        binExp.setLeft(integerLiteralExprHandle((IntegerLiteralExpr)expLeft, listNewExpStmt));
      }else if(expLeft instanceof BinaryExpr){
        binExp.setLeft(binaryExprHandle((BinaryExpr)expLeft, listNewExpStmt));
      }

      if (expRight instanceof IntegerLiteralExpr){
        binExp.setRight(integerLiteralExprHandle((IntegerLiteralExpr)expRight, listNewExpStmt));
      }else if(expRight instanceof BinaryExpr){
        binExp.setRight(binaryExprHandle((BinaryExpr)expRight, listNewExpStmt));
      }

      return generateDeltaMethod(binExp.getLeft(), binExp.getOperator(), binExp.getRight());

    }

    private NameExpr integerLiteralExprHandle(IntegerLiteralExpr intValue, List<ExpressionStmt> listNewExpStmt) {

      List<Mutation> mutations = MutationDummy.getMutations(getClass(), intValue.getEndLine());
      NameExpr varId = null;

      if(mutations != null){

        VariableDeclarationExpr varDec = generateDeltaIntDeclaration(intValue);
        listNewExpStmt.add(new ExpressionStmt(varDec));

        for (Mutation mutation : mutations) {
          listNewExpStmt.add(new ExpressionStmt(generateSetValueAt(new NameExpr(varDec.getVars().get(0).getId().getName()),
              new IntegerLiteralExpr(String.valueOf(mutation.getId())), 
              getMutationIntValue(mutation,intValue))));
        }
        //new value for constant
        varId = new NameExpr(varDec.getVars().get(0).getId().getName());

      }else{

        VariableDeclarationExpr varDec = generateDeltaIntConstantDeclaration(intValue);
        listNewExpStmt.add(new ExpressionStmt(varDec));
        varId = new NameExpr(varDec.getVars().get(0).getId().getName());

      }

      return varId;
    }

    private VariableDeclarationExpr generateDeltaIntDeclaration(
        Expression exp) {
      ReferenceType deltaType = new ReferenceType(new ClassOrInterfaceType("DeltaInt"));
      VariableDeclaratorId id = new VariableDeclaratorId("VAR"+ varNumber++);
      MethodCallExpr initDeclaration = generateCreate(new IntegerLiteralExpr(String.valueOf(numberMutations.get(getClass()))), exp);
      VariableDeclarator varDeclarator = new VariableDeclarator(id, initDeclaration);
      List<VariableDeclarator> vars = new ArrayList<VariableDeclarator>();
      vars.add(varDeclarator);
      VariableDeclarationExpr varDec = new VariableDeclarationExpr(deltaType, vars);

      changedVariables.put(id.getName(), Primitive.Int);

      return varDec;
    }

    private VariableDeclarationExpr generateDeltaIntConstantDeclaration(
        Expression exp) {
      ReferenceType deltaType = new ReferenceType(new ClassOrInterfaceType("DeltaInt"));
      VariableDeclaratorId id = new VariableDeclaratorId("VAR"+ varNumber++);
      MethodCallExpr initDeclaration = generateCreateConstant((IntegerLiteralExpr)exp);
      VariableDeclarator varDeclarator = new VariableDeclarator(id, initDeclaration);
      List<VariableDeclarator> vars = new ArrayList<VariableDeclarator>();
      vars.add(varDeclarator);
      VariableDeclarationExpr varDec = new VariableDeclarationExpr(deltaType, vars);
      return varDec;
    }

    private boolean assignHandle(AssignExpr assign,
        List<ExpressionStmt> listNewExpStmt) {

      ExpressionStmt newMethod = null;
      boolean replaceStatement = false;
      int newMethodPosition = 0;
      Expression target = assign.getTarget();
      Expression value = assign.getValue();

      if (target instanceof NameExpr && changedVariables.get(((NameExpr) target).getName()) != null) {

        AssignExpr.Operator op = assign.getOperator();

        switch (op) {
        case assign:
          newMethod = new ExpressionStmt(generateSetValue(assign.getTarget(), null));
          replaceStatement = true;
          listNewExpStmt.add(newMethod);
          newMethodPosition = listNewExpStmt.size() - 1; 
          break;
        case plus:
          //TODO
          break;
        case minus:
          //TODO
          break;
        case star:
          //TODO
          break;
        case slash:
          //TODO
          break;
        case and:
          //TODO
          break;
        case or:
          //TODO
          break;
        case xor:
          //TODO
          break;
        case rem:
          //TODO
          break;
        case lShift:
          //TODO
          break;
        case rSignedShift:
          //TODO
          break;
        case rUnsignedShift:
          //TODO
          break;

        default:
          break;
        }


        if (value instanceof IntegerLiteralExpr) {
          // verificar se no value da atribuição há mutação, caso haja, devem haver os setValueAt correspondentes

          IntegerLiteralExpr intValue = (IntegerLiteralExpr) value;
          ((MethodCallExpr)newMethod.getExpression()).getArgs().set(newMethodPosition, intValue);          

          List<Mutation> mutations = MutationDummy.getMutations(
              this.getClass(), intValue.getEndLine());

          if (mutations != null) {

            for (Mutation mutation : mutations) {
              MethodCallExpr methodCall = generateSetValueAt(
                  assign.getTarget(),
                  new IntegerLiteralExpr(String.valueOf(mutation.getId())),
                  getMutationIntValue(mutation,intValue));
              listNewExpStmt.add(new ExpressionStmt(methodCall));
            }

          } else {// set constant value into array
            
            MethodCallExpr methodCall = generateCreateConstant(intValue);
            assign.setValue(methodCall);
            
          }

        }else if(value instanceof MethodCallExpr){
          // se o value da atribuição for uma função, verificar se há expressões nos parâmetros e como pode ser aplicada mutação a elas
          // tratar o escopo das variaveis, como saber a que escopo pertence cada variavel?

          ((MethodCallExpr)newMethod.getExpression()).getArgs().set(newMethodPosition, value);

        }else if(value instanceof BinaryExpr){

          ((MethodCallExpr)newMethod.getExpression()).getArgs().set(newMethodPosition, binaryExprHandle((BinaryExpr)value, listNewExpStmt));

        }

      }

      return replaceStatement;
    }

    private void varDecHandle(VariableDeclarationExpr varDec,
        List<ExpressionStmt> listNewExpStmt) {

      Type type = varDec.getType();
      if (type instanceof PrimitiveType) {

        if (((PrimitiveType) type).getType() == Primitive.Int) {

          // change type to DeltaInt
          ReferenceType deltaType = new ReferenceType(new ClassOrInterfaceType("DeltaInt"));
          varDec.setType(deltaType);

          int currLine = 0;
          int mutIndex = 0;
          List<Mutation> mutations = new ArrayList<Mutation>();
          List<VariableDeclarator> vars = varDec.getVars();
          // For each variable, change variable initialization
          for (VariableDeclarator variableDeclarator : vars) {

            changedVariables.put(variableDeclarator.getId().getName(), Primitive.Int);

            String defaultValue;
            IntegerLiteralExpr value = (IntegerLiteralExpr) variableDeclarator
            .getInit();
            if (value != null) {
              defaultValue = value.getValue();

              if (currLine != value.getEndLine()) {
                mutations = MutationDummy.getMutations(this.getClass(),
                    value.getBeginLine());
                currLine = value.getEndLine();
              }

              if (mutations == null) {

                MethodCallExpr methodCall = generateCreateConstant(value);
                variableDeclarator.setInit(methodCall);

              } else {

                int index = 0;
                for (Mutation mutation : mutations) {

                  if (mutation.getMutationForLine() <= mutIndex) {
                    mutIndex = 0;
                    break;
                  } else {
                    mutIndex = mutation.getMutationForLine();
                  }

                  MethodCallExpr SetValueAtMethod = generateSetValueAt(
                      new NameExpr(variableDeclarator.getId().getName()),
                      new IntegerLiteralExpr(String.valueOf(mutation.getId())),
                      getMutationIntValue(mutation,value));
                  listNewExpStmt.add(new ExpressionStmt(SetValueAtMethod));
                  index++;

                  // creating DeltaArray initialization method
                  MethodCallExpr createMethod = generateCreate(
                      new IntegerLiteralExpr(String.valueOf(numberMutations
                          .get(this.getClass()))), new IntegerLiteralExpr(
                              defaultValue));
                  variableDeclarator.setInit(createMethod);
                }

                if (mutations.size() > 0) {
                  for (int j = 0; j < index; j++) {
                    mutations.remove(0);
                  }
                }

              }

            } else {
              defaultValue = "0";
              // creating DeltaArray initialization method
              MethodCallExpr createMethod = generateCreate(
                  new IntegerLiteralExpr(String.valueOf(numberMutations
                      .get(this.getClass()))), new IntegerLiteralExpr(
                          defaultValue));
              variableDeclarator.setInit(createMethod);
            }
          }

        } else if (((PrimitiveType) type).getType() == Primitive.Boolean) {
          // TODO
        }

      }

    }

    private MethodCallExpr generateSetValue(Expression scope, IntegerLiteralExpr value) {
      MethodCallExpr methodCall = new MethodCallExpr(scope, "setValue");
      List<Expression> args = new LinkedList<Expression>();
      args.add(value);
      methodCall.setArgs(args);
      return methodCall;
    }

    private MethodCallExpr generateSetValueAt(Expression scope,
        IntegerLiteralExpr index, IntegerLiteralExpr value) {
      MethodCallExpr methodCall = new MethodCallExpr(scope, "setValueAt");
      List<Expression> args = new LinkedList<Expression>();
      args.add(index);
      args.add(value);
      methodCall.setArgs(args);
      return methodCall;
    }

    private MethodCallExpr generateCreateConstant(IntegerLiteralExpr intValue) {
      MethodCallExpr methodCall = new MethodCallExpr(new NameExpr(
      "DeltaIntArray"), "createConstant");
      List<Expression> args = new LinkedList<Expression>();
      args.add(intValue);
      methodCall.setArgs(args);
      return methodCall;
    }

    private static MethodCallExpr generateCreate(Expression size,
        Expression value) {
      MethodCallExpr createMethod = new MethodCallExpr(new NameExpr(
      "DeltaIntArray"), "create");
      List<Expression> argsCreate = new LinkedList<Expression>();
      argsCreate.add(size);
      argsCreate.add(value);
      createMethod.setArgs(argsCreate);
      return createMethod;
    }

    private static MethodCallExpr generateSplitMethod(Expression exp){
      MethodCallExpr methodCall = new MethodCallExpr(new NameExpr("General"),"split");
      List<Expression> args = new LinkedList<Expression>();
      methodCall.setArgs(args);
      args.add(exp);
      return methodCall;
    }

    private static MethodCallExpr generateDeltaMethod(Expression left,
        BinaryExpr.Operator op, Expression right) {
      String method = null;

      switch (op) {
      case or:
        //TODO
        break;
      case and:
        //TODO
        break;
      case binOr:
        //TODO
        break;
      case binAnd:
        //TODO
        break;
      case xor:
        //TODO
        break;
      case equals:
        method = "eq_int";
        break;
      case notEquals:
        method = "neq_int";
        break;
      case less:
        method = "lt";
        break;
      case greater:
        method = "gt";
        break;
      case lessEquals:
        method = "leq";
        break;
      case greaterEquals:
        method = "geq";
        break;
      case lShift:
        //TODO
        break;
      case rSignedShift:
        //TODO
        break;
      case rUnsignedShift:
        //TODO
        break;
      case plus:
        method = "add";
        break;
      case minus:
        method = "sub";
        break;
      case times:
        method = "mult";
        break;
      case divide:
        method = "div";
        break;
      case remainder:
        //TODO
        break;

      default:
        //TODO
        break;
      }

      MethodCallExpr methodCall = new MethodCallExpr(new NameExpr("General"),
          method);
      List<Expression> args = new LinkedList<Expression>();
      methodCall.setArgs(args);

      args.add(left);
      args.add(right);

      return methodCall;
    }

    private static IntegerLiteralExpr getMutationIntValue(Mutation mutation, IntegerLiteralExpr oldValue) {
      int var = Integer.parseInt(oldValue.getValue());
      MutationType type = mutation.getMutationType();

      switch (type) {
      case RIC_MINUS_1:
        var--;
        break;
      case RIC_PLUS_1:
        var++;
        break;
      case RIC_ZERO:
        var = 0;
        break;
      default:
        throw new RuntimeException("MutationType unexpected");
      }

      return new IntegerLiteralExpr(String.valueOf(var));
    }
  }

  private static void removeNulls(List<?> list) {
    for (int i = list.size() - 1; i >= 0; i--) {
      if (list.get(i) == null) {
        list.remove(i);
      }
    }
  }

}