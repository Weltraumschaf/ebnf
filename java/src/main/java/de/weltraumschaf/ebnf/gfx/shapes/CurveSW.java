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
public class CurveSW extends Empty {

    @Override
    public void paint(final Graphics2D graphic) {
        super.paint(graphic);
        backupColorAndStroke(graphic);
        graphic.setStroke(Strokes.createForRail());
        graphic.setColor(Color.BLACK);
        final Point pos = getPosition();
        final Dimension size = getSize();
        graphic.draw(new Arc2D.Float(pos.x - (size.width / 2) - 1,
                                     pos.y + size.height / 2,
                                     size.width,
                                     size.height + 1,
                                     0, 90, Arc2D.OPEN));
        resotreColorAndStroke(graphic);
    }
}
