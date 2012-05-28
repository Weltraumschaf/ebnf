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
import static de.weltraumschaf.ebnf.gfx.shapes.ShapeFactory.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ColumnLayout extends AbstractLayout {

    private final List<Shape> col = Lists.newArrayList();

    public static ColumnLayout newColumnLayout() {
        return new ColumnLayout();
    }

    public Shape get(final int index) {
        return col.get(index);
    }

    public ColumnLayout set(final int index, final Shape shape) {
        final int count = countShapes();

        if (index >= count) {
            append(empty(index - count));
            append(shape);
        } else {
            col.set(index, shape);
        }

        return this;
    }

    public ColumnLayout append(final Shape... shapes) {
        for (Shape shape : shapes) {
            append(shape);
        }

        return this;
    }

    public ColumnLayout append(final Shape shape) {
        col.add(shape);
        return this;
    }

    public int countShapes() {
        return col.size();
    }

    @Override
    public void paint(final Graphics2D graphic) {
        if (countShapes() == 0) {
            return;
        }

        final Point pos = getPosition();
        int currentY = pos.y;

        for (Shape shape : col) {
            shape.setPosition(pos.setY(currentY));
            shape.setDebug(isDebug());
            shape.paint(graphic);
            currentY += shape.getSize().height;
        }
    }

    @Override
    public void adjust(final Graphics2D graphics) {
        int width = 0;
        int height = 0;

        for (Shape shape : col) {
            adjustShape(shape, graphics);
            final Dimension shapeSize = shape.getSize();
            height += shapeSize.height;
            width = Math.max(width, shapeSize.width);
        }

        for (Shape shape : col) {
            shape.getSize().width = width;
        }

        setSize(new Dimension(width, height));
    }
}
