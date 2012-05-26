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

    /**
     * Size of the padded box around the text.
     */
    private Dimension boxSize = null;

    public Identifier(final String text) {
        super(text, StringPainter.SANSERIFIT);
    }

    protected Dimension calcBoxSize(final Graphics2D graphic) {
        final Dimension textSize = calculateTextSize(graphic);
        return new Dimension(textSize.width + H_PADDING * 2, textSize.height);
    }

    @Override
    public void adjust(final Graphics2D graphic) {
        boxSize = calcBoxSize(graphic);
        setSize(new Dimension(calculateWidth(boxSize.width), DEFAULT_HEIGHT));
    }

    @Override
    public void paint(final Graphics2D graphic) {
        // @todo Write tests for this method.
        if (null == boxSize) {
            adjust(graphic);
        }

        final Point pos     = getPosition();
        final Dimension size = getSize();
        final int vCenter   = getCenterY();
        final int hPaddingp = (size.width - boxSize.width) / 2;
        final int vPadding  = (size.height - boxSize.height) / 2;
        final Point rectanglePosition = new Point(pos.x + hPaddingp, pos.y + vPadding);
        final Point inLineStart       = new Point(pos.x, vCenter);
        final Point inLineEnd         = new Point(pos.x + hPaddingp, vCenter);
        final Point outLineStart      = new Point(pos.x + hPaddingp + boxSize.width, vCenter);
        final Point outLineEnd        = new Point(pos.x + size.width, vCenter);

        super.paint(graphic);
        backupColorAndStroke(graphic);
        graphic.setColor(Color.BLACK);
        graphic.setStroke(Strokes.createForBox());
        graphic.drawRect(rectanglePosition.x, rectanglePosition.y, boxSize.width, boxSize.height);
        graphic.setStroke(Strokes.createForRail());
        graphic.drawLine(inLineStart.x, inLineStart.y, inLineEnd.x, inLineEnd.y);
        graphic.drawLine(outLineStart.x, outLineStart.y, outLineEnd.x, outLineEnd.y);
        final StringPainter textPainter = createStringPainter(graphic);
        textPainter.drawCenteredString(getText(), rectanglePosition, boxSize);
        resotreColorAndStroke(graphic);
    }

}
