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
import static de.weltraumschaf.ebnf.gfx.shapes.ShapeFactory.rule;
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
public class RuleTest {

    static class RuleStub extends Rule {

        Dimension calcTextSize = new Dimension();

        public RuleStub(final String text) {
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
        final String name = "foobar";
        final AbstractTextShape rule = rule(name);
        assertEquals(name, rule.getText());
        assertEquals(StringPainter.SANSERIF, rule.getFont());
    }

    @Test public void adjust() {
        final RuleStub rule = new RuleStub("foobar");
        rule.adjust(null);
        assertEquals(new Dimension(31, 31), rule.getSize());

        rule.setCalculatedTextSize(new Dimension(100, 16));
        rule.adjust(null);
        assertEquals(new Dimension(124, 31), rule.getSize());
    }

    @Test public void paint() {
        final String name = "foobar";
        final FontMetrics metrics = mock(FontMetrics.class);
        when(metrics.stringWidth(name)).thenReturn(80);
        when(metrics.getAscent()).thenReturn(12);
        when(metrics.getDescent()).thenReturn(4);

        final Graphics2D graphics = mock(Graphics2D.class);
        when(graphics.getFontMetrics()).thenReturn(metrics);

        final RuleStub rule = new RuleStub(name);
        rule.setCalculatedTextSize(new Dimension(100, 16));
        rule.paint(graphics);

        verify(graphics, times(1)).setColor(Color.BLACK);
        verify(graphics, times(1)).setFont(StringPainter.SANSERIF);
        verify(graphics, times(1)).drawString(name, 15, 19);
    }
}
