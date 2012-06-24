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

import com.google.common.base.Objects;

/**
 * Represents a immutable point with a x and y coordinate.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Point {

    /**
     * Immutable x coordinate.
     */
    public final int x; //NOPMD
    /**
     * Immutable x coordinate.
     */
    public final int y; //NOPMD

    /**
     * Initializes a point with coordinates (0, 0).
     */
    public Point() {
        this(0, 0);
    }

    /**
     * Initializes a point with given coordinates.
     *
     * @param x X-coordinate.
     * @param y Y-Coordinate.
     */
    public Point(final int x, final int y) { //NOPMD
        this.x = x;
        this.y = y;
    }

    /**
     * Returns a new point object with changed x-coordinate.
     *
     * @param x The new x-coordinate.
     * @return
     */
    public Point setX(final int x) { //NOPMD
        return new Point(x, y);
    }

    /**
     * Returns a new point object with changed y-coordinate.
     *
     * @param y The new x-coordinate.
     * @return
     */
    public Point setY(final int y) { //NOPMD
        return new Point(x, y);
    }

    /**
     * String representation of hte point.
     *
     * @return
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .add("x", x)
                      .add("y", y)
                      .toString();
    }

    /**
     * Checks for equality.
     *
     * @param obj Object to check against.
     * @return
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Point other = (Point) obj;

        if (Objects.equal(x, other.x) && Objects.equal(y, other.y)) {
            return true;
        }

        return false;
    }

    /**
     * Calculates the hash code.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(x, y);
    }

}
