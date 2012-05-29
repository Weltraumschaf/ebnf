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

import com.google.common.collect.Lists;
import de.weltraumschaf.ebnf.gfx.Point;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class GridLayout extends AbstractLayout {

    private final List<ColumnLayout> columns = Lists.newArrayList();

    public int countCols() {
        return columns.size();
    }

    public int counRows() {
        return countCols() == 0
               ? 0
               : columns.get(0).countShapes();
    }

    public ColumnLayout get(final int columnIndex) {
        ColumnLayout col;

        try {
            col = columns.get(columnIndex);
        } catch (IndexOutOfBoundsException ex) {
            col = null;
        }

        if (null == col) {
            throw new IllegalArgumentException(
                    String.format("The column at columnIndex %d is not present!", columnIndex));
        }

        return col;
    }

    public Shape get(final int columnIndex, final int rowIndex) {
        Shape shape;

        try {
            shape = get(columnIndex).get(rowIndex);
        } catch (IndexOutOfBoundsException ex) {
            shape = null;
        }

        if (null == shape) {
            throw new IllegalArgumentException(
                    String.format("The shape at columnIndex %d and rowIndex %d is not present!",
                                  columnIndex, rowIndex));
        }

        return shape;
    }

    public GridLayout set(final int columnIndex, final int rowIndex, final Shape shape) {
        final int colCount = countCols();

        if (columnIndex >= colCount) {
            for (int i = colCount; i < columnIndex + 1; ++i) {
                columns.add(ColumnLayout.newColumnLayout());
            }
        }

        final ColumnLayout col = columns.get(columnIndex);
        col.set(rowIndex, shape);
        return this;
    }

    public GridLayout append(final ColumnLayout... cols) {
        for (ColumnLayout cl : cols) {
            append(cl);
        }

        return this;
    }

    public GridLayout append(final ColumnLayout col) {
        columns.add(col);
        return this;
    }

    @Override
    public void paint(final Graphics2D graphic) {
        final Point pos = getPosition();
        int currentX = pos.x;

        for (ColumnLayout col : columns) {
            col.setPosition(pos.setX(currentX));
            col.setDebug(isDebug());
            col.paint(graphic);
            currentX += col.getSize().width;
        }
    }

    @Override
    public void adjust(final Graphics2D graphic) {
        int width = 0;
        int height = 0;

        for (ColumnLayout col : columns) {
            col.adjust(graphic);
            final  Dimension colSize = col.getSize();
            width  += colSize.width;
            height = Math.max(height, colSize.height);
        }

        setSize(new Dimension(width, height));
    }

}
