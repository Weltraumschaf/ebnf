package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.Notification;
import de.weltraumschaf.ebnf.visitor.Visitor;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * Unit test for Comment.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CommentTest {

    @Test public void testProbeEquivalence() {
        Notification notification;
        final Comment comment1 = Comment.newInstance();
        comment1.value = "a";
        notification = new Notification();
        comment1.probeEquivalence(comment1, notification);
        assertTrue(notification.isOk());

        final Comment comment2 = Comment.newInstance();
        comment2.value = "b";
        notification = new Notification();
        comment2.probeEquivalence(comment2, notification);
        assertTrue(notification.isOk());

        notification = new Notification();
        comment1.probeEquivalence(Identifier.newInstance(), notification);
        assertFalse(notification.isOk());
        assertEquals("Probed node types mismatch: 'class de.weltraumschaf.ebnf.ast.nodes.Comment' "
                   + "!= 'class de.weltraumschaf.ebnf.ast.nodes.Identifier'!",
            notification.report()
        );

        notification = new Notification();
        comment1.probeEquivalence(comment2, notification);
        assertFalse(notification.isOk());
        assertEquals("Comment value mismatch: 'a' != 'b'!", notification.report());

        notification = new Notification();
        comment2.probeEquivalence(comment1, notification);
        assertFalse(notification.isOk());
        assertEquals("Comment value mismatch: 'b' != 'a'!", notification.report());
    }

    @Test public void testDepth() {
        final Comment comment = Comment.newInstance();
        assertEquals(1, comment.depth());
    }

    @Test public void accept() {
        final Visitor visitor = mock(Visitor.class);
        final Comment comment = Comment.newInstance();
        comment.accept(visitor);
        verify(visitor, times(1)).beforeVisit(comment);
        verify(visitor, times(1)).visit(comment);
        verify(visitor, times(1)).afterVisit(comment);
    }

    @Test public void testToString() {
        final Comment comment = Comment.newInstance("foo");
        assertEquals("<COMMENT value=foo>", comment.toString());
    }
}
