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
public class Loop extends AbstractCompund {

    public Loop() {
        super(FACOTRY.grid()
                     .append(FACOTRY.column().append(new HForkSE(), new CurveNE()))
                     .append(FACOTRY.column().append(FACOTRY.empty(), new StraightWE()))
                     .append(FACOTRY.column().append(new HForkSW(), new CurveNW())));
    }

    public void setLooped(final Shape shape) {
        grid.set(1, 0, shape);
    }

    public void setAdditional(final Shape shape) {
        grid.set(1, 1, shape);
    }

}
