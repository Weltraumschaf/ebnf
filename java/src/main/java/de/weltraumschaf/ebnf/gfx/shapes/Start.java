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

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Start extends Empty {

    @Override
    public void paint(final Graphics2D graphic) {
        final Point pos = getPosition();
        final Dimension size = getSize();
        final int xPosition = pos.x + size.width / 2;
        final int yPosition = pos.y + size.height / 2;
        super.paint(graphic);
        backupColorAndStroke(graphic);
        graphic.setColor(Color.BLACK);
        graphic.setStroke(Strokes.createForRail(true));
        graphic.drawLine(xPosition, pos.y + 3, xPosition, pos.y + size.height - 4);
        graphic.setStroke(Strokes.createForRail());
        graphic.drawLine(xPosition, yPosition, pos.x + size.width, yPosition);
        resotreColorAndStroke(graphic);
    }
}
