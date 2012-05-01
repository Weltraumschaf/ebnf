package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.Notification;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for Comment.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CommentTest {

    @Test public void testProbeEquivalence() {
        Notification n;
        Comment comment1 = Comment.newInstance();
        comment1.value = "a";
        n = new Notification();
        comment1.probeEquivalence(comment1, n);
        assertTrue(n.isOk());

        Comment comment2 = Comment.newInstance();
        comment2.value = "b";
        n = new Notification();
        comment2.probeEquivalence(comment2, n);
        assertTrue(n.isOk());

        n = new Notification();
        comment1.probeEquivalence(Identifier.newInstance(), n);
        assertFalse(n.isOk());
        assertEquals(
            "Probed node types mismatch: 'class de.weltraumschaf.ebnf.ast.nodes.Comment' != 'class de.weltraumschaf.ebnf.ast.nodes.Identifier'!",
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
        Comment comment1 = Comment.newInstance();
        assertEquals(1, comment1.depth());
    }
}
