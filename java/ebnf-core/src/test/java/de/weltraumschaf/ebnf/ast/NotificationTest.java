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
        final Notification notification = new Notification();
        assertTrue(notification.isOk());
        assertEquals("", notification.report());
        notification.error("An error!");
        assertFalse(notification.isOk());
        notification.error("Some %s occured at %s!", "FOO", "BAR");
        assertFalse(notification.isOk());
        notification.error("Error: %s at line %d occued because %s!", "SNAFU", 5, "FOOBAR");
        assertEquals("An error!\n"
                   + "Some FOO occured at BAR!\n"
                   + "Error: SNAFU at line 5 occued because FOOBAR!", notification.report());
    }

}
