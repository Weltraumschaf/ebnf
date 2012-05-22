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
import static de.weltraumschaf.ebnf.gfx.ShapeFactory.Straights.NORT_SOUTH;
import static de.weltraumschaf.ebnf.gfx.ShapeFactory.Straights.WEST_EAST;
import static de.weltraumschaf.ebnf.gfx.ShapeFactory.*;
import java.awt.Dimension;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Option extends AbstractCompund {

    public Option() {
        super(grid().set(0, 0, column().append(fork(WEST_EAST, SOUTH_WEST)))
                    .set(0, 1, curve(NORTH_EAST))
                    .set(1, 0, empty())
                    .set(1, 1, straight(WEST_EAST))
                    .set(2, 0, column().append(fork(WEST_EAST, SOUTH_EAST)))
                    .set(2, 1, curve(NORTH_WEST)));
    }

    public void setOptional(final Shape shape) {
        grid.set(1, 0, shape);
        final Dimension size = shape.getSize();

        if (DEFAULT_HEIGHT < size.height) {
            final int count = (int)Math.ceil(size.height / DEFAULT_HEIGHT) - 1;

            for (int i = 0; i < count; ++i) {
                ((ColumnLayout)grid.get(0, 0)).append(straight(NORT_SOUTH));
                ((ColumnLayout)grid.get(2, 0)).append(straight(NORT_SOUTH));
            }
        }
    }

}
