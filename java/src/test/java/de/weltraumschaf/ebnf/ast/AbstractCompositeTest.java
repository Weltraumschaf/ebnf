package de.weltraumschaf.ebnf.ast;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;
;

/**
 * Unit test for AbstractComposite.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AbstractCompositeTest {

    class AbstractCompositeImpl extends AbstractComposite {

        @Override
        public String getNodeName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    @Test public void testAddHasAndGetChildren() {
        AbstractComposite composite = new AbstractCompositeImpl();

        assertFalse(composite.hasChildren());
        assertEquals(0, composite.countChildren());
        List<Node> childs = composite.getChildren();
        assertEquals(0, childs.size());

        Node nodeOne = mock(Node.class);
        composite.addChild(nodeOne);
        assertTrue(composite.hasChildren());
        assertEquals(1, composite.countChildren());
        childs = composite.getChildren();
        assertEquals(1, childs.size());
        assertSame(nodeOne, childs.get(0));

        Node nodeTwo = mock(Node.class);
        composite.addChild(nodeTwo);
        assertTrue(composite.hasChildren());
        assertEquals(2, composite.countChildren());
        childs = composite.getChildren();
        assertEquals(2, childs.size());
        assertSame(nodeOne, childs.get(0));
        assertSame(nodeTwo, childs.get(1));
    }

    @Test public void probeEquivalenceInternal() {
        AbstractComposite comp = new AbstractCompositeImpl();
        Notification n = new Notification();
        comp.probeEquivalence(new Terminal(mock(Node.class)), n);
        assertFalse(n.isOk());
        assertEquals(
            "Probed node is not a composite node: 'class de.weltraumschaf.ebnf.ast.Terminal'!",
            n.report()
        );

        n = new Notification();
        comp.probeEquivalence(new Rule(mock(Node.class)), n);
        assertFalse(n.isOk());
        assertEquals(
            "Probed node types mismatch: 'class de.weltraumschaf.ebnf.ast.AbstractCompositeTest$AbstractCompositeImpl' != 'class de.weltraumschaf.ebnf.ast.Rule'!",
            n.report()
        );
    }

    @Test public void depth() {
        Syntax syntax = new Syntax();
        assertEquals(1, syntax.depth());

        Rule rule = new Rule(mock(Node.class));
        assertEquals(1, rule.depth());
        syntax.addChild(rule);
        assertEquals(2, syntax.depth());

        Sequence seq = new Sequence(mock(Node.class));
        assertEquals(1, seq.depth());
        rule.addChild(seq);
        assertEquals(2, rule.depth());
        assertEquals(3, syntax.depth());

        Identifier ident = new Identifier(mock(Node.class));
        assertEquals(1, ident.depth());
        seq.addChild(ident);
        assertEquals(2, seq.depth());
        assertEquals(3, rule.depth());
        assertEquals(4, syntax.depth());

        Loop loop = new Loop(mock(Node.class));
        assertEquals(1, loop.depth());
        seq.addChild(loop);
        assertEquals(2, seq.depth());
        assertEquals(3, rule.depth());
        assertEquals(4, syntax.depth());

        Terminal term = new Terminal(mock(Node.class));
        assertEquals(1, term.depth());
        loop.addChild(term);
        assertEquals(2, loop.depth());
        assertEquals(3, seq.depth());
        assertEquals(4, rule.depth());
        assertEquals(5, syntax.depth());
    }
}
