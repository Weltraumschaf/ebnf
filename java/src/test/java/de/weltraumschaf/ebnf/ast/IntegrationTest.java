package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.ast.nodes.*;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Tests the integration of all AST nodes in a whole tree.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class IntegrationTest {

    @Test public void testThatNodeIsComposite() {
        Choice choice = Choice.newInstance();
        assertTrue(choice instanceof Composite);

        Loop loop = Loop.newInstance();
        assertTrue(loop instanceof Composite);

        Option option = Option.newInstance();
        assertTrue(option instanceof Composite);

        Rule rule = Rule.newInstance();
        assertTrue(rule instanceof Composite);

        Sequence seq = Sequence.newInstance();
        assertTrue(seq instanceof Composite);

        Syntax syntax = Syntax.newInstance();
        assertTrue(syntax instanceof Composite);
    }

    @Test public void testIntegration() {
        Syntax syntax = Syntax.newInstance("foo", "bar");
        List<Node> children = syntax.getChildren();
        assertEquals(0, children.size());

        Rule rule1 = Rule.newInstance(syntax);
        assertSame(syntax, rule1.getParent());
        rule1.name = "first";
        syntax.addChild(rule1);

        children = syntax.getChildren();
        assertEquals(1, children.size());
        assertSame(rule1, children.get(0));

        Rule rule2 = Rule.newInstance(syntax);
        rule2.name = "second";
        assertSame(syntax, rule1.getParent());
        syntax.addChild(rule2);

        children = syntax.getChildren();
        assertEquals(2, children.size());
        assertSame(rule1, children.get(0));
        assertSame(rule2, children.get(1));
    }

    @Test public void testProbeEquivalenceSyntax() {
        Syntax syntax1 = Syntax.newInstance("foo", "bar");
        syntax1.title  = "foo";
        syntax1.meta   = "bar";
        Syntax syntax2 = Syntax.newInstance("foo", "bar");
        syntax2.title  = "foo";
        syntax2.meta   = "bar";
        Syntax syntax3 = Syntax.newInstance("bla", "blub");
        syntax3.title  = "bla";
        syntax3.meta   = "blub";

        Notification n = new Notification();
        syntax1.probeEquivalence(syntax1, n);
        assertTrue(n.report(), n.isOk());
        assertEquals("", n.report());
        n = new Notification();
        syntax1.probeEquivalence(syntax2, n);
        assertTrue(n.report(), n.isOk());
        assertEquals("", n.report());

        n = new Notification();
        syntax2.probeEquivalence(syntax2, n);
        assertTrue(n.report(), n.isOk());
        assertEquals("", n.report());
        n = new Notification();
        syntax2.probeEquivalence(syntax1, n);
        assertTrue(n.report(), n.isOk());
        assertEquals("", n.report());

        n = new Notification();
        syntax3.probeEquivalence(syntax3, n);
        assertTrue(n.report(), n.isOk());
        assertEquals("", n.report());

        StringBuilder errors = new StringBuilder();
        errors.append("Titles of syntx differs: 'foo' != 'bla'!\n");
        errors.append("Meta of syntx differs: 'bar' != 'blub'!");
        n = new Notification();
        syntax1.probeEquivalence(syntax3, n);
        assertFalse(n.isOk());
        assertEquals(errors.toString(), n.report());
        n = new Notification();
        syntax2.probeEquivalence(syntax3, n);
        assertFalse(n.isOk());
        assertEquals(errors.toString(), n.report());

        errors = new StringBuilder();
        errors.append("Titles of syntx differs: 'bla' != 'foo'!\n");
        errors.append("Meta of syntx differs: 'blub' != 'bar'!");
        n = new Notification();
        syntax3.probeEquivalence(syntax1, n);
        assertFalse(n.isOk());
        assertEquals(errors.toString(), n.report());
        n = new Notification();
        syntax3.probeEquivalence(syntax2, n);
        assertFalse(n.isOk());
        assertEquals(errors.toString(), n.report());

        errors = new StringBuilder();
        errors.append("Probed node types mismatch: 'class de.weltraumschaf.ebnf.ast.nodes.Syntax' != 'class de.weltraumschaf.ebnf.ast.nodes.Rule'!");
        Syntax stub = Syntax.newInstance();
        Node mock = Rule.newInstance(stub);
        assertFalse(mock instanceof Syntax);
        n = new Notification();
        stub.probeEquivalence(mock, n);
        assertFalse(n.isOk());
        assertEquals(errors.toString(), n.report());
    }

    @Ignore
    @Test public void testProbeEquivalenceSyntaxWithRules() {
        Syntax syntax1 = Syntax.newInstance("foo", "bar");
        Syntax syntax2 = Syntax.newInstance("foo", "bar");
        Syntax syntax3 = Syntax.newInstance("foo", "bar");
        Rule rule1 = Rule.newInstance();
        rule1.name = "rule1";
        syntax1.addChild(rule1);
        syntax2.addChild(rule1);

        Notification n = new Notification();
        syntax1.probeEquivalence(syntax2, n);
        assertTrue(n.report(), n.isOk());
        assertEquals("", n.report());

        n = new Notification();
        syntax2.probeEquivalence(syntax1, n);
        assertTrue(n.report(), n.isOk());
        assertEquals("", n.report());

        Rule rule2 = Rule.newInstance();
        rule2.name = "rule2";
        syntax1.addChild(rule2);
        StringBuilder error = new StringBuilder();
        error.append("Node syntax has different child size than other: 2 != 1!\n");
        error.append("Other node has not the expected subnode!");
        n = new Notification();
        syntax1.probeEquivalence(syntax2, n);
        assertFalse(n.isOk());
        assertEquals(error.toString(), n.report());

        error = new StringBuilder();
        error.append("Node syntax has different child size than other: 1 != 2!");
        n = new Notification();
        syntax2.probeEquivalence(syntax1, n);
        assertFalse(n.isOk());
        assertEquals(error, n.report());

        syntax2.addChild(rule2);
        n = new Notification();
        syntax1.probeEquivalence(syntax2, n);
        assertTrue(n.report(), n.isOk());
        assertEquals("", n.report());

        n = new Notification();
        syntax2.probeEquivalence(syntax1, n);
        assertTrue(n.report(), n.isOk());
        assertEquals("", n.report());

        Rule rule3 = Rule.newInstance();
        rule3.name = "rule3";
        syntax3.addChild(rule1);
        syntax3.addChild(rule3);
        n = new Notification();
        syntax1.probeEquivalence(syntax3, n);
        assertFalse(n.isOk());
        assertEquals("Names of rule differs: 'rule2' != 'rule3'!", n.report());
    }

    @Test public void testProbeEquivalenceSyntaxWithRulesAndSubnodes() {
//        builder = new SyntaxBuilder();
//        builder.syntax("foo", "bar")
//                .rule("syntax")
//                    .sequence()
//                        .option()
//                            .identifier("title")
//                        .end()
//                        .terminal("{")
//                        .loop()
//                            .identifier("rule")
//                        .end()
//                        .terminal("}")
//                        .option()
//                            .identifier("comment")
//                        .end()
//                    .end();
//        syntax1 = builder.getAst();
//        builder.clear()
//                .syntax("foo", "bar")
//                .rule("syntax")
//                    .sequence()
//                        .option()
//                            .identifier("title")
//                        .end()
//                        .terminal("{")
//                        .loop()
//                            .identifier("rule")
//                        .end()
//                        .terminal("}")
//                        .option()
//                            .identifier("comment")
//                        .end()
//                    .end();
//        syntax2 = builder.getAst();
//
//        n = new Notification();
//        syntax1.probeEquivalence(syntax2, n);
//        assertTrue(n.isOk(), n.report());
//        assertEquals("", n.report());
//
//        n = new Notification();
//        syntax2.probeEquivalence(syntax1, n);
//        assertTrue(n.isOk(), n.report());
//        assertEquals("", n.report());
//
//        builder.clear()
//                .syntax("foo", "bar")
//                .rule("syntax")
//                    .sequence()
//                        .option()
//                            .identifier("title")
//                        .end()
//                        .terminal("{")
//                        .loop()
//                            .identifier("rule")
//                        .end()
//                        .terminal("}")
//                        .option()
//                            .identifier("comment")
//                        .end()
//                    .end();
//        syntax1 = builder.getAst();
//        builder.clear()
//                .syntax("snafu", "bar")
//                .rule("syntax")
//                    .sequence()
//                        .option()
//                            .identifier("bla")
//                        .end()
//                        .terminal("{")
//                        .loop()
//                            .identifier("snafu")
//                        .end()
//                        .terminal("}")
//                        .option()
//                            .identifier("blub")
//                        .end()
//                    .end();
//        syntax2 = builder.getAst();
//
//        n = new Notification();
//        syntax1.probeEquivalence(syntax2, n);
//        assertFalse(n.isOk(), n.report());
//        assertEquals(
//            "Titles of syntx differs: 'foo' != 'snafu'!\n" .
//            "Identifier value mismatch: 'title' != 'bla'!\n" .
//            "Identifier value mismatch: 'rule' != 'snafu'!\n" .
//            "Identifier value mismatch: 'comment' != 'blub'!",
//            n.report()
//        );
//
//        n = new Notification();
//        syntax2.probeEquivalence(syntax1, n);
//        assertFalse(n.isOk(), n.report());
//        assertEquals(
//            "Titles of syntx differs: 'snafu' != 'foo'!\n" .
//            "Identifier value mismatch: 'bla' != 'title'!\n" .
//            "Identifier value mismatch: 'snafu' != 'rule'!\n" .
//            "Identifier value mismatch: 'blub' != 'comment'!",
//            n.report()
//        );

    }

}
