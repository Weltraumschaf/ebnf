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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Empty extends AbstractShape {

    private boolean transparent = false;

    public static Empty newEmpty() {
        return new Empty();
    }

    public static Empty[] newEmpty(final int count) {
        final Empty[] empties = new Empty[count];
        for (int i = 0; i < count; ++i) {
            empties[i] = newEmpty();
        }
        return empties;
    }

    protected void setTransparent(final boolean transparent) {
        this.transparent = transparent;
    }

    @Override
    public void paint(final Graphics2D graphic) {
        backupColorAndStroke(graphic);
        graphic.setColor(Color.WHITE);
        final Point pos = getPosition();
        final Dimension size = getSize();

        if (!transparent) {
            graphic.fillRect(pos.x, pos.y, size.width, size.height);
        }

        debugShape(graphic, new Paintable() {
            @Override
            public void paint(final Graphics2D graphic) {
                drawHorizontalLines(graphic);
                drawVerticalLines(graphic);
                graphic.drawRect(pos.x, pos.y, size.width - 1, size.height - 1);
            }

            private void drawHorizontalLines(final Graphics2D graphic) {
                final int step = (int)Math.ceil(DEFAULT_HEIGHT / 2);
                final int maxYPosition = pos.y + size.height - 1;

                for (int yPosition = pos.y + step; yPosition < maxYPosition; yPosition += step) {
                    graphic.drawLine(pos.x, yPosition, pos.x + size.width - 1, yPosition);
                }
            }

            private void drawVerticalLines(final Graphics2D graphic) {
                final int step = (int)Math.ceil(DEFAULT_WIDTH / 2);
                final int maxXPosition = pos.x + size.width - 1;

                for (int xPosition = pos.x + step; xPosition < maxXPosition; xPosition += step) {
                    graphic.drawLine(xPosition, pos.y, xPosition, pos.y + size.height -1);
                }
            }
        });

        resotreColorAndStroke(graphic);
    }

}
