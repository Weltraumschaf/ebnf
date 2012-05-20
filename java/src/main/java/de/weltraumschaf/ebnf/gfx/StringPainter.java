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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * http://stackoverflow.com/questions/5378052/positioning-string-in-graphic-java
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class StringPainter {

    public static final Font MONOSPACED = new Font("Monospaced", Font.PLAIN, 16);
    public static final Font SANSERIFIT = new Font("Sanserif", Font.ITALIC, 16);
    public static final Font SANSERIF   = new Font("Sanserif", Font.PLAIN, 16);

    private static final Graphics2D DEFAULT_GRAPHIC =
        new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();

    private final Graphics2D graphic;
    private final Font font;
    private final FontRenderContext renderContext;

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
        this.graphic       = graphic;
        this.font          = font;
        this.renderContext = graphic.getFontRenderContext();
    }

    public Rectangle2D getStringBounds(final String str) {
        return font.getStringBounds(str, renderContext);
    }

    public LineMetrics getLineMetrics(final String str) {
        return font.getLineMetrics(str, renderContext);
    }

    public void drawCenteredString(final String str, final int width, final int height) {
        drawCenteredString(str, 0, 0, width, height);
    }

    public void drawCenteredString(final String str, final int offsetX, final int offsetY, final int width, final int height) {
        final Font backup = graphic.getFont();
        graphic.setFont(font);
        final FontMetrics metrics = graphic.getFontMetrics();
        final int xPosition = (width - metrics.stringWidth(str)) / 2 + offsetX;
        final int yPosition = (metrics.getAscent() + (height - (metrics.getAscent() + metrics.getDescent())) / 2) + offsetY;
        graphic.drawString(str, xPosition, yPosition);
        graphic.setFont(backup);
    }
}
