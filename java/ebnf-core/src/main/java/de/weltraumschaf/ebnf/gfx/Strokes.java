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

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 * See http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Strokes {
    private static final int RAIL_LINE  = 5;
    private static final int BOX_LINE   = 3;
    private static final int DEBUG_LINE = 1;

    private static final Stroke DEBUG         = new BasicStroke(DEBUG_LINE,
                                                                BasicStroke.CAP_BUTT,
                                                                BasicStroke.JOIN_MITER);
    private static final Stroke RAIL          = new BasicStroke(RAIL_LINE,
                                                                BasicStroke.CAP_BUTT,
                                                                BasicStroke.JOIN_MITER);
    private static final Stroke RAIL_ROUNDED  = new BasicStroke(RAIL_LINE,
                                                                BasicStroke.CAP_ROUND,
                                                                BasicStroke.JOIN_ROUND);
    private static final Stroke BOX           = new BasicStroke(BOX_LINE,
                                                                BasicStroke.CAP_BUTT,
                                                                BasicStroke.JOIN_MITER);

    private Strokes() { }

    public static Stroke createForDebug() {
        return DEBUG;
    }

    public static Stroke createForRail() {
        return createForRail(false);
    }

    public static Stroke createForRail(final boolean rounded) {
        return rounded ? RAIL_ROUNDED : RAIL;
    }

    public static Stroke createForBox() {
        return BOX;
    }
}
