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

import de.weltraumschaf.ebnf.gfx.StringPainter;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract public class AbstractTextShape extends Empty implements Adjustable {

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

    protected int calculateWidth(final int boxSize) {
        final int minWidth = (boxSize + 2 * H_PADDING);
        final int emtpyShapeCount = (int)Math.ceil(minWidth / DEFAULT_WIDTH) + 1;
        return DEFAULT_WIDTH * emtpyShapeCount;
    }

    protected int getCenterY() {
        return getPosition().y + (int)Math.floor(getSize().height / 2);
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
            final Rectangle2D textBounds = createStringPainter(graphic).getStringBounds(getText());
            textSize = new Dimension((int)Math.ceil(textBounds.getWidth()), (int)Math.ceil(textBounds.getHeight()));
        }
        return textSize;
    }
}