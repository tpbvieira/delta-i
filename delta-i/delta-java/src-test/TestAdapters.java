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
 * Created on 11/06/2008
 */


import japaparser.ParseException;
import japaparser.ast.CompilationUnit;
import japaparser.ast.visitor.GenericVisitor;
import japaparser.ast.visitor.GenericVisitorAdapter;
import japaparser.ast.visitor.ModifierVisitorAdapter;
import japaparser.ast.visitor.VoidVisitor;
import japaparser.ast.visitor.VoidVisitorAdapter;

import org.junit.Test;

/**
 * @author Julio Vilmar Gesser
 */
public class TestAdapters {

    class ConcreteVoidVisitorAdapter extends VoidVisitorAdapter {

    }

    class ConcreteGenericVisitorAdapter extends GenericVisitorAdapter {

    }

    class ConcreteModifierVisitorAdapter extends ModifierVisitorAdapter {

    }

    private void doTest(VoidVisitor< ? > visitor) throws ParseException {
        CompilationUnit cu = TestHelper.parserClass("./test", DumperTestClass.class);
        cu.accept(visitor, null);

        cu = TestHelper.parserClass("./test", JavadocTestClass.class);
        cu.accept(visitor, null);
    }

    private void doTest(GenericVisitor< ? , ? > visitor) throws ParseException {
        CompilationUnit cu = TestHelper.parserClass("./test", DumperTestClass.class);
        cu.accept(visitor, null);

        cu = TestHelper.parserClass("./test", JavadocTestClass.class);
        cu.accept(visitor, null);
    }

    @Test
    public void testVoidVisitorAdapter() throws Exception {
        doTest(new ConcreteVoidVisitorAdapter());
    }

    @Test
    public void testGenericVisitorAdapter() throws Exception {
        doTest(new ConcreteGenericVisitorAdapter());
    }

    @Test
    public void testModifierVisitorAdapter() throws Exception {
        doTest(new ConcreteModifierVisitorAdapter());
    }

}
