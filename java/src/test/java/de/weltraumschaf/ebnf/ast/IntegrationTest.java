package de.weltraumschaf.ebnf.ast;

import static de.weltraumschaf.ebnf.ast.builder.SyntaxBuilder.syntax;
import de.weltraumschaf.ebnf.ast.nodes.*;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the integration of all AST nodes in a whole tree.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class IntegrationTest {

    @Test public void testThatNodeIsComposite() {
        final Choice choice = Choice.newInstance();
        assertTrue(choice instanceof Composite);

        final Loop loop = Loop.newInstance();
        assertTrue(loop instanceof Composite);

        final Option option = Option.newInstance();
        assertTrue(option instanceof Composite);

        final Rule rule = Rule.newInstance();
        assertTrue(rule instanceof Composite);

        final Sequence seq = Sequence.newInstance();
        assertTrue(seq instanceof Composite);

        final Syntax syntax = Syntax.newInstance();
        assertTrue(syntax instanceof Composite);
    }

    @Test public void testIntegration() {
        final Syntax syntax = Syntax.newInstance("foo", "bar"); // NOPMD
        List<Node> children = syntax.getChildren();
        assertEquals(0, children.size());

        final Rule rule1 = Rule.newInstance(syntax);
        assertSame(syntax, rule1.getParent());
        rule1.name = "first";
        syntax.addChild(rule1);

        children = syntax.getChildren();
        assertEquals(1, children.size());
        assertSame(rule1, children.get(0));

        final Rule rule2 = Rule.newInstance(syntax);
        rule2.name = "second";
        assertSame(syntax, rule1.getParent());
        syntax.addChild(rule2);

        children = syntax.getChildren();
        assertEquals(2, children.size());
        assertSame(rule1, children.get(0));
        assertSame(rule2, children.get(1));
    }

    @Test public void testProbeEquivalenceSyntax() {
        final Syntax syntax1 = Syntax.newInstance("foo", "bar");
        syntax1.title  = "foo";
        syntax1.meta   = "bar";
        final Syntax syntax2 = Syntax.newInstance("foo", "bar");
        syntax2.title  = "foo";
        syntax2.meta   = "bar";
        final Syntax syntax3 = Syntax.newInstance("bla", "blub");
        syntax3.title  = "bla";
        syntax3.meta   = "blub";

        Notification notification = new Notification();
        syntax1.probeEquivalence(syntax1, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());
        notification = new Notification();
        syntax1.probeEquivalence(syntax2, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());

        notification = new Notification();
        syntax2.probeEquivalence(syntax2, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());
        notification = new Notification();
        syntax2.probeEquivalence(syntax1, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());

        notification = new Notification();
        syntax3.probeEquivalence(syntax3, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());

        StringBuilder errors = new StringBuilder();
        errors.append("Titles of syntx differs: 'foo' != 'bla'!\n");
        errors.append("Meta of syntx differs: 'bar' != 'blub'!");
        notification = new Notification();
        syntax1.probeEquivalence(syntax3, notification);
        assertFalse(notification.isOk());
        assertEquals(errors.toString(), notification.report());
        notification = new Notification();
        syntax2.probeEquivalence(syntax3, notification);
        assertFalse(notification.isOk());
        assertEquals(errors.toString(), notification.report());

        errors = new StringBuilder();
        errors.append("Titles of syntx differs: 'bla' != 'foo'!\n");
        errors.append("Meta of syntx differs: 'blub' != 'bar'!");
        notification = new Notification();
        syntax3.probeEquivalence(syntax1, notification);
        assertFalse(notification.isOk());
        assertEquals(errors.toString(), notification.report());
        notification = new Notification();
        syntax3.probeEquivalence(syntax2, notification);
        assertFalse(notification.isOk());
        assertEquals(errors.toString(), notification.report());

        errors = new StringBuilder();
        errors.append("Probed node types mismatch: 'class de.weltraumschaf.ebnf.ast.nodes.Syntax'"
                    + " != 'class de.weltraumschaf.ebnf.ast.nodes.Rule'!");
        final Syntax stub = Syntax.newInstance();
        final Node mock = Rule.newInstance(stub);
        assertFalse(mock instanceof Syntax);
        notification = new Notification();
        stub.probeEquivalence(mock, notification);
        assertFalse(notification.isOk());
        assertEquals(errors.toString(), notification.report());
    }

    @Test public void testProbeEquivalenceSyntaxWithRules() {
        final Syntax syntax1 = Syntax.newInstance("foo", "bar");
        final Syntax syntax2 = Syntax.newInstance("foo", "bar");
        final Syntax syntax3 = Syntax.newInstance("foo", "bar");
        final Rule rule1 = Rule.newInstance();
        rule1.name = "rule1";
        syntax1.addChild(rule1);
        syntax2.addChild(rule1);

        Notification notification = new Notification();
        syntax1.probeEquivalence(syntax2, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());

        notification = new Notification();
        syntax2.probeEquivalence(syntax1, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());

        final Rule rule2 = Rule.newInstance();
        rule2.name = "rule2";
        syntax1.addChild(rule2);
        StringBuilder error = new StringBuilder();
        error.append("Node syntax has different child count than other: 2 != 1!\n");
        error.append("Other node has not the expected subnode!");
        notification = new Notification();
        syntax1.probeEquivalence(syntax2, notification);
        assertFalse(notification.isOk());
        assertEquals(error.toString(), notification.report());

        error = new StringBuilder();
        error.append("Node syntax has different child count than other: 1 != 2!");
        notification = new Notification();
        syntax2.probeEquivalence(syntax1, notification);
        assertFalse(notification.isOk());
        assertEquals(error.toString(), notification.report());

        syntax2.addChild(rule2);
        notification = new Notification();
        syntax1.probeEquivalence(syntax2, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());

        notification = new Notification();
        syntax2.probeEquivalence(syntax1, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());

        final Rule rule3 = Rule.newInstance();
        rule3.name = "rule3";
        syntax3.addChild(rule1);
        syntax3.addChild(rule3);
        notification = new Notification();
        syntax1.probeEquivalence(syntax3, notification);
        assertFalse(notification.isOk());
        assertEquals("Names of rule differs: 'rule2' != 'rule3'!", notification.report());
    }

    @Test public void testProbeEquivalenceSyntaxWithRulesAndSubnodes() { //NOPMD
        Syntax syntax1, syntax2;
        syntax1 = syntax("foo", "bar")
            .rule("syntax") // NOPMD
                .sequence()
                    .option()
                        .identifier("title")
                    .end()
                    .terminal("{")
                    .loop()
                        .identifier("rule")
                    .end()
                    .terminal("}")
                    .option()
                        .identifier("comment")
                    .end()
                .end()
            .end()
        .build();
        syntax2 = syntax("foo", "bar")
            .rule("syntax")
                .sequence()
                    .option()
                        .identifier("title")
                    .end()
                    .terminal("{")
                    .loop()
                        .identifier("rule")
                    .end()
                    .terminal("}")
                    .option()
                        .identifier("comment")
                    .end()
                .end()
            .end()
        .build();

        Notification notification = new Notification();
        syntax1.probeEquivalence(syntax2, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());

        notification = new Notification();
        syntax2.probeEquivalence(syntax1, notification);
        assertTrue(notification.report(), notification.isOk());
        assertEquals("", notification.report());

        syntax1 =syntax("foo", "bar")
            .rule("syntax")
                .sequence()
                    .option()
                        .identifier("title")
                    .end()
                    .terminal("{")
                    .loop()
                        .identifier("rule")
                    .end()
                    .terminal("}")
                    .option()
                        .identifier("comment")
                    .end()
                .end()
            .end()
        .build();

        syntax2 = syntax("snafu", "bar")
            .rule("syntax")
                .sequence()
                    .option()
                        .identifier("bla")
                    .end()
                    .terminal("{")
                    .loop()
                        .identifier("snafu")
                    .end()
                    .terminal("}")
                    .option()
                        .identifier("blub")
                    .end()
                .end()
            .end()
        .build();

        notification = new Notification();
        syntax1.probeEquivalence(syntax2, notification);
        assertFalse(notification.isOk());
        assertEquals(
            "Titles of syntx differs: 'foo' != 'snafu'!\n" +
            "Identifier value mismatch: 'title' != 'bla'!\n" +
            "Identifier value mismatch: 'rule' != 'snafu'!\n" +
            "Identifier value mismatch: 'comment' != 'blub'!",
            notification.report()
        );

        notification = new Notification();
        syntax2.probeEquivalence(syntax1, notification);
        assertFalse(notification.isOk());
        assertEquals(
            "Titles of syntx differs: 'snafu' != 'foo'!\n" +
            "Identifier value mismatch: 'bla' != 'title'!\n" +
            "Identifier value mismatch: 'snafu' != 'rule'!\n" +
            "Identifier value mismatch: 'blub' != 'comment'!",
            notification.report()
        );

    }

}
