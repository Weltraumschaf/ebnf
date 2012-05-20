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

import de.weltraumschaf.ebnf.gfx.ShapeFactory;
import java.awt.Dimension;
import java.awt.Graphics2D;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SequenceTest {

    private static final ShapeFactory FACTORY = ShapeFactory.getInstance();

    @Test public void testGetSize() {
        final Sequence sequence = FACTORY.sequence();
        Dimension size = sequence.getSize();
        assertEquals(0, size.width);
        assertEquals(0, size.height);
        sequence.append(FACTORY.empty());
        sequence.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        size = sequence.getSize();
        assertEquals(Shape.DEFAULT_WIDTH, size.width);
        assertEquals(Shape.DEFAULT_HEIGHT, size.height);
        sequence.append(FACTORY.empty());
        sequence.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        size = sequence.getSize();
        assertEquals(2 * Shape.DEFAULT_WIDTH, size.width);
        assertEquals(Shape.DEFAULT_WIDTH, size.height);
        sequence.append(FACTORY.empty());
        sequence.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        size = sequence.getSize();
        assertEquals(3 * Shape.DEFAULT_WIDTH, size.width);
        assertEquals(Shape.DEFAULT_HEIGHT, size.height);
        sequence.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        sequence.set(0, FACTORY.empty());
        size = sequence.getSize();
        assertEquals(3 * Shape.DEFAULT_WIDTH, size.width);
        assertEquals(Shape.DEFAULT_HEIGHT, size.height);
    }

    @Ignore
    @Test public void testSetShape() {
        final Shape empty0 = FACTORY.empty(), empty1 = FACTORY.empty(), empty2 = FACTORY.empty(),
                    empty3 = FACTORY.empty(), empty7 = FACTORY.empty();
        final Sequence sequence = FACTORY.sequence();
        assertEquals(0, sequence.countShapes());
        sequence.append(empty0, empty1, empty2, empty3);
        assertEquals(4, sequence.countShapes());
        sequence.set(7, empty7);
        assertEquals(8, sequence.countShapes());
        assertSame(empty0, sequence.get(0));
        assertSame(empty1, sequence.get(1));
        assertSame(empty2, sequence.get(2));
        assertSame(empty3, sequence.get(3));
        assertSame(empty7, sequence.get(7));
        assertTrue(sequence.get(4) instanceof Empty);
        assertTrue(sequence.get(5) instanceof Empty);
        assertTrue(sequence.get(6) instanceof Empty);
    }

    @Test public void testAppendShape() {
        final Sequence sequence = FACTORY.sequence();
        assertEquals(0, sequence.countShapes());
        sequence.append(FACTORY.empty());
        assertEquals(1, sequence.countShapes());
        sequence.append(FACTORY.empty());
        assertEquals(2, sequence.countShapes());
        sequence.append(FACTORY.empty());
        assertEquals(3, sequence.countShapes());
    }

    @Test public void testPaintRow() {
        final Sequence sequence = FACTORY.sequence();
        final Graphics2D graphics = mock(Graphics2D.class);
        final Shape shape1 = mock(Shape.class);
        when(shape1.getSize()).thenReturn(new Dimension());
        sequence.append(shape1);
        final Shape shape2 = mock(Shape.class);
        when(shape2.getSize()).thenReturn(new Dimension());
        sequence.append(shape2);
        sequence.paint(graphics);
        verify(shape1, times(1)).paint(graphics);
        verify(shape2, times(1)).paint(graphics);
    }
}
