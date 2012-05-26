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
public class ChoiceTest {

    @Test public void testToString() {
        final Choice choice = Choice.newInstance();
        assertEquals("<CHOICE>", choice.toString());
        final Node node1 = mock(Node.class);
        when(node1.toString()).thenReturn("<foo>");
        choice.addChild(node1);
        final Node node2 = mock(Node.class);
        when(node2.toString()).thenReturn("<bar>");
        choice.addChild(node2);
        assertEquals("<CHOICE>\n<foo>\n<bar>", choice.toString());
    }

    @Test public void getType() {
        assertEquals(NodeType.CHOICE, Choice.newInstance().getType());
    }

}
