package de.weltraumschaf.ebnf.parser;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit test for Position.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PositionTest {

    @Test public void testToString() {
        Position position;
        position = new Position(5, 10);
        assertEquals("(5, 10)", position.toString());

        position = new Position(7, 8, "/foo/bar/baz.ebnf");
        assertEquals("/foo/bar/baz.ebnf (7, 8)", position.toString());
    }

}
