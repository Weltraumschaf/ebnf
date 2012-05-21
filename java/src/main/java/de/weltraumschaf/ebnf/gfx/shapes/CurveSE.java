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

import de.weltraumschaf.ebnf.gfx.Point;
import java.awt.Dimension;
import java.awt.geom.Arc2D;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CurveSE extends AbstractCurve {

    @Override
    protected Arc2D createArc() {
        return createArc(90, 90);
    }

    @Override
    protected Point calcArcPosition() {
        final Point pos      = getPosition();
        final Dimension size = getSize();
        return new Point(pos.x + size.width / 2, pos.y + size.height / 2);
    }

    @Override
    protected Dimension calcArcDimenson() {
        final Dimension size = getSize();
        return new Dimension(size.width + 1, size.height + 1);
    }
    
}
