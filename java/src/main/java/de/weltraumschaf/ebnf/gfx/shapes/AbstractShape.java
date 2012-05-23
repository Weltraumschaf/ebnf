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
import de.weltraumschaf.ebnf.gfx.Strokes;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Stack;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
abstract public class AbstractShape implements Shape {

    protected static class GraphicsSetting {
        private final Color color;
        private final Stroke stroke;

        public GraphicsSetting(final Color color, final Stroke stroke) {
            this.color  = color;
            this.stroke = stroke;
        }

        public Color getColor() {
            return color;
        }

        public Stroke getStroke() {
            return stroke;
        }
    }

    private final Stack<GraphicsSetting> backup = new Stack<GraphicsSetting>();
    private Point position;
    private Dimension size;
    private boolean debug;

    public AbstractShape() {
        size     = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        position = new Point();
    }

    @Override
    public Point getPosition() {
        return position;
    }

    @Override
    public void setPosition(final Point position) {
        this.position = position;
    }

    @Override
    public void setDebug(final boolean onOff) {
        debug = onOff;
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public void setSize(final Dimension size) {
        this.size = size;
    }

    protected boolean isDebug() {
        return debug;
    }

    protected int countBackup() {
        return backup.size();
    }

    protected Stack<GraphicsSetting> getBackup() {
        return backup;
    }

    protected void backupColorAndStroke(final Graphics2D graphic) {
        backup.push(new GraphicsSetting(graphic.getColor(), graphic.getStroke()));
    }

    protected void resotreColorAndStroke(final Graphics2D graphic) {
        if (!backup.isEmpty()) {
            final GraphicsSetting settings = backup.pop();
            graphic.setColor(settings.getColor());
            graphic.setStroke(settings.getStroke());
        }
    }

    protected void debugShape(final Graphics2D graphic, final Paintable paintable) {
        if (isDebug()) {
            backupColorAndStroke(graphic);
            graphic.setColor(Color.RED);
            graphic.setStroke(Strokes.createForDebug());
            paintable.paint(graphic);
            resotreColorAndStroke(graphic);
        }
    }

    protected int getCenterX() {
        return getPosition().x + getSize().width / 2;
    }

    protected int getCenterY() {
        return getPosition().y + getSize().height / 2;
    }

}
