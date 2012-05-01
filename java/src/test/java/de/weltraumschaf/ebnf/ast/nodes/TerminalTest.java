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
        Notification n;
        Terminal term1 = Terminal.newInstance();
        term1.value = "a";
        n = new Notification();
        term1.probeEquivalence(term1, n);
        assertTrue(n.isOk());

        Terminal term2 = Terminal.newInstance();
        term2.value = "b";
        n = new Notification();
        term2.probeEquivalence(term2, n);
        assertTrue(n.isOk());

        term1.probeEquivalence(Identifier.newInstance(), n);
        assertFalse(n.isOk());
        assertEquals(
            "Probed node types mismatch: 'class de.weltraumschaf.ebnf.ast.nodes.Terminal' != 'class de.weltraumschaf.ebnf.ast.nodes.Identifier'!",
            n.report()
        );

        n = new Notification();
        term1.probeEquivalence(term2, n);
        assertFalse(n.isOk());
        assertEquals("Terminal value mismatch: 'a' != 'b'!", n.report());

        n = new Notification();
        term2.probeEquivalence(term1, n);
        assertFalse(n.isOk());
        assertEquals("Terminal value mismatch: 'b' != 'a'!", n.report());
    }

    @Test public void testDepth() {
        Terminal term1 = Terminal.newInstance();
        assertEquals(1, term1.depth());
    }

}
