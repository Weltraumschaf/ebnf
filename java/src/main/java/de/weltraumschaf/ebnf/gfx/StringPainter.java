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
package de.weltraumschaf.ebnf.gfx;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Paints a centered string into a rectangular area.
 *
 * Also provides some helpers for measuring text and calculate positions.
 *
 * The algorithm for position the text as centered is from:
 * http://stackoverflow.com/questions/5378052/positioning-string-in-graphic-java
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class StringPainter {

    /**
     * Default font for terminal shapes.
     */
    public static final Font MONOSPACED = Fonts.MONOSPACED.create();
    /**
     * Default font for rule shapes.
     */
    public static final Font SANSERIF   = Fonts.SANSERIF.create();
    /**
     * Default font for identifier shapes.
     */
    public static final Font SANSERIFIT = Fonts.SANSERIFIT.create();

    /**
     * Default graphics object.
     */
    private static final Graphics2D DEFAULT_GRAPHIC = Helper.newGraphics();

    /**
     * used graphics object.
     */
    private final Graphics2D graphic;
    /**
     * Used font object.
     */
    private final Font font;

    public StringPainter() {
        this(DEFAULT_GRAPHIC);
    }

    public StringPainter(final Font font) {
        this(DEFAULT_GRAPHIC, font);
    }

    public StringPainter(final Graphics2D graphic) {
        this(graphic, MONOSPACED);
    }

    public StringPainter(final Graphics2D graphic, final Font font) {
        this.graphic  = graphic;
        this.font     = font;
    }

    /**
     * Returns the bounding area of a string.
     *
     * @param str
     * @return
     */
    public Rectangle2D getStringBounds(final String str) {
        return font.getStringBounds(str, graphic.getFontRenderContext());
    }

    /**
     * Draws a string centered inside the given size.
     *
     * @param str  String to center.
     * @param size Size of the rectangle to center inside.
     */
    public void drawCenteredString(final String str, final Dimension size) {
        drawCenteredString(str, new Point(), size);
    }

    /**
     * Draws a string centered inside the given size with an x and y offset..
     *
     * @param str    String to center.
     * @param offset X and y offset.
     * @param size   Size of the rectangle to center inside.
     */
    public void drawCenteredString(final String str, final Point offset, final Dimension size) {
        final Font backup = graphic.getFont();
        graphic.setFont(font);
        final FontMetrics metrics = graphic.getFontMetrics();
        final int xPosition = calcXPosition(metrics.stringWidth(str), offset.x, size.width);
        final int yPosition = calcYPosition(offset.y, size.height, metrics.getAscent(), metrics.getDescent());
        graphic.drawString(str, xPosition, yPosition);
        graphic.setFont(backup);
    }

    /**
     * Calculates the string x position to draw.
     *
     * @param stringWidth
     * @param offsetX
     * @param width
     * @return
     */
    public int calcXPosition(final int stringWidth, final int offsetX, final int width) {
        final int _offsetX = offsetX < 0
                           ? 0
                           : offsetX;
        return (width - stringWidth) / 2 + _offsetX;
    }

    /**
     * Calculates the string y position to draw.
     *
     * @param Y
     * @param height
     * @param ascent
     * @param descent
     * @return
     */
    public int calcYPosition(final int offsetY, final int height, final int ascent, final int descent) {
        return (ascent + (height - (ascent + descent)) / 2) + offsetY;
    }
}
