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
public interface GenericVisitor<R, A> {

    //- Compilation Unit ----------------------------------

    public R visit(CompilationUnit n, A arg);

    public R visit(PackageDeclaration n, A arg);

    public R visit(ImportDeclaration n, A arg);

    public R visit(TypeParameter n, A arg);

    public R visit(LineComment n, A arg);

    public R visit(BlockComment n, A arg);

    //- Body ----------------------------------------------

    public R visit(ClassOrInterfaceDeclaration n, A arg);

    public R visit(EnumDeclaration n, A arg);

    public R visit(EmptyTypeDeclaration n, A arg);

    public R visit(EnumConstantDeclaration n, A arg);

    public R visit(AnnotationDeclaration n, A arg);

    public R visit(AnnotationMemberDeclaration n, A arg);

    public R visit(FieldDeclaration n, A arg);

    public R visit(VariableDeclarator n, A arg);

    public R visit(VariableDeclaratorId n, A arg);

    public R visit(ConstructorDeclaration n, A arg);

    public R visit(MethodDeclaration n, A arg);

    public R visit(Parameter n, A arg);

    public R visit(EmptyMemberDeclaration n, A arg);

    public R visit(InitializerDeclaration n, A arg);

    public R visit(JavadocComment n, A arg);

    //- Type ----------------------------------------------

    public R visit(ClassOrInterfaceType n, A arg);

    public R visit(PrimitiveType n, A arg);

    public R visit(ReferenceType n, A arg);

    public R visit(VoidType n, A arg);

    public R visit(WildcardType n, A arg);

    //- Expression ----------------------------------------

    public R visit(ArrayAccessExpr n, A arg);

    public R visit(ArrayCreationExpr n, A arg);

    public R visit(ArrayInitializerExpr n, A arg);

    public R visit(AssignExpr n, A arg);

    public R visit(BinaryExpr n, A arg);

    public R visit(CastExpr n, A arg);

    public R visit(ClassExpr n, A arg);

    public R visit(ConditionalExpr n, A arg);

    public R visit(EnclosedExpr n, A arg);

    public R visit(FieldAccessExpr n, A arg);

    public R visit(InstanceOfExpr n, A arg);

    public R visit(StringLiteralExpr n, A arg);

    public R visit(IntegerLiteralExpr n, A arg);

    public R visit(LongLiteralExpr n, A arg);

    public R visit(IntegerLiteralMinValueExpr n, A arg);

    public R visit(LongLiteralMinValueExpr n, A arg);

    public R visit(CharLiteralExpr n, A arg);

    public R visit(DoubleLiteralExpr n, A arg);

    public R visit(BooleanLiteralExpr n, A arg);

    public R visit(NullLiteralExpr n, A arg);

    public R visit(MethodCallExpr n, A arg);

    public R visit(NameExpr n, A arg);

    public R visit(ObjectCreationExpr n, A arg);

    public R visit(QualifiedNameExpr n, A arg);

    public R visit(ThisExpr n, A arg);

    public R visit(SuperExpr n, A arg);

    public R visit(UnaryExpr n, A arg);

    public R visit(VariableDeclarationExpr n, A arg);

    public R visit(MarkerAnnotationExpr n, A arg);

    public R visit(SingleMemberAnnotationExpr n, A arg);

    public R visit(NormalAnnotationExpr n, A arg);

    public R visit(MemberValuePair n, A arg);

    //- Statements ----------------------------------------

    public R visit(ExplicitConstructorInvocationStmt n, A arg);

    public R visit(TypeDeclarationStmt n, A arg);

    public R visit(AssertStmt n, A arg);

    public R visit(BlockStmt n, A arg);

    public R visit(LabeledStmt n, A arg);

    public R visit(EmptyStmt n, A arg);

    public R visit(ExpressionStmt n, A arg);

    public R visit(SwitchStmt n, A arg);

    public R visit(SwitchEntryStmt n, A arg);

    public R visit(BreakStmt n, A arg);

    public R visit(ReturnStmt n, A arg);

    public R visit(IfStmt n, A arg);

    public R visit(WhileStmt n, A arg);

    public R visit(ContinueStmt n, A arg);

    public R visit(DoStmt n, A arg);

    public R visit(ForeachStmt n, A arg);

    public R visit(ForStmt n, A arg);

    public R visit(ThrowStmt n, A arg);

    public R visit(SynchronizedStmt n, A arg);

    public R visit(TryStmt n, A arg);

    public R visit(CatchClause n, A arg);

}
