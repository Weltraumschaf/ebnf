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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Rule extends AbstractTextShape implements Shape {

    private Dimension textSize = null;

    public Rule(final String text) {
        super(text, StringPainter.SANSERIF);
    }

    @Override
    public void adjust(final Graphics2D graphic) {
        textSize = calculateTextSize(graphic);
        setSize(new Dimension(calculateWidth(textSize.width + H_PADDING * 2), DEFAULT_HEIGHT));
    }

    @Override
    public void paint(final Graphics2D graphic) {
        // @todo Write tests for this method.
        if (null == textSize) {
            adjust(graphic);
        }

        final Point pos = getPosition();
        final Dimension size = getSize();
        final Point textPosition = new Point(pos.x + H_PADDING,
                                             pos.y + (size.height - textSize.height) / 2);

        super.paint(graphic);
        backupColorAndStroke(graphic);
        graphic.setColor(Color.BLACK);
        final StringPainter textPainter = createStringPainter(graphic);
        textPainter.drawCenteredString(getText(), textPosition, textSize);
        resotreColorAndStroke(graphic);
    }
}
