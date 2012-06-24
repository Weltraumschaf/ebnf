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

package de.weltraumschaf.ebnf.gfx;

import com.google.common.collect.Lists;
import de.weltraumschaf.ebnf.gfx.shapes.Shape;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class RailroadDiagram {

    private final List<Shape> shapes = Lists.newArrayList();
    private final boolean antialiasing;
    private boolean debug;

    public RailroadDiagram() {
        this(false);
    }

    public RailroadDiagram(final boolean antialiasing) {
        this.antialiasing = antialiasing;
        this.debug = false;
    }

    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    public void add(final Shape shape) {
        shapes.add(shape);
    }

    private void antialiase(final Graphics2D graphic) {
            final RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                                                RenderingHints.VALUE_ANTIALIAS_ON);

        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphic.setRenderingHints(hints);
    }

    public void paint(final Graphics2D graphic) {
        if (antialiasing) {
            antialiase(graphic);
        }

        for (Shape id : shapes) {
            id.setDebug(debug);
            id.paint(graphic);
        }
    }
}
