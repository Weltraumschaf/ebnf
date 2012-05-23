package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.Notification;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for Terminal.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class TerminalTest {

    @Test public void testProbeEquivalence() {
        Notification notification;
        final Terminal term1 = Terminal.newInstance();
        term1.value = "a";
        notification = new Notification();
        term1.probeEquivalence(term1, notification);
        assertTrue(notification.isOk());

        final Terminal term2 = Terminal.newInstance();
        term2.value = "b";
        notification = new Notification();
        term2.probeEquivalence(term2, notification);
        assertTrue(notification.isOk());

        term1.probeEquivalence(Identifier.newInstance(), notification);
        assertFalse(notification.isOk());
        assertEquals("Probed node types mismatch: 'class de.weltraumschaf.ebnf.ast.nodes.Terminal' "
                   + "!= 'class de.weltraumschaf.ebnf.ast.nodes.Identifier'!",
            notification.report()
        );

        notification = new Notification();
        term1.probeEquivalence(term2, notification);
        assertFalse(notification.isOk());
        assertEquals("Terminal value mismatch: 'a' != 'b'!", notification.report());

        notification = new Notification();
        term2.probeEquivalence(term1, notification);
        assertFalse(notification.isOk());
        assertEquals("Terminal value mismatch: 'b' != 'a'!", notification.report());
    }

    @Test public void testDepth() {
        final Terminal term1 = Terminal.newInstance();
        assertEquals(1, term1.depth());
    }

    @Test public void testToString() {
        final Terminal term = Terminal.newInstance();
        assertEquals("<TERMINAL value=>", term.toString());
        term.value = "foo";
        assertEquals("<TERMINAL value=foo>", term.toString());
    }
}
