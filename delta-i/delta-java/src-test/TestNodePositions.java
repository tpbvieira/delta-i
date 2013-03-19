/*
 * Created on 30/06/2008
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import japaparser.ast.BlockComment;
import japaparser.ast.CompilationUnit;
import japaparser.ast.ImportDeclaration;
import japaparser.ast.LineComment;
import japaparser.ast.Node;
import japaparser.ast.PackageDeclaration;
import japaparser.ast.TypeParameter;
import japaparser.ast.body.AnnotationDeclaration;
import japaparser.ast.body.AnnotationMemberDeclaration;
import japaparser.ast.body.ClassOrInterfaceDeclaration;
import japaparser.ast.body.ConstructorDeclaration;
import japaparser.ast.body.EmptyMemberDeclaration;
import japaparser.ast.body.EmptyTypeDeclaration;
import japaparser.ast.body.EnumConstantDeclaration;
import japaparser.ast.body.EnumDeclaration;
import japaparser.ast.body.FieldDeclaration;
import japaparser.ast.body.InitializerDeclaration;
import japaparser.ast.body.JavadocComment;
import japaparser.ast.body.MethodDeclaration;
import japaparser.ast.body.Parameter;
import japaparser.ast.body.VariableDeclarator;
import japaparser.ast.body.VariableDeclaratorId;
import japaparser.ast.expr.ArrayAccessExpr;
import japaparser.ast.expr.ArrayCreationExpr;
import japaparser.ast.expr.ArrayInitializerExpr;
import japaparser.ast.expr.AssignExpr;
import japaparser.ast.expr.BinaryExpr;
import japaparser.ast.expr.BooleanLiteralExpr;
import japaparser.ast.expr.CastExpr;
import japaparser.ast.expr.CharLiteralExpr;
import japaparser.ast.expr.ClassExpr;
import japaparser.ast.expr.ConditionalExpr;
import japaparser.ast.expr.DoubleLiteralExpr;
import japaparser.ast.expr.EnclosedExpr;
import japaparser.ast.expr.FieldAccessExpr;
import japaparser.ast.expr.InstanceOfExpr;
import japaparser.ast.expr.IntegerLiteralExpr;
import japaparser.ast.expr.IntegerLiteralMinValueExpr;
import japaparser.ast.expr.LongLiteralExpr;
import japaparser.ast.expr.LongLiteralMinValueExpr;
import japaparser.ast.expr.MarkerAnnotationExpr;
import japaparser.ast.expr.MemberValuePair;
import japaparser.ast.expr.MethodCallExpr;
import japaparser.ast.expr.NameExpr;
import japaparser.ast.expr.NormalAnnotationExpr;
import japaparser.ast.expr.NullLiteralExpr;
import japaparser.ast.expr.ObjectCreationExpr;
import japaparser.ast.expr.QualifiedNameExpr;
import japaparser.ast.expr.SingleMemberAnnotationExpr;
import japaparser.ast.expr.StringLiteralExpr;
import japaparser.ast.expr.SuperExpr;
import japaparser.ast.expr.ThisExpr;
import japaparser.ast.expr.UnaryExpr;
import japaparser.ast.expr.VariableDeclarationExpr;
import japaparser.ast.stmt.AssertStmt;
import japaparser.ast.stmt.BlockStmt;
import japaparser.ast.stmt.BreakStmt;
import japaparser.ast.stmt.CatchClause;
import japaparser.ast.stmt.ContinueStmt;
import japaparser.ast.stmt.DoStmt;
import japaparser.ast.stmt.EmptyStmt;
import japaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import japaparser.ast.stmt.ExpressionStmt;
import japaparser.ast.stmt.ForStmt;
import japaparser.ast.stmt.ForeachStmt;
import japaparser.ast.stmt.IfStmt;
import japaparser.ast.stmt.LabeledStmt;
import japaparser.ast.stmt.ReturnStmt;
import japaparser.ast.stmt.SwitchEntryStmt;
import japaparser.ast.stmt.SwitchStmt;
import japaparser.ast.stmt.SynchronizedStmt;
import japaparser.ast.stmt.ThrowStmt;
import japaparser.ast.stmt.TryStmt;
import japaparser.ast.stmt.TypeDeclarationStmt;
import japaparser.ast.stmt.WhileStmt;
import japaparser.ast.type.ClassOrInterfaceType;
import japaparser.ast.type.PrimitiveType;
import japaparser.ast.type.ReferenceType;
import japaparser.ast.type.VoidType;
import japaparser.ast.type.WildcardType;
import japaparser.ast.visitor.VoidVisitorAdapter;

import org.junit.Test;

/**
 * @author Julio Vilmar Gesser
 */
