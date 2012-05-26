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
public class SyntaxTest {

    @Test public void getType() {
        assertEquals(NodeType.SYNTAX, Syntax.newInstance().getType());
    }

    @Test public void testToString() {
        final Syntax syntax = Syntax.newInstance("foo", "bar");
        assertEquals("<SYNTAX title=foo meta=bar>", syntax.toString());
        final Node node1 = mock(Node.class);
        when(node1.toString()).thenReturn("<foo>");
        syntax.addChild(node1);
        final Node node2 = mock(Node.class);
        when(node2.toString()).thenReturn("<bar>");
        syntax.addChild(node2);
        assertEquals("<SYNTAX title=foo meta=bar>\n"
                   + "<foo>\n"
                   + "<bar>", syntax.toString());
    }
}
