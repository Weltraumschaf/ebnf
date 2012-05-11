package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.ast.nodes.*;
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
        final AbstractComposite composite = new AbstractCompositeImpl();

        assertFalse(composite.hasChildren());
        assertEquals(0, composite.countChildren());
        List<Node> childs = composite.getChildren();
        assertEquals(0, childs.size());

        final Node nodeOne = mock(Node.class);
        composite.addChild(nodeOne);
        assertTrue(composite.hasChildren());
        assertEquals(1, composite.countChildren());
        childs = composite.getChildren();
        assertEquals(1, childs.size());
        assertSame(nodeOne, childs.get(0));

        final Node nodeTwo = mock(Node.class);
        composite.addChild(nodeTwo);
        assertTrue(composite.hasChildren());
        assertEquals(2, composite.countChildren());
        childs = composite.getChildren();
        assertEquals(2, childs.size());
        assertSame(nodeOne, childs.get(0));
        assertSame(nodeTwo, childs.get(1));
    }

    @Test public void probeEquivalenceInternal() {
        final AbstractComposite comp = new AbstractCompositeImpl();
        Notification notification = new Notification();
        comp.probeEquivalence(Terminal.newInstance(), notification);
        assertFalse(notification.isOk());
        assertEquals(
            "Probed node is not a composite node: 'class de.weltraumschaf.ebnf.ast.nodes.Terminal'!",
            notification.report()
        );

        notification = new Notification();
        comp.probeEquivalence(Rule.newInstance(), notification);
        assertFalse(notification.isOk());
        assertEquals(
            "Probed node types mismatch: 'class de.weltraumschaf.ebnf.ast.AbstractCompositeTest$AbstractCompositeImpl' != 'class de.weltraumschaf.ebnf.ast.nodes.Rule'!",
            notification.report()
        );
    }

    @Test public void depth() {
        final Syntax syntax = Syntax.newInstance();
        assertEquals(1, syntax.depth());

        final Rule rule = Rule.newInstance();
        assertEquals(1, rule.depth());
        syntax.addChild(rule);
        assertEquals(2, syntax.depth());

        final Sequence seq = Sequence.newInstance();
        assertEquals(1, seq.depth());
        rule.addChild(seq);
        assertEquals(2, rule.depth());
        assertEquals(3, syntax.depth());

        final Identifier ident = Identifier.newInstance();
        assertEquals(1, ident.depth());
        seq.addChild(ident);
        assertEquals(2, seq.depth());
        assertEquals(3, rule.depth());
        assertEquals(4, syntax.depth());

        final Loop loop = Loop.newInstance();
        assertEquals(1, loop.depth());
        seq.addChild(loop);
        assertEquals(2, seq.depth());
        assertEquals(3, rule.depth());
        assertEquals(4, syntax.depth());

        final Terminal term = Terminal.newInstance();
        assertEquals(1, term.depth());
        loop.addChild(term);
        assertEquals(2, loop.depth());
        assertEquals(3, seq.depth());
        assertEquals(4, rule.depth());
        assertEquals(5, syntax.depth());
    }
}