public class TestNodePositions {

    @Test
    public void testNodePositions() throws Exception {
        String source = TestHelper.readClass("./test", DumperTestClass.class);
        CompilationUnit cu = TestHelper.parserString(source);

        cu.accept(new TestVisitor(source), null);
    }

    void doTest(String source, Node node) {
        String parsed = node.toString();

        assertTrue(node.getClass().getName() + ": " + parsed, node.getBeginLine() >= 0);
        assertTrue(node.getClass().getName() + ": " + parsed, node.getBeginColumn() >= 0);
        assertTrue(node.getClass().getName() + ": " + parsed, node.getEndLine() >= 0);
        assertTrue(node.getClass().getName() + ": " + parsed, node.getEndColumn() >= 0);

        if (node.getBeginLine() == node.getEndLine()) {
            assertTrue(node.getClass().getName() + ": " + parsed, node.getBeginColumn() <= node.getEndColumn());
        } else {
            assertTrue(node.getClass().getName() + ": " + parsed, node.getBeginLine() <= node.getEndLine());
        }

        String substr = substring(source, node.getBeginLine(), node.getBeginColumn(), node.getEndLine(), node.getEndColumn());
        assertEquals(node.getClass().getName(), trimLines(parsed), trimLines(substr));
    }

    private String trimLines(String str) {
        String[] split = str.split("\n");
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            ret.append(split[i].trim());
            if (i < split.length - 1) {
                ret.append("\n");
            }
        }

        return ret.toString();
    }

    private String substring(String source, int beginLine, int beginColumn, int endLine, int endColumn) {
        int pos = 0;
        while (beginLine > 1) {
            if (source.charAt(pos) == '\n') {
                beginLine--;
                endLine--;
            }
            pos++;
        }
        int start = pos + beginColumn - 1;

        while (endLine > 1) {
            if (source.charAt(pos) == '\n') {
                endLine--;
            }
            pos++;
        }
        int end = pos + endColumn;

        return source.substring(start, end);
    }

    class TestVisitor extends VoidVisitorAdapter<Object> {

        private final String source;

        public TestVisitor(String source) {
            this.source = source;
        }

        @Override
        public void visit(AnnotationDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(AnnotationMemberDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ArrayAccessExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ArrayCreationExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ArrayInitializerExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(AssertStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(AssignExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(BinaryExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(BlockComment n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(BlockStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(BooleanLiteralExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(BreakStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(CastExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(CatchClause n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(CharLiteralExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ClassExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ClassOrInterfaceType n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(CompilationUnit n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ConditionalExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ConstructorDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ContinueStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(DoStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(DoubleLiteralExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(EmptyMemberDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(EmptyStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(EmptyTypeDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(EnclosedExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(EnumConstantDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(EnumDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ExpressionStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(FieldAccessExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(FieldDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ForeachStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ForStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(IfStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ImportDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(InitializerDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(InstanceOfExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(IntegerLiteralExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(IntegerLiteralMinValueExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(JavadocComment n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(LabeledStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(LineComment n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(LongLiteralExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(LongLiteralMinValueExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(MarkerAnnotationExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(MemberValuePair n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(MethodCallExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(NameExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(NormalAnnotationExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(NullLiteralExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ObjectCreationExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(PackageDeclaration n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(Parameter n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(PrimitiveType n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(QualifiedNameExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ReferenceType n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ReturnStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(SingleMemberAnnotationExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(StringLiteralExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(SuperExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(SwitchEntryStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(SwitchStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(SynchronizedStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ThisExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(ThrowStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(TryStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(TypeDeclarationStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(TypeParameter n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(UnaryExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(VariableDeclarationExpr n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(VariableDeclarator n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(VariableDeclaratorId n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(VoidType n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(WhileStmt n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

        @Override
        public void visit(WildcardType n, Object arg) {
            doTest(source, n);
            super.visit(n, arg);
        }

    }

}
