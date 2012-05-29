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
import de.weltraumschaf.ebnf.gfx.Strokes;
import static de.weltraumschaf.ebnf.gfx.shapes.ShapeFactory.identifier;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class IdentifierTest {

    static class IdentifierStub extends Identifier {

        Dimension calcTextSize = new Dimension();

        public IdentifierStub(final String text) {
            super(text);
        }

        @Override
        protected Dimension calculateTextSize(final Graphics2D graphic) {
            return calcTextSize;
        }

        void setCalculatedTextSize(final Dimension calcTextSize) {
            this.calcTextSize = calcTextSize;
        }

    }

    @Test public void font() {
        final String value = "foobar";
        final AbstractTextShape ident = identifier(value);
        assertEquals(value, ident.getText());
        assertEquals(StringPainter.SANSERIFIT, ident.getFont());
    }

    @Test public void calcBoxSize() {
        final IdentifierStub ident = new IdentifierStub("foobar");
        assertEquals(new Dimension(10, 0), ident.calcBoxSize(null));
        ident.setCalculatedTextSize(new Dimension(100, 16));
        assertEquals(new Dimension(110, 16), ident.calcBoxSize(null));
    }

    @Test public void adjust() {
        final IdentifierStub ident = new IdentifierStub("foobar");
        ident.adjust(null);
        assertEquals(new Dimension(31, 31), ident.getSize());

        ident.setCalculatedTextSize(new Dimension(100, 16));
        ident.adjust(null);
        assertEquals(new Dimension(124, 31), ident.getSize());
    }

    @Test public void paint() {
        final String value = "foobar";
        final FontMetrics metrics = mock(FontMetrics.class);
        when(metrics.stringWidth(value)).thenReturn(80);
        when(metrics.getAscent()).thenReturn(12);
        when(metrics.getDescent()).thenReturn(4);

        final Graphics2D graphics = mock(Graphics2D.class);
        when(graphics.getFontMetrics()).thenReturn(metrics);

        final IdentifierStub ident = new IdentifierStub(value);
        ident.setCalculatedTextSize(new Dimension(100, 16));
        ident.paint(graphics);

        verify(graphics, times(1)).setColor(Color.BLACK);
        verify(graphics, times(1)).setStroke(Strokes.createForBox());
        verify(graphics, times(1)).drawRect(7, 7, 110, 16);
        verify(graphics, times(1)).setStroke(Strokes.createForRail());
        verify(graphics, times(1)).drawLine(0, 15, 7, 15);
        verify(graphics, times(1)).drawLine(117, 15, 124, 15);
        verify(graphics, times(1)).setFont(StringPainter.SANSERIFIT);
        verify(graphics, times(1)).drawString(value, 22, 19);
    }
}
