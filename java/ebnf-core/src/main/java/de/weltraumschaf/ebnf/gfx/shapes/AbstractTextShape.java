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

import de.weltraumschaf.ebnf.gfx.Line;
import de.weltraumschaf.ebnf.gfx.Point;
import de.weltraumschaf.ebnf.gfx.StringPainter;
import de.weltraumschaf.ebnf.gfx.Strokes;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class AbstractTextShape extends Empty implements Adjustable {

    protected static final int H_PADDING = 5;
    private static final Font DEFUALT_FONT = StringPainter.SANSERIF;

    private final String text;
    private final Font font;

    private StringPainter textPainter = null;
    private Graphics2D lastContext = null;
    private Dimension textSize = null;

    public AbstractTextShape(final String text) {
        this(text, DEFUALT_FONT);
    }

    public AbstractTextShape(final String text, final Font font) {
        super();
        this.text = text;
        this.font = font;
    }

    public String getText() {
        return text;
    }

    Font getFont() {
        return font;
    }

    protected static int calculateWidth(final int boxWidth) {
        if (boxWidth < 0) {
            throw new IllegalArgumentException("box width need to be greater or equal zero!");
        }

        final int minWidth = (boxWidth + 2 * H_PADDING);
        final int emtpyShapeCount = minWidth / DEFAULT_WIDTH + 1;
        return DEFAULT_WIDTH * emtpyShapeCount;
    }

    protected StringPainter createStringPainter(final Graphics2D graphics) {
        if (null == textPainter || !lastContext.equals(graphics)) {
            textPainter = new StringPainter(graphics, font);
            lastContext = graphics;
        }
        return textPainter;
    }

    protected Dimension calculateTextSize(final Graphics2D graphic) {
        if (null == textSize) {
            final Rectangle2D textBounds = font.getStringBounds(getText(),
                                                                graphic.getFontRenderContext());
            textSize = new Dimension((int) Math.ceil(textBounds.getWidth()),
                                     (int) Math.ceil(textBounds.getHeight()));
        }
        return textSize;
    }

    protected Line calculateInLine(final int boxWidth) {
        final Point pos      = getPosition();
        final int vCenter    = getCenterY();
        final Point start    = new Point(pos.x, vCenter);
        final Point end      = new Point(pos.x + calculateHPadding(boxWidth), vCenter);
        return new Line(start, end);
    }

    protected Line calculateOutLine(final int boxWidth) {
        final Point pos      = getPosition();
        final int vCenter    = getCenterY();
        final Point start    = new Point(pos.x + calculateHPadding(boxWidth) + boxWidth, vCenter);
        final Point end      = new Point(pos.x + getSize().width, vCenter);
        return new Line(start, end);
    }

    protected int calculateHPadding(final int boxWidth) {
        return (getSize().width - boxWidth) / 2;
    }

    protected int calculateVPadding(final int boxHeight) {
        return (getSize().height - boxHeight) / 2;
    }

    protected Point calculatePaddedRectanglePosition(final Dimension size) {
        final Point pos = getPosition();
        return new Point(pos.x + calculateHPadding(size.width),
                         pos.y + calculateVPadding(size.height));
    }

    protected void drawLine(final Graphics2D graphic, final Line line) {
        graphic.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
    }

    protected void drawText(final Graphics2D graphic, final Point pos, final Dimension size) {
        final StringPainter painter = createStringPainter(graphic);
        painter.drawCenteredString(getText(), pos, size);
    }

    protected void drawTextWithInAndOutLine(final Graphics2D graphic, final Point pos, final Dimension size) {
        graphic.setStroke(Strokes.createForRail());
        drawLine(graphic, calculateInLine(size.width));
        drawLine(graphic, calculateOutLine(size.width));
        drawText(graphic, pos, size);
    }
}
