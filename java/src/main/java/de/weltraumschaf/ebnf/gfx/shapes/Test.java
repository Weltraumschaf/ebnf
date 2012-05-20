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
public class Test extends Empty {

    @Override
    public void paint(final Graphics2D graphic) {
        super.paint(graphic);
        final Point pos = getPosition();
        final Dimension size = getSize();
        backupColorAndStroke(graphic);
        graphic.setColor(Color.BLUE);
        graphic.setStroke(Strokes.createForDebug());

        for (int i = 0; i < size.width; ++i) {
            for(int j = 0; j < size.height; j++) {
                final int xPosition = pos.x + i;
                final int yPosition = pos.y + j;

                if (i % 2 == 1 && yPosition % 2 == 1 || i % 2 == 0 && yPosition % 2 == 0) {
                    graphic.drawLine(xPosition, yPosition, xPosition, yPosition);
                }
            }
        }

        resotreColorAndStroke(graphic);
    }

}
