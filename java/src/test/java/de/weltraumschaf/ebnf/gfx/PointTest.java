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

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import org.junit.Test;

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

    @Test public void testHashCode() {
        final Point point1 = new Point(11, 22);
        final Point point2 = new Point(11, 22);
        final Point point3 = new Point(10, 20);
        assertThat(point1.hashCode(), equalTo(point2.hashCode()));
        assertThat(point1.hashCode(), not(equalTo(point3.hashCode())));
        assertThat(point2.hashCode(), not(equalTo(point3.hashCode())));
    }

    @Test public void testEquals() {
        final Point point1 = new Point(11, 22);
        final Point point2 = new Point(11, 22);
        final Point point3 = new Point(10, 20);
        final Point nullPoint = null;
        assertFalse(point1.equals(nullPoint));
        assertFalse(point2.equals(nullPoint));
        assertFalse(point3.equals(nullPoint));
        assertFalse(point1.equals(new Object()));
        assertFalse(point2.equals(new Object()));
        assertFalse(point3.equals(new Object()));
        assertThat(point1, equalTo(point2));
        assertThat(point1, not(equalTo(point3)));
        assertThat(point2, not(equalTo(point3)));
    }
}
