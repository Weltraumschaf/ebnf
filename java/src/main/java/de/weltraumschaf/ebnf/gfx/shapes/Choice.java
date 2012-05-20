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

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Choice extends AbstractCompund {

    public Choice() {
        super(FACOTRY.grid()
                     .append(FACOTRY.column())
                     .append(FACOTRY.column())
                     .append(FACOTRY.column()));
    }

    public Choice addChoice(final Shape shape) {
        final int rowCount = grid.counRows();
        Shape first, last;

        if (rowCount == 0) {
            first = new HForkSW();
            last  = new HForkSE();
        } else {
            if (rowCount > 1) {
                grid.set(0, rowCount - 1, new VForkNE());
                grid.set(2, rowCount - 1, new VForkNW());
            }

            first =  new CurveNE();
            last  =  new CurveNW();
        }

        grid.set(0, rowCount, first);
        grid.set(1, rowCount, shape);
        grid.set(2, rowCount, last);
        return this;
    }

}
