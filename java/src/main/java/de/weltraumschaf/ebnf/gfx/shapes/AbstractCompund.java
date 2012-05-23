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

import de.weltraumschaf.ebnf.gfx.Point;
import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AbstractCompund extends AbstractShape implements Adjustable {

    protected final GridLayout grid;

    public AbstractCompund(final GridLayout grid) {
        super();
        this.grid = grid;
    }

    @Override
    public Point getPosition() {
        return grid.getPosition();
    }

    @Override
    public void setPosition(final Point pos) {
        grid.setPosition(pos);
    }

    @Override
    protected boolean isDebug() {
        return grid.isDebug();
    }

    @Override
    public void setDebug(final boolean onOff) {
        grid.setDebug(onOff);
    }

    @Override
    public void setSize(final Dimension size) {
        grid.setSize(size);
    }

    @Override
    public Dimension getSize() {
        return grid.getSize();
    }

    @Override
    public void paint(final Graphics2D graphic) {
        grid.paint(graphic);
    }

    @Override
    public void adjust(final Graphics2D graphic) {
        grid.adjust(graphic);
    }

    protected void extendColumnWithEmpty(final int height, final int[] colIndexs, final int rowIndex) {
        extendColumnWithShape(height, colIndexs, rowIndex, Empty.class);
    }

    protected void extendColumnWithStraightNS(final int height, final int[] colIndexs, final int rowIndex) {
        extendColumnWithShape(height, colIndexs, rowIndex, StraightNS.class);
    }

    protected void extendColumnWithShape(final int height, final int[] colIndexs, final int rowIndex, final Class<? extends Shape> type) {
        if (DEFAULT_HEIGHT < height) {
            final int count = height / DEFAULT_HEIGHT - 1;

            for (int i = 0; i < count; ++i) {
                for (int j = 0; j < colIndexs.length; ++j) {
                    try {
                        final Shape filler = type.newInstance();
                        ((ColumnLayout) grid.get(colIndexs[j], rowIndex)).append(filler);
                    } catch (InstantiationException ex) {
                        throw new IllegalArgumentException("Can't instantiate shape of type: "
                                                           + type.getName() + "!", ex);
                    } catch (IllegalAccessException ex) {
                        throw new IllegalArgumentException("Can't access shape of type: "
                                                           + type.getName() + "!", ex);
                    }
                }
            }
        }
    }
}
