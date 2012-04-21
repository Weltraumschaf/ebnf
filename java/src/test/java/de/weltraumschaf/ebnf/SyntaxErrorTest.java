package de.weltraumschaf.ebnf;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for SyntaxtError.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SyntaxErrorTest {

    @Test public void testToString() {
        SyntaxError e;
        e = new SyntaxError("foo bar", new Position(3, 4));
        assertEquals("Syntax error: foo bar at (3, 4) (code: 0)!", e.toString());
        e = new SyntaxError("foo bar", new Position(3, 4), 4);
        assertEquals("Syntax error: foo bar at (3, 4) (code: 4)!", e.toString());
        e = new SyntaxError("foo bar", new Position(3, 4, "foo.ebnf"));
        assertEquals("Syntax error: foo bar at foo.ebnf (3, 4) (code: 0)!", e.toString());
    }
}
