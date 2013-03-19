/*
 * Copyright (C) 2008 J�lio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 09/06/2008
 */
package japaparser.ast.visitor;

import japaparser.ast.BlockComment;
import japaparser.ast.CompilationUnit;
import japaparser.ast.ImportDeclaration;
import japaparser.ast.LineComment;
import japaparser.ast.PackageDeclaration;
import japaparser.ast.TypeParameter;
import japaparser.ast.body.AnnotationDeclaration;
import japaparser.ast.body.AnnotationMemberDeclaration;
import japaparser.ast.body.BodyDeclaration;
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
import japaparser.ast.body.TypeDeclaration;
import japaparser.ast.body.VariableDeclarator;
import japaparser.ast.body.VariableDeclaratorId;
import japaparser.ast.expr.AnnotationExpr;
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
import japaparser.ast.expr.Expression;
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
import japaparser.ast.stmt.Statement;
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
import japaparser.ast.type.Type;
import japaparser.ast.type.VoidType;
import japaparser.ast.type.WildcardType;

/**
 * @author Julio Vilmar Gesser
 */
public abstract class GenericVisitorAdapter<R, A> implements GenericVisitor<R, A> {

    public R visit(AnnotationDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getMembers() != null) {
            for (BodyDeclaration member : n.getMembers()) {
                member.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(AnnotationMemberDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        if (n.getDefaultValue() != null) {
            n.getDefaultValue().accept(this, arg);
        }
        return null;
    }

    public R visit(ArrayAccessExpr n, A arg) {
        n.getName().accept(this, arg);
        n.getIndex().accept(this, arg);
        return null;
    }

    public R visit(ArrayCreationExpr n, A arg) {
        n.getType().accept(this, arg);
        if (n.getDimensions() != null) {
            for (Expression dim : n.getDimensions()) {
                dim.accept(this, arg);
            }
        } else {
            n.getInitializer().accept(this, arg);
        }
        return null;
    }

    public R visit(ArrayInitializerExpr n, A arg) {
        if (n.getValues() != null) {
            for (Expression expr : n.getValues()) {
                expr.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(AssertStmt n, A arg) {
        n.getCheck().accept(this, arg);
        if (n.getMessage() != null) {
            n.getMessage().accept(this, arg);
        }
        return null;
    }

    public R visit(AssignExpr n, A arg) {
        n.getTarget().accept(this, arg);
        n.getValue().accept(this, arg);
        return null;
    }

    public R visit(BinaryExpr n, A arg) {
        n.getLeft().accept(this, arg);
        n.getRight().accept(this, arg);
        return null;
    }

    public R visit(BlockStmt n, A arg) {
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
            }
        }
        return null;

    }

    public R visit(BooleanLiteralExpr n, A arg) {
        return null;
    }

    public R visit(BreakStmt n, A arg) {
        return null;
    }

    public R visit(CastExpr n, A arg) {
        n.getType().accept(this, arg);
        n.getExpr().accept(this, arg);
        return null;
    }

    public R visit(CatchClause n, A arg) {
        n.getExcept().accept(this, arg);
        n.getCatchBlock().accept(this, arg);
        return null;

    }

    public R visit(CharLiteralExpr n, A arg) {
        return null;
    }

    public R visit(ClassExpr n, A arg) {
        n.getType().accept(this, arg);
        return null;
    }

    public R visit(ClassOrInterfaceDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getTypeParameters() != null) {
            for (TypeParameter t : n.getTypeParameters()) {
                t.accept(this, arg);
            }
        }
        if (n.getExtends() != null) {
            for (ClassOrInterfaceType c : n.getExtends()) {
                c.accept(this, arg);
            }
        }

        if (n.getImplements() != null) {
            for (ClassOrInterfaceType c : n.getImplements()) {
                c.accept(this, arg);
            }
        }
        if (n.getMembers() != null) {
            for (BodyDeclaration member : n.getMembers()) {
                member.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(ClassOrInterfaceType n, A arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        if (n.getTypeArgs() != null) {
            for (Type t : n.getTypeArgs()) {
                t.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(CompilationUnit n, A arg) {
        if (n.getPackage() != null) {
            n.getPackage().accept(this, arg);
        }
        if (n.getImports() != null) {
            for (ImportDeclaration i : n.getImports()) {
                i.accept(this, arg);
            }
        }
        if (n.getTypes() != null) {
            for (TypeDeclaration typeDeclaration : n.getTypes()) {
                typeDeclaration.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(ConditionalExpr n, A arg) {
        n.getCondition().accept(this, arg);
        n.getThenExpr().accept(this, arg);
        n.getElseExpr().accept(this, arg);
        return null;
    }

    public R visit(ConstructorDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getTypeParameters() != null) {
            for (TypeParameter t : n.getTypeParameters()) {
                t.accept(this, arg);
            }
        }
        if (n.getParameters() != null) {
            for (Parameter p : n.getParameters()) {
                p.accept(this, arg);
            }
        }
        if (n.getThrows() != null) {
            for (NameExpr name : n.getThrows()) {
                name.accept(this, arg);
            }
        }
        n.getBlock().accept(this, arg);
        return null;
    }

    public R visit(ContinueStmt n, A arg) {
        return null;
    }

    public R visit(DoStmt n, A arg) {
        n.getBody().accept(this, arg);
        n.getCondition().accept(this, arg);
        return null;
    }

    public R visit(DoubleLiteralExpr n, A arg) {
        return null;
    }

    public R visit(EmptyMemberDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        return null;
    }

    public R visit(EmptyStmt n, A arg) {
        return null;
    }

    public R visit(EmptyTypeDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        return null;
    }

    public R visit(EnclosedExpr n, A arg) {
        n.getInner().accept(this, arg);
        return null;
    }

    public R visit(EnumConstantDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getArgs() != null) {
            for (Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
        if (n.getClassBody() != null) {
            for (BodyDeclaration member : n.getClassBody()) {
                member.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(EnumDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getImplements() != null) {
            for (ClassOrInterfaceType c : n.getImplements()) {
                c.accept(this, arg);
            }
        }
        if (n.getEntries() != null) {
            for (EnumConstantDeclaration e : n.getEntries()) {
                e.accept(this, arg);
            }
        }
        if (n.getMembers() != null) {
            for (BodyDeclaration member : n.getMembers()) {
                member.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(ExplicitConstructorInvocationStmt n, A arg) {
        if (!n.isThis()) {
            if (n.getExpr() != null) {
                n.getExpr().accept(this, arg);
            }
        }
        if (n.getTypeArgs() != null) {
            for (Type t : n.getTypeArgs()) {
                t.accept(this, arg);
            }
        }
        if (n.getArgs() != null) {
            for (Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(ExpressionStmt n, A arg) {
        n.getExpression().accept(this, arg);
        return null;
    }

    public R visit(FieldAccessExpr n, A arg) {
        n.getScope().accept(this, arg);
        return null;
    }

    public R visit(FieldDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        for (VariableDeclarator var : n.getVariables()) {
            var.accept(this, arg);
        }
        return null;
    }

    public R visit(ForeachStmt n, A arg) {
        n.getVariable().accept(this, arg);
        n.getIterable().accept(this, arg);
        n.getBody().accept(this, arg);
        return null;
    }

    public R visit(ForStmt n, A arg) {
        if (n.getInit() != null) {
            for (Expression e : n.getInit()) {
                e.accept(this, arg);
            }
        }
        if (n.getCompare() != null) {
            n.getCompare().accept(this, arg);
        }
        if (n.getUpdate() != null) {
            for (Expression e : n.getUpdate()) {
                e.accept(this, arg);
            }
        }
        n.getBody().accept(this, arg);
        return null;
    }

    public R visit(IfStmt n, A arg) {
        n.getCondition().accept(this, arg);
        n.getThenStmt().accept(this, arg);
        if (n.getElseStmt() != null) {
            n.getElseStmt().accept(this, arg);
        }
        return null;
    }

    public R visit(ImportDeclaration n, A arg) {
        n.getName().accept(this, arg);
        return null;
    }

    public R visit(InitializerDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        n.getBlock().accept(this, arg);
        return null;
    }

    public R visit(InstanceOfExpr n, A arg) {
        n.getExpr().accept(this, arg);
        n.getType().accept(this, arg);
        return null;
    }

    public R visit(IntegerLiteralExpr n, A arg) {
        return null;
    }

    public R visit(IntegerLiteralMinValueExpr n, A arg) {
        return null;
    }

    public R visit(JavadocComment n, A arg) {
        return null;
    }

    public R visit(LabeledStmt n, A arg) {
        n.getStmt().accept(this, arg);
        return null;
    }

    public R visit(LongLiteralExpr n, A arg) {
        return null;
    }

    public R visit(LongLiteralMinValueExpr n, A arg) {
        return null;
    }

    public R visit(MarkerAnnotationExpr n, A arg) {
        n.getName().accept(this, arg);
        return null;
    }

    public R visit(MemberValuePair n, A arg) {
        n.getValue().accept(this, arg);
        return null;
    }

    public R visit(MethodCallExpr n, A arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        if (n.getTypeArgs() != null) {
            for (Type t : n.getTypeArgs()) {
                t.accept(this, arg);
            }
        }
        if (n.getArgs() != null) {
            for (Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(MethodDeclaration n, A arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        if (n.getTypeParameters() != null) {
            for (TypeParameter t : n.getTypeParameters()) {
                t.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        if (n.getParameters() != null) {
            for (Parameter p : n.getParameters()) {
                p.accept(this, arg);
            }
        }
        if (n.getThrows() != null) {
            for (NameExpr name : n.getThrows()) {
                name.accept(this, arg);
            }
        }
        if (n.getBody() != null) {
            n.getBody().accept(this, arg);
        }
        return null;
    }

    public R visit(NameExpr n, A arg) {
        return null;
    }

    public R visit(NormalAnnotationExpr n, A arg) {
        n.getName().accept(this, arg);
        if (n.getPairs() != null) {
            for (MemberValuePair m : n.getPairs()) {
                m.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(NullLiteralExpr n, A arg) {
        return null;
    }

    public R visit(ObjectCreationExpr n, A arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
        }
        if (n.getTypeArgs() != null) {
            for (Type t : n.getTypeArgs()) {
                t.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        if (n.getArgs() != null) {
            for (Expression e : n.getArgs()) {
                e.accept(this, arg);
            }
        }
        if (n.getAnonymousClassBody() != null) {
            for (BodyDeclaration member : n.getAnonymousClassBody()) {
                member.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(PackageDeclaration n, A arg) {
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        n.getName().accept(this, arg);
        return null;
    }

    public R visit(Parameter n, A arg) {
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        n.getId().accept(this, arg);
        return null;
    }

    public R visit(PrimitiveType n, A arg) {
        return null;
    }

    public R visit(QualifiedNameExpr n, A arg) {
        n.getQualifier().accept(this, arg);
        return null;
    }

    public R visit(ReferenceType n, A arg) {
        n.getType().accept(this, arg);
        return null;
    }

    public R visit(ReturnStmt n, A arg) {
        if (n.getExpr() != null) {
            n.getExpr().accept(this, arg);
        }
        return null;
    }

    public R visit(SingleMemberAnnotationExpr n, A arg) {
        n.getName().accept(this, arg);
        n.getMemberValue().accept(this, arg);
        return null;
    }

    public R visit(StringLiteralExpr n, A arg) {
        return null;
    }

    public R visit(SuperExpr n, A arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
        }
        return null;
    }

    public R visit(SwitchEntryStmt n, A arg) {
        if (n.getLabel() != null) {
            n.getLabel().accept(this, arg);
        }
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(SwitchStmt n, A arg) {
        n.getSelector().accept(this, arg);
        if (n.getEntries() != null) {
            for (SwitchEntryStmt e : n.getEntries()) {
                e.accept(this, arg);
            }
        }
        return null;

    }

    public R visit(SynchronizedStmt n, A arg) {
        n.getExpr().accept(this, arg);
        n.getBlock().accept(this, arg);
        return null;
    }

    public R visit(ThisExpr n, A arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
        }
        return null;
    }

    public R visit(ThrowStmt n, A arg) {
        n.getExpr().accept(this, arg);
        return null;
    }

    public R visit(TryStmt n, A arg) {
        n.getTryBlock().accept(this, arg);
        if (n.getCatchs() != null) {
            for (CatchClause c : n.getCatchs()) {
                c.accept(this, arg);
            }
        }
        if (n.getFinallyBlock() != null) {
            n.getFinallyBlock().accept(this, arg);
        }
        return null;
    }

    public R visit(TypeDeclarationStmt n, A arg) {
        n.getTypeDeclaration().accept(this, arg);
        return null;
    }

    public R visit(TypeParameter n, A arg) {
        if (n.getTypeBound() != null) {
            for (ClassOrInterfaceType c : n.getTypeBound()) {
                c.accept(this, arg);
            }
        }
        return null;
    }

    public R visit(UnaryExpr n, A arg) {
        n.getExpr().accept(this, arg);
        return null;
    }

    public R visit(VariableDeclarationExpr n, A arg) {
        if (n.getAnnotations() != null) {
            for (AnnotationExpr a : n.getAnnotations()) {
                a.accept(this, arg);
            }
        }
        n.getType().accept(this, arg);
        for (VariableDeclarator v : n.getVars()) {
            v.accept(this, arg);
        }
        return null;
    }

    public R visit(VariableDeclarator n, A arg) {
        n.getId().accept(this, arg);
        if (n.getInit() != null) {
            n.getInit().accept(this, arg);
        }
        return null;
    }

    public R visit(VariableDeclaratorId n, A arg) {
        return null;
    }

    public R visit(VoidType n, A arg) {
        return null;
    }

    public R visit(WhileStmt n, A arg) {
        n.getCondition().accept(this, arg);
        n.getBody().accept(this, arg);
        return null;
    }

    public R visit(WildcardType n, A arg) {
        if (n.getExtends() != null) {
            n.getExtends().accept(this, arg);
        }
        if (n.getSuper() != null) {
            n.getSuper().accept(this, arg);
        }
        return null;
    }

    public R visit(BlockComment n, A arg) {
        return null;
    }

    public R visit(LineComment n, A arg) {
        return null;
    }

}
