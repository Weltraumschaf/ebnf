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

package de.weltraumschaf.ebnf.gfx.shapes;

import static de.weltraumschaf.ebnf.gfx.ShapeFactory.Curves.*;
import static de.weltraumschaf.ebnf.gfx.ShapeFactory.Straights.WEST_EAST;
import static de.weltraumschaf.ebnf.gfx.ShapeFactory.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Loop extends AbstractCompund {

    public Loop() {
        super(grid().set(0, 0, column().append(fork(WEST_EAST, SOUTH_EAST)))
                    .set(0, 1, column(curve(NORTH_EAST)))
                    .set(1, 0, empty())
                    .set(1, 1, straight(WEST_EAST))
                    .set(2, 0, column().append(fork(WEST_EAST, SOUTH_WEST)))
                    .set(2, 1, column().append(curve(NORTH_WEST))));
    }

    public void setLooped(final Shape shape) {
        grid.set(1, 0, shape);
    }

    public void setAdditional(final Shape shape) {
        grid.set(1, 1, shape);
    }

}
