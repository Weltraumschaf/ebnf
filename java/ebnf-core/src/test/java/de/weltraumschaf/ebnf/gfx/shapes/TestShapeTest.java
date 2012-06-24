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

import de.weltraumschaf.ebnf.gfx.Strokes;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class TestShapeTest {

    @Test public void paint() {
        final Graphics2D graphics = mock(Graphics2D.class);
        final TestShape test = new TestShape();
        test.setSize(new Dimension(5, 5));
        test.paint(graphics);
        verify(graphics, times(1)).setColor(Color.BLUE);
        verify(graphics, times(1)).setStroke(Strokes.createForDebug());
        verify(graphics, times(13)).drawLine(anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test public void shouldPaint() {
        final TestShape test = new TestShape();

        assertTrue(test.shouldPaint(0, 0));
        assertFalse(test.shouldPaint(1, 0));
        assertTrue(test.shouldPaint(2, 0));
        assertFalse(test.shouldPaint(3, 0));
        assertTrue(test.shouldPaint(4, 0));
        assertFalse(test.shouldPaint(5, 0));

        assertFalse(test.shouldPaint(0, 1));
        assertTrue(test.shouldPaint(1, 1));
        assertFalse(test.shouldPaint(2, 1));
        assertTrue(test.shouldPaint(3, 1));
        assertFalse(test.shouldPaint(4, 1));
        assertTrue(test.shouldPaint(5, 1));

        assertTrue(test.shouldPaint(0, 2));
        assertFalse(test.shouldPaint(1, 2));
        assertTrue(test.shouldPaint(2, 2));
        assertFalse(test.shouldPaint(3, 2));
        assertTrue(test.shouldPaint(4, 2));
        assertFalse(test.shouldPaint(5, 2));
    }

    @Test public void throwExceptionOnNEgateIterations() {
        final TestShape test = new TestShape();

        try {
            test.shouldPaint(-2, 0);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException ex) {
            assertEquals("Passed horizIteration must be greater equal than 0! -2 given.", ex.getMessage());
        }

        try {
            test.shouldPaint(0, -3);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException ex) {
            assertEquals("Passed verticalIteration must be greater equal than 0! -3 given.", ex.getMessage());
        }
    }
}
