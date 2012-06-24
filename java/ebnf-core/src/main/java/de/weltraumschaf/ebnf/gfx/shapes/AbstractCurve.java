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
import de.weltraumschaf.ebnf.gfx.Strokes;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class AbstractCurve extends Empty {

    @Override
    public void paint(final Graphics2D graphic) {
        super.paint(graphic);
        backupColorAndStroke(graphic);
        graphic.setStroke(Strokes.createForRail());
        graphic.setColor(Color.BLACK);
        graphic.draw(createArc());
        resotreColorAndStroke(graphic);
    }

    protected abstract Point calcArcPosition();
    protected abstract Dimension calcArcDimenson();
    protected abstract Arc2D createArc();

    protected Arc2D createArc(final int start, final int extent) {
        return createArc(calcArcPosition(), calcArcDimenson(), start, extent);
    }

    protected Arc2D createArc(final Point pos, final Dimension size, final int start, final int extent) {
        return new Arc2D.Float(pos.x, pos.y, size.width, size.height, start, extent, Arc2D.OPEN);
    }

}
