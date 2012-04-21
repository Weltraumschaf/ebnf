package de.weltraumschaf.ebnf;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for Position.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PositionTest {

    @Test public void testToString() {
        Position p;
        p = new Position(5, 10);
        assertEquals("(5, 10)", p.toString());

        p = new Position(7, 8, "/foo/bar/baz.ebnf");
        assertEquals("/foo/bar/baz.ebnf (7, 8)", p.toString());
    }

}
