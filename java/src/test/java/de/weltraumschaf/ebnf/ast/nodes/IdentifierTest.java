package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.Notification;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for Identifier.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class IdentifierTest {

    @Test public void testProbeEquivalence() {
        Notification notifiaction;

        final Identifier ident1 = Identifier.newInstance();
        ident1.value = "a";
        notifiaction = new Notification();
        ident1.probeEquivalence(ident1, notifiaction);
        assertTrue(notifiaction.isOk());

        final Identifier ident2 = Identifier.newInstance();
        ident2.value = "b";
        notifiaction = new Notification();
        ident2.probeEquivalence(ident2, notifiaction);
        assertTrue(notifiaction.isOk());

        ident1.probeEquivalence(Terminal.newInstance(), notifiaction);
        assertFalse(notifiaction.isOk());
        assertEquals("Probed node types mismatch: "
            + "'class de.weltraumschaf.ebnf.ast.nodes.Identifier' != "
            + "'class de.weltraumschaf.ebnf.ast.nodes.Terminal'!",
            notifiaction.report()
        );

        notifiaction = new Notification();
        ident1.probeEquivalence(ident2, notifiaction);
        assertFalse(notifiaction.isOk());
        assertEquals("Identifier value mismatch: 'a' != 'b'!", notifiaction.report());

        notifiaction = new Notification();
        ident2.probeEquivalence(ident1, notifiaction);
        assertFalse(notifiaction.isOk());
        assertEquals("Identifier value mismatch: 'b' != 'a'!", notifiaction.report());
    }

    @Test public void testDepth() {
        final Identifier ident = Identifier.newInstance();
        assertEquals(1, ident.depth());
    }

    @Test public void testToString() {
        final Identifier ident = Identifier.newInstance();
        assertEquals("<IDENTIFIER value=>", ident.toString());
        ident.value = "foo";
        assertEquals("<IDENTIFIER value=foo>", ident.toString());
    }
}
