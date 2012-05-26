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

package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.NodeType;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SequenceTest {

    @Test public void testToString() {
        final Sequence sequence = Sequence.newInstance();
        assertEquals("<SEQUENCE>", sequence.toString());
        final Node node1 = mock(Node.class);
        when(node1.toString()).thenReturn("<foo>");
        sequence.addChild(node1);
        final Node node2 = mock(Node.class);
        when(node2.toString()).thenReturn("<bar>");
        sequence.addChild(node2);
        assertEquals("<SEQUENCE>\n<foo>\n<bar>", sequence.toString());
    }

    @Test public void getType() {
        assertEquals(NodeType.SEQUENCE, Sequence.newInstance().getType());
    }
}
