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
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ColumnLayoutTest {

    private static final ShapeFactory FACTORY = ShapeFactory.getInstance();

    @Test public void testGetSize() {
        final ColumnLayout column = FACTORY.column();
        Dimension size = column.getSize();
        assertEquals(0, size.width);
        assertEquals(0, size.height);

        column.append(FACTORY.empty());
        column.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        size = column.getSize();
        assertEquals(Shape.DEFAULT_WIDTH, size.width);
        assertEquals(Shape.DEFAULT_HEIGHT, size.height);

        column.append(FACTORY.empty());
        column.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        size = column.getSize();
        assertEquals(Shape.DEFAULT_WIDTH, size.width);
        assertEquals(2 * Shape.DEFAULT_WIDTH, size.height);

        column.append(FACTORY.empty());
        column.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        size = column.getSize();
        assertEquals(Shape.DEFAULT_WIDTH, size.width);
        assertEquals(3 * Shape.DEFAULT_HEIGHT, size.height);

        column.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        column.set(0, FACTORY.empty());
        size = column.getSize();
        assertEquals(Shape.DEFAULT_WIDTH, size.width);
        assertEquals(3 * Shape.DEFAULT_HEIGHT, size.height);
    }

    @Test public void testSetShape() {
        final Shape empty0 = FACTORY.empty(), empty1 = FACTORY.empty(), empty2 = FACTORY.empty(),
                    empty3 = FACTORY.empty(), empty7 = FACTORY.empty();
        final ColumnLayout column = FACTORY.column();
        assertEquals(0, column.countShapes());

        column.append(empty0, empty1, empty2, empty3);
        assertEquals(4, column.countShapes());

        column.set(7, empty7);
        assertEquals(8, column.countShapes());
        assertSame(empty0, column.get(0));
        assertSame(empty1, column.get(1));
        assertSame(empty2, column.get(2));
        assertSame(empty3, column.get(3));
        assertSame(empty7, column.get(7));
        assertTrue(column.get(4) instanceof Empty);
        assertTrue(column.get(5) instanceof Empty);
        assertTrue(column.get(6) instanceof Empty);
    }

    @Test public void testAppendShape() {
        final ColumnLayout column = FACTORY.column();
        assertEquals(0, column.countShapes());
        column.append(FACTORY.empty());
        assertEquals(1, column.countShapes());
        column.append(FACTORY.empty());
        assertEquals(2, column.countShapes());
        column.append(FACTORY.empty());
        assertEquals(3, column.countShapes());
    }

    @Test public void testPaint() {
        final ColumnLayout column = FACTORY.column();
        final Graphics2D graphics = mock(Graphics2D.class);
        column.paint(graphics);

        final Shape shape1 = mock(Shape.class);
        when(shape1.getSize()).thenReturn(new Dimension());
        column.append(shape1);
        final Shape shape2 = mock(Shape.class);
        when(shape2.getSize()).thenReturn(new Dimension());
        column.append(shape2);
        column.paint(graphics);
        verify(shape1, times(1)).paint(graphics);
        verify(shape2, times(1)).paint(graphics);
    }
}
