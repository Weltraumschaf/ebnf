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
import static de.weltraumschaf.ebnf.gfx.ShapeFactory.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Sequence extends AbstractLayout {

    private final List<Shape> row = Lists.newArrayList();

    public Shape get(final int index) {
        return row.get(index);
    }

    public Sequence set(final int index, final Shape shape) {
        final int count = countShapes();

        if (index >= count) {
            append(empty(count - 1));
            append(shape);
        } else {
            row.set(index, shape);
        }

        return this;
    }

    public Sequence append(final Shape... shapes) {
        for (Shape shape : shapes) {
            append(shape);
        }

        return this;
    }

    public Sequence append(final Shape shape) {
        row.add(shape);
        return this;
    }

    @Override
    public void paint(final Graphics2D graphic) {
        if (countShapes() == 0) {
            return;
        }

        final Point pos = getPosition();
        int currentX = pos.x;

        for (Shape shape : row) {
            shape.setPosition(pos.setX(currentX));
            shape.setDebug(isDebug());
            shape.paint(graphic);
            currentX += shape.getSize().width;
        }
    }

    public int countShapes() {
        return row.size();
    }

    @Override
    public void adjust(final Graphics2D graphics) {
        int width = 0, height = 0;

        for (Shape shape : row) {
            adjustShape(shape, graphics);
            final Dimension shapeSize = shape.getSize();
            width += shapeSize.width;
            height = Math.max(height, shapeSize.height);
        }

        for (Shape shape : row) {
            shape.getSize().height = height;
        }

        setSize(new Dimension(width, height));
    }

}
