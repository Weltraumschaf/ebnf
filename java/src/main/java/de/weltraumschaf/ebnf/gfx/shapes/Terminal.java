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
import de.weltraumschaf.ebnf.gfx.StringPainter;
import de.weltraumschaf.ebnf.gfx.Strokes;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Terminal extends AbstractTextShape implements Shape {

    /**
     * Width of the rounded rectangle arcs.
     */
    private static final int ARC_WIDTH = 25;

    /**
     * Padded box size of the text.
     */
    private Dimension boxSize = null;

    public Terminal(final String text) {
        super(text, StringPainter.MONOSPACED);
    }

    protected Dimension calcBoxSize(final Graphics2D graphic) {
        final Dimension textSize = calculateTextSize(graphic);
        return new Dimension(textSize.width + H_PADDING * 4, textSize.height + 4);
    }

    @Override
    public void adjust(final Graphics2D graphic) {
        boxSize = calcBoxSize(graphic);
        setSize(new Dimension(calculateWidth(boxSize.width), DEFAULT_HEIGHT));
    }

    @Override
    public void paint(final Graphics2D graphic) {
        if (null == boxSize) {
            adjust(graphic);
        }

        final Point rectanglePosition = calculatePaddedRectanglePosition(boxSize);

        super.paint(graphic);
        backupColorAndStroke(graphic);

        graphic.setColor(Color.BLACK);
        graphic.setStroke(Strokes.createForBox());
        graphic.drawRoundRect(rectanglePosition.x, rectanglePosition.y,
                              boxSize.width, boxSize.height,
                              ARC_WIDTH, ARC_WIDTH);

        drawTextWithInAndOutLine(graphic, rectanglePosition, boxSize);

        resotreColorAndStroke(graphic);
    }

}
