package de.weltraumschaf.ebnf.ast;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Unit test for Comment.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CommentTest {

    @Test public void testProbeEquivalence() {
        Notification n;
        Comment comment1 = new Comment(mock(Node.class));
        comment1.value = "a";
        n = new Notification();
        comment1.probeEquivalence(comment1, n);
        assertTrue(n.isOk());

        Comment comment2 = new Comment(mock(Node.class));
        comment2.value = "b";
        n = new Notification();
        comment2.probeEquivalence(comment2, n);
        assertTrue(n.isOk());

        n = new Notification();
        comment1.probeEquivalence(new Identifier(mock(Node.class)), n);
        assertFalse(n.isOk());
        assertEquals(
            "Probed node types mismatch: 'class de.weltraumschaf.ebnf.ast.Comment' != 'class de.weltraumschaf.ebnf.ast.Identifier'!",
            n.report()
        );

        n = new Notification();
        comment1.probeEquivalence(comment2, n);
        assertFalse(n.isOk());
        assertEquals("Comment value mismatch: 'a' != 'b'!", n.report());

        n = new Notification();
        comment2.probeEquivalence(comment1, n);
        assertFalse(n.isOk());
        assertEquals("Comment value mismatch: 'b' != 'a'!", n.report());
    }

    @Test public void testDepth() {
        Comment comment1 = new Comment(mock(Node.class));
        assertEquals(1, comment1.depth());
    }
}
