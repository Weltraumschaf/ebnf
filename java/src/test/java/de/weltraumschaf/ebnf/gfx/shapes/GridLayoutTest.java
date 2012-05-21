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
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class GridLayoutTest {

    private static final ShapeFactory FACTORY = ShapeFactory.getInstance();

    @Test public void testGetSize() {
        Dimension size;
        final GridLayout grid = FACTORY.grid();

        size = grid.getSize();
        assertEquals(0, size.width);
        assertEquals(0, size.height);

        grid.append(FACTORY.column().append(FACTORY.empty(), FACTORY.empty(), FACTORY.empty()));
        grid.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        size = grid.getSize();
        assertEquals(Shape.DEFAULT_WIDTH, size.width);
        assertEquals(3 * Shape.DEFAULT_HEIGHT, size.height);

        grid.append(FACTORY.column().append(FACTORY.empty(), FACTORY.empty(), FACTORY.empty()));
        grid.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        size = grid.getSize();
        assertEquals(2 * Shape.DEFAULT_WIDTH, size.width);
        assertEquals(3 * Shape.DEFAULT_HEIGHT, size.height);

        grid.append(FACTORY.column().append(FACTORY.empty(), FACTORY.empty(), FACTORY.empty(), FACTORY.empty()));
        grid.adjust(mock(Graphics2D.class, CALLS_REAL_METHODS));
        size = grid.getSize();
        assertEquals(3 * Shape.DEFAULT_WIDTH, size.width);
        assertEquals(4 * Shape.DEFAULT_WIDTH, size.height);

    }

    @Test public void testPaintGrid() {
        final GridLayout grid = FACTORY.grid();
        final Graphics2D graphic = mock(Graphics2D.class);
        final ColumnLayout row1 = mock(ColumnLayout.class);
        when(row1.getSize()).thenReturn(new Dimension());
        grid.append(row1);
        final ColumnLayout row2 = mock(ColumnLayout.class);
        when(row2.getSize()).thenReturn(new Dimension());
        grid.append(row2);
        grid.paint(graphic);
        verify(row1, times(1)).paint(graphic);
        verify(row2, times(1)).paint(graphic);
    }

    @Test public void testSetShape() {
        final GridLayout grid = FACTORY.grid();
        assertEquals(0, grid.countCols());
        assertEquals(0, grid.counRows());

        grid.set(0, 0, FACTORY.empty());
        assertEquals(1, grid.countCols());
        assertEquals(1, grid.counRows());

        grid.set(0, 1, FACTORY.empty());
        assertEquals(1, grid.countCols());
        assertEquals(2, grid.counRows());

        grid.set(1, 0, FACTORY.empty());
        assertEquals(2, grid.countCols());
        assertEquals(2, grid.counRows());

        grid.set(1, 1, FACTORY.empty());
        assertEquals(2, grid.countCols());
        assertEquals(2, grid.counRows());
    }

    @Test public void testAppendColumnLayout() {
        final GridLayout grid = FACTORY.grid();
        assertEquals(0, grid.countCols());
        assertEquals(0, grid.counRows());

        grid.append(FACTORY.column(FACTORY.empty()));
        assertEquals(1, grid.countCols());
        assertEquals(1, grid.counRows());

        grid.append(FACTORY.column(FACTORY.empty()), FACTORY.column(FACTORY.empty()));
        assertEquals(3, grid.countCols());
        assertEquals(1, grid.counRows());
    }

}
