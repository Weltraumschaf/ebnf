/*
 * LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a beer in return.
 *
 */

package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.ast.nodes.Null;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AbstractNodeTest {

    static class AbstractNodeStub extends AbstractNode {

        public AbstractNodeStub(final Node parent, final NodeType type) {
            super(parent, type);
        }

        @Override
        public void probeEquivalence(final Node other, final Notification result) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int depth() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }

    @Test public void hasParent() {
        AbstractNodeStub sut = new AbstractNodeStub(null, NodeType.CHOICE);
        assertEquals(NodeType.CHOICE, sut.getType());
        assertFalse(sut.hasParent());
        assertNull(sut.getParent());

        sut = new AbstractNodeStub(Null.getInstance(), NodeType.CHOICE);
        assertFalse(sut.hasParent());
        assertSame(Null.getInstance(), sut.getParent());
        final Node parent = NodeFactory.newNode(NodeType.LOOP);
        sut = new AbstractNodeStub(parent, NodeType.CHOICE);
        assertTrue(sut.hasParent());
        assertSame(parent, sut.getParent());
    }

    @Test public void testAttributes() {
        final AbstractNodeStub sut = new AbstractNodeStub(null, NodeType.CHOICE);
        assertFalse(sut.hasAttributes());
        sut.setAttribute("foo", "bar");
        assertTrue(sut.hasAttributes());
        assertTrue(sut.hasAttribute("foo"));
        assertEquals("bar", sut.getAttribute("foo"));
        assertFalse(sut.hasAttribute("snafu"));

        try {
            sut.getAttribute("snafu");
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException ex) {
            assertEquals("Does not have attribute with name 'snafu'!", ex.getMessage());
        }
    }
}
