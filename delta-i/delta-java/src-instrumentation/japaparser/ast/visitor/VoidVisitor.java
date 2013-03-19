/*
 * Copyright (C) 2007 J�lio Vilmar Gesser.
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
 * Created on 05/10/2006
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

/**
 * @author Julio Vilmar Gesser
 */
public interface VoidVisitor<A> {

    //- Compilation Unit ----------------------------------

    public void visit(CompilationUnit n, A arg);

    public void visit(PackageDeclaration n, A arg);

    public void visit(ImportDeclaration n, A arg);

    public void visit(TypeParameter n, A arg);

    public void visit(LineComment n, A arg);

    public void visit(BlockComment n, A arg);

    //- Body ----------------------------------------------

    public void visit(ClassOrInterfaceDeclaration n, A arg);

    public void visit(EnumDeclaration n, A arg);

    public void visit(EmptyTypeDeclaration n, A arg);

    public void visit(EnumConstantDeclaration n, A arg);

    public void visit(AnnotationDeclaration n, A arg);

    public void visit(AnnotationMemberDeclaration n, A arg);

    public void visit(FieldDeclaration n, A arg);

    public void visit(VariableDeclarator n, A arg);

    public void visit(VariableDeclaratorId n, A arg);

    public void visit(ConstructorDeclaration n, A arg);

    public void visit(MethodDeclaration n, A arg);

    public void visit(Parameter n, A arg);

    public void visit(EmptyMemberDeclaration n, A arg);

    public void visit(InitializerDeclaration n, A arg);

    public void visit(JavadocComment n, A arg);

    //- Type ----------------------------------------------

    public void visit(ClassOrInterfaceType n, A arg);

    public void visit(PrimitiveType n, A arg);

    public void visit(ReferenceType n, A arg);

    public void visit(VoidType n, A arg);

    public void visit(WildcardType n, A arg);

    //- Expression ----------------------------------------

    public void visit(ArrayAccessExpr n, A arg);

    public void visit(ArrayCreationExpr n, A arg);

    public void visit(ArrayInitializerExpr n, A arg);

    public void visit(AssignExpr n, A arg);

    public void visit(BinaryExpr n, A arg);

    public void visit(CastExpr n, A arg);

    public void visit(ClassExpr n, A arg);

    public void visit(ConditionalExpr n, A arg);

    public void visit(EnclosedExpr n, A arg);

    public void visit(FieldAccessExpr n, A arg);

    public void visit(InstanceOfExpr n, A arg);

    public void visit(StringLiteralExpr n, A arg);

    public void visit(IntegerLiteralExpr n, A arg);

    public void visit(LongLiteralExpr n, A arg);

    public void visit(IntegerLiteralMinValueExpr n, A arg);

    public void visit(LongLiteralMinValueExpr n, A arg);

    public void visit(CharLiteralExpr n, A arg);

    public void visit(DoubleLiteralExpr n, A arg);

    public void visit(BooleanLiteralExpr n, A arg);

    public void visit(NullLiteralExpr n, A arg);

    public void visit(MethodCallExpr n, A arg);

    public void visit(NameExpr n, A arg);

    public void visit(ObjectCreationExpr n, A arg);

    public void visit(QualifiedNameExpr n, A arg);

    public void visit(ThisExpr n, A arg);

    public void visit(SuperExpr n, A arg);

    public void visit(UnaryExpr n, A arg);

    public void visit(VariableDeclarationExpr n, A arg);

    public void visit(MarkerAnnotationExpr n, A arg);

    public void visit(SingleMemberAnnotationExpr n, A arg);

    public void visit(NormalAnnotationExpr n, A arg);

    public void visit(MemberValuePair n, A arg);

    //- Statements ----------------------------------------

    public void visit(ExplicitConstructorInvocationStmt n, A arg);

    public void visit(TypeDeclarationStmt n, A arg);

    public void visit(AssertStmt n, A arg);

    public void visit(BlockStmt n, A arg);

    public void visit(LabeledStmt n, A arg);

    public void visit(EmptyStmt n, A arg);

    public void visit(ExpressionStmt n, A arg);

    public void visit(SwitchStmt n, A arg);

    public void visit(SwitchEntryStmt n, A arg);

    public void visit(BreakStmt n, A arg);

    public void visit(ReturnStmt n, A arg);

    public void visit(IfStmt n, A arg);

    public void visit(WhileStmt n, A arg);

    public void visit(ContinueStmt n, A arg);

    public void visit(DoStmt n, A arg);

    public void visit(ForeachStmt n, A arg);

    public void visit(ForStmt n, A arg);

    public void visit(ThrowStmt n, A arg);

    public void visit(SynchronizedStmt n, A arg);

    public void visit(TryStmt n, A arg);

    public void visit(CatchClause n, A arg);

}
