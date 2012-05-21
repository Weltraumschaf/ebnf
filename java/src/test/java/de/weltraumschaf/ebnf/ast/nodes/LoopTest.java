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
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class LoopTest {

    @Test public void testToString() {
        final Loop loop = Loop.newInstance();
        assertEquals("<LOOP>", loop.toString());
        final Node node1 = mock(Node.class);
        when(node1.toString()).thenReturn("<foo>");
        loop.addChild(node1);
        final Node node2 = mock(Node.class);
        when(node2.toString()).thenReturn("<bar>");
        loop.addChild(node2);
        assertEquals("<LOOP>\n<foo>\n<bar>", loop.toString());
    }

}
