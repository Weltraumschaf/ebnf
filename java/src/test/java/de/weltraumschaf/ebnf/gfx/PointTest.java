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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class PointTest {

    @Test public void testCreation() {
        Point point = new Point();
        assertEquals(0, point.x);
        assertEquals(0, point.y);
        point = new Point(11, 22);
        assertEquals(11, point.x);
        assertEquals(22, point.y);
    }

    @Test public void testSetX() {
        Point point = new Point();
        assertEquals(0, point.x);
        assertEquals(0, point.y);
        point = point.setX(10);
        assertEquals(10, point.x);
        assertEquals(0, point.y);
    }

    @Test public void testSetY() {
        Point point = new Point();
        assertEquals(0, point.x);
        assertEquals(0, point.y);
        point = point.setY(10);
        assertEquals(0, point.x);
        assertEquals(10, point.y);
    }

    @Test public void testToString() {
        final Point point = new Point(11, 22);
        assertEquals("Point{x=11, y=22}", point.toString());
    }
}
