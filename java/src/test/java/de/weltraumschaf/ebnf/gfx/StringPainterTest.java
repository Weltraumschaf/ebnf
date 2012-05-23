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
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class StringPainterTest {

    @Test public void createWithFont() {
        // test flawless object creation
        new StringPainter(Fonts.MONOSPACED.create()); // NOPMD
    }

    @Test public void calcXPosition() {
        final StringPainter painter = new StringPainter();
        assertEquals(0, painter.calcXPosition(10, -3, 10));
        assertEquals(5, painter.calcXPosition(10, -3, 20));
        assertEquals(15, painter.calcXPosition(10, -3, 40));
        assertEquals(33, painter.calcXPosition(233, -3, 300));

        assertEquals(0, painter.calcXPosition(10, 0, 10));
        assertEquals(5, painter.calcXPosition(10, 0, 20));
        assertEquals(15, painter.calcXPosition(10, 0, 40));
        assertEquals(33, painter.calcXPosition(233, 0, 300));

        assertEquals(2, painter.calcXPosition(10, 2, 10));
        assertEquals(7, painter.calcXPosition(10, 2, 20));
        assertEquals(17, painter.calcXPosition(10, 2, 40));
        assertEquals(35, painter.calcXPosition(233, 2, 300));
    }

    @Test public void calcYPosition() {
        final StringPainter painter = new StringPainter();
        assertEquals(7, painter.calcYPosition(0, 20, 5, 10));
        assertEquals(12, painter.calcYPosition(0, 20, 10, 5));
        assertEquals(10, painter.calcYPosition(3, 20, 5, 10));
        assertEquals(15, painter.calcYPosition(3, 20, 10, 5));
    }

    @Test public void drawCenteredString() {
        final String str = "test";
        final int stringWidth = 20;
        final int width = 100;
        final int height = 100;
        final int ascent = 5;
        final int descent = 10;
        final Font font = Fonts.SANSERIFIT.create();
        final Font backup = Fonts.SANSERIF.create();
        final FontMetrics metrics = mock(FontMetrics.class);
        when(metrics.stringWidth(str)).thenReturn(stringWidth);
        when(metrics.getAscent()).thenReturn(ascent);
        when(metrics.getDescent()).thenReturn(descent);

        final Graphics2D graphics = mock(Graphics2D.class);
        when(graphics.getFont()).thenReturn(backup);
        when(graphics.getFontMetrics()).thenReturn(metrics);

        final StringPainter painter = new StringPainter(graphics, font);
        painter.drawCenteredString(str, new Dimension(width, 100));
        verify(graphics).getFont();
        verify(graphics).setFont(font);
        verify(metrics).stringWidth(str);
        verify(metrics).getAscent();
        verify(metrics).getDescent();
        verify(graphics).drawString(str,
                                    painter.calcXPosition(stringWidth, 0, width),
                                    painter.calcYPosition(0, height, ascent, descent));
    }
}
