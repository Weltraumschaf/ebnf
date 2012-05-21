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
import de.weltraumschaf.ebnf.ast.Notification;
import de.weltraumschaf.ebnf.visitor.Visitor;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class NullTest {

    private final Null sut = Null.getInstance();

    @Test public void getNodeName() {
        assertEquals("null", sut.getNodeName());
    }

    @Test public void accept() {
        final Visitor visitor = mock(Visitor.class);
        sut.accept(visitor);
        verify(visitor, never()).beforeVisit(sut);
        verify(visitor, never()).visit(sut);
        verify(visitor, never()).afterVisit(sut);
    }


    @Test  public void probeEquivalence() {
        Notification notification = new Notification();
        sut.probeEquivalence(sut, notification);
        assertTrue(notification.isOk());

        notification = new Notification();
        sut.probeEquivalence(Sequence.newInstance(), notification);
        assertFalse(notification.isOk());
    }

    @Test public void depth() {
        assertEquals(0, sut.depth());
    }
}
