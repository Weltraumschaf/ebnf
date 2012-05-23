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
 * Represents a geometric line with a {@link Point start} and {@link Point end}.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Line {

    /**
     * Start point of line.
     */
    public final Point start;
    /**
     * End point of line.
     */
    public final Point end;

    /**
     * Initializes a line with both start and end with points to (0, 0).
     */
    public Line() {
        this(new Point(), new Point());
    }

    /**
     * Designated constructor.
     *
     * @param start
     * @param end
     */
    public Line(final Point start, final Point end) {
        this.start = start;
        this.end = end;
    }

    /**
     * String representation.
     *
     * @return
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("start", start).add("end", end).toString();
    }

}
