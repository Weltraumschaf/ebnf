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
public class GridLayout extends AbstractLayout implements Adjustable {

    private final List<ColumnLayout> grid = Lists.newArrayList();

    public int countCols() {
        return grid.size();
    }

    public int counRows() {
        return countCols() == 0
               ? 0
               : grid.get(0).countShapes();
    }

    public GridLayout set(final int columnIndex, final int rowIndex, final Shape shape) {
        final int colCount = countCols();

        if (columnIndex >= colCount) {
            for (int i = colCount; i < columnIndex + 1; ++i) {
                grid.add(ColumnLayout.newColumnLayout());
            }
        }

        final ColumnLayout col = grid.get(columnIndex);
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
        grid.add(col);
        return this;
    }

    @Override
    public void paint(final Graphics2D graphic) {
        final Point pos = getPosition();
        int currentX = pos.x;

        for (ColumnLayout col : grid) {
            col.setPosition(pos.setX(currentX));
            col.setDebug(isDebug());
            col.paint(graphic);
            currentX += col.getSize().width;
        }
    }

    @Override
    public void adjust(final Graphics2D graphic) {
        int width = 0, height = 0;

        for (ColumnLayout col : grid) {
            col.adjust(graphic);
            final  Dimension colSize = col.getSize();
            width  += colSize.width;
            height = Math.max(height, colSize.height);
        }

        setSize(new Dimension(width, height));
    }

}
