package de.weltraumschaf.ebnf.ast;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for Notification.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class NotificationTest {

    @Test public void errorCollectingAndReporting() {
        Notification n = new Notification();
        assertTrue(n.isOk());
        assertEquals("", n.report());
        n.error("An error!");
        assertFalse(n.isOk());
        n.error("Some %s occured at %s!", "FOO", "BAR");
        assertFalse(n.isOk());
        n.error("Error: %s at line %d occued because %s!", "SNAFU", 5, "FOOBAR");
        assertEquals(
            "An error!\n" +
            "Some FOO occured at BAR!\n" +
            "Error: SNAFU at line 5 occued because FOOBAR!", n.report());
    }

}
