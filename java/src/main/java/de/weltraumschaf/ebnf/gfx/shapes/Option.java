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

import java.awt.Dimension;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Option extends AbstractCompund {

    public Option() {
        super(FACOTRY.grid()
                    .append(FACOTRY.column().append(new HForkSW()))
                    .append(FACOTRY.column().append(FACOTRY.empty()))
                    .append(FACOTRY.column().append(new HForkSE())));
    }

    public void setOptional(final Shape shape) {
        grid.set(1, 0, shape);
        final Dimension size = shape.getSize();
        int row = 1;

        if (DEFAULT_HEIGHT < size.height) {
            final int count = size.height % DEFAULT_HEIGHT + 1;

            for (; row <= count; ++row) {
                grid.set(0, row, FACOTRY.straightNorthSouth());
                grid.set(2, row, FACOTRY.straightNorthSouth());
            }
        }

        grid.set(0, row, new CurveNE());
        grid.set(1, row - 1, FACOTRY.straightWestEast());
        grid.set(2, row, new CurveNW());
    }

}
