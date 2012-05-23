/*
 * LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a beer in return.
 *
 */

package de.weltraumschaf.ebnf.gfx;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class LineTest {

    @Test public void create() {
        Line sut = new Line();
        assertEquals(new Point(0, 0), sut.start);
        assertEquals(new Point(0, 0), sut.end);

        final Point start = new Point(5, 6);
        final Point end = new Point(50, 60);
        sut = new Line(start, end);
        assertEquals(start, sut.start);
        assertEquals(end, sut.end);
    }

    @Test public void testToString() {
        Line sut = new Line();
        assertEquals(String.format("Line{start=%s, end=%s}", new Point(), new Point()),
                     sut.toString());
        final Point start = new Point(5, 6);
        final Point end = new Point(50, 60);
        sut = new Line(start, end);
        assertEquals(String.format("Line{start=%s, end=%s}", start, end), sut.toString());
    }

}
