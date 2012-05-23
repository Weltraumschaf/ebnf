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
public class TestShape extends Empty {

    @Override
    public void paint(final Graphics2D graphic) {
        super.paint(graphic);
        final Point pos = getPosition();
        final Dimension size = getSize();
        backupColorAndStroke(graphic);
        graphic.setColor(Color.BLUE);
        graphic.setStroke(Strokes.createForDebug());

        for (int i = 0; i < size.width; ++i) {
            for (int j = 0; j < size.height; j++) {
                final int xPosition = pos.x + i;
                final int yPosition = pos.y + j;

                if (shouldPaint(i, j)) {
                    graphic.drawLine(xPosition, yPosition, xPosition, yPosition);
                }
            }
        }

        resotreColorAndStroke(graphic);
    }

    protected boolean shouldPaint(final int horizIteration, final int verticalIteration) {
        if (horizIteration < 0) {
            throw new IllegalArgumentException(String.format(
                    "Passed horizIteration must be greater equal than 0! %d given.",
                    horizIteration));
        }

        if (verticalIteration < 0) {
            throw new IllegalArgumentException(String.format(
                    "Passed verticalIteration must be greater equal than 0! %d given.",
                    verticalIteration));
        }

        if (horizIteration % 2 == 1 && verticalIteration % 2 == 1) { // NOPMD Negative arguments throw excpetions.
            return true;
        }

        if (horizIteration % 2 == 0 && verticalIteration % 2 == 0) {
            return true;
        }

        return false;
    }
}
