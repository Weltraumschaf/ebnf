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
public class Identifier extends AbstractTextShape implements Shape {

    public Identifier(final String text) {
        super(text, StringPainter.SANSERIFIT);
    }

    private Dimension boxSize = null;

    @Override
    public void adjust(final Graphics2D graphic) {
        final Dimension textSize = calculateTextSize(graphic);
        boxSize = new Dimension(textSize.width + H_PADDING * 2, textSize.height);
        setSize(new Dimension(calculateWidth(boxSize.width), DEFAULT_HEIGHT));
    }

    @Override
    public void paint(final Graphics2D graphic) {
        if (null == boxSize) {
            adjust(graphic);
        }

        final Point pos     = getPosition();
        final Dimension size = getSize();
        final int vCenter   = getCenterY();
        final int hPaddingp = (size.width - boxSize.width) / 2;
        final int vPadding  = (size.height - boxSize.height) / 2;
        final Point rectanglePosition = new Point(pos.x + hPaddingp, pos.y + vPadding),
                    inLineStart       = new Point(pos.x, vCenter),
                    inLineEnd         = new Point(pos.x + hPaddingp, vCenter),
                    outLineStart      = new Point(pos.x + hPaddingp + boxSize.width, vCenter),
                    outLineEnd        = new Point(pos.x + size.width, vCenter);

        super.paint(graphic);
        backupColorAndStroke(graphic);
        graphic.setColor(Color.BLACK);
        graphic.setStroke(Strokes.createForBox());
        graphic.drawRect(rectanglePosition.x, rectanglePosition.y, boxSize.width, boxSize.height);
        graphic.setStroke(Strokes.createForRail());
        graphic.drawLine(inLineStart.x, inLineStart.y, inLineEnd.x, inLineEnd.y);
        graphic.drawLine(outLineStart.x, outLineStart.y, outLineEnd.x, outLineEnd.y);
        final StringPainter textPainter = createStringPainter(graphic);
        textPainter.drawCenteredString(getText(), rectanglePosition.x, rectanglePosition.y, boxSize.width, boxSize.height);
        resotreColorAndStroke(graphic);
    }

}
