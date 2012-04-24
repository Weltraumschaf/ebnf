package de.weltraumschaf.ebnf.ast;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Unit test for Identifier.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class IdentifierTest {

    @Test public void testProbeEquivalence() {
        Notification n;

        Identifier ident1 = new Identifier(mock(Node.class));
        ident1.value = "a";
        n = new Notification();
        ident1.probeEquivalence(ident1, n);
        assertTrue(n.isOk());

        Identifier ident2 = new Identifier(mock(Node.class));
        ident2.value = "b";
        n = new Notification();
        ident2.probeEquivalence(ident2, n);
        assertTrue(n.isOk());

        ident1.probeEquivalence(new Terminal(mock(Node.class)), n);
        assertFalse(n.isOk());
        assertEquals(
            "Probed node types mismatch: 'class de.weltraumschaf.ebnf.ast.Identifier' != 'class de.weltraumschaf.ebnf.ast.Terminal'!",
            n.report()
        );

        n = new Notification();
        ident1.probeEquivalence(ident2, n);
        assertFalse(n.isOk());
        assertEquals("Identifier value mismatch: 'a' != 'b'!", n.report());

        n = new Notification();
        ident2.probeEquivalence(ident1, n);
        assertFalse(n.isOk());
        assertEquals("Identifier value mismatch: 'b' != 'a'!",n.report());
    }

    @Test public void testDepth() {
        Identifier ident = new Identifier(mock(Node.class));
        assertEquals(1, ident.depth());
    }

}
