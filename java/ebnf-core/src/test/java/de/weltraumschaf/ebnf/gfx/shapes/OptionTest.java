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

import static de.weltraumschaf.ebnf.gfx.shapes.ShapeFactory.column;
import static de.weltraumschaf.ebnf.gfx.shapes.ShapeFactory.terminal;
import java.awt.Dimension;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class OptionTest {

    @Test public void setOptional() {
        final Option option = new Option();
        ColumnLayout split;
        ColumnLayout join;

        assertEquals(2, option.grid.counRows());
        assertEquals(3, option.grid.countCols());
        assertTrue(option.grid.get(0, 0) instanceof ColumnLayout);
        assertTrue(option.grid.get(1, 0) instanceof Empty);
        assertTrue(option.grid.get(2, 0) instanceof ColumnLayout);
        assertTrue(option.grid.get(0, 1) instanceof CurveNE);
        assertTrue(option.grid.get(1, 1) instanceof StraightWE);
        assertTrue(option.grid.get(2, 1) instanceof CurveNW);

        split = (ColumnLayout) option.grid.get(0, 0);
        assertEquals(1, split.countShapes());
        assertTrue(split.get(0) instanceof HForkSW);
        join = (ColumnLayout) option.grid.get(2, 0);
        assertEquals(1, join.countShapes());
        assertTrue(join.get(0) instanceof HForkSE);

        final AbstractTextShape term = terminal("foo");
        option.setOptional(term);
        assertEquals(2, option.grid.counRows());
        assertEquals(3, option.grid.countCols());
        assertTrue(option.grid.get(0, 0) instanceof ColumnLayout);
        assertSame(option.grid.get(1, 0), term);
        assertTrue(option.grid.get(2, 0) instanceof ColumnLayout);
        assertTrue(option.grid.get(0, 1) instanceof CurveNE);
        assertTrue(option.grid.get(1, 1) instanceof StraightWE);
        assertTrue(option.grid.get(2, 1) instanceof CurveNW);

        // taller optional
        final ColumnLayout greatColumn = column();
        greatColumn.setSize(new Dimension(Shape.DEFAULT_WIDTH, Shape.DEFAULT_HEIGHT * 3));
        option.setOptional(greatColumn);
        assertEquals(2, option.grid.counRows());
        assertEquals(3, option.grid.countCols());
        assertTrue(option.grid.get(0, 0) instanceof ColumnLayout);
        assertSame(option.grid.get(1, 0), greatColumn);
        assertTrue(option.grid.get(2, 0) instanceof ColumnLayout);
        assertTrue(option.grid.get(0, 1) instanceof CurveNE);
        assertTrue(option.grid.get(1, 1) instanceof StraightWE);
        assertTrue(option.grid.get(2, 1) instanceof CurveNW);

        split = (ColumnLayout) option.grid.get(0, 0);
        assertEquals(3, split.countShapes());
        assertTrue(split.get(0) instanceof HForkSW);
        assertTrue(split.get(1) instanceof StraightNS);
        assertTrue(split.get(2) instanceof StraightNS);
        join = (ColumnLayout) option.grid.get(2, 0);
        assertEquals(3, join.countShapes());
        assertTrue(join.get(0) instanceof HForkSE);
        assertTrue(join.get(1) instanceof StraightNS);
        assertTrue(join.get(2) instanceof StraightNS);
    }
}
