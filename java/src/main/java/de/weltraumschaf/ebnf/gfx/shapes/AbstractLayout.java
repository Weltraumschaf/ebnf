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

import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class AbstractLayout extends AbstractShape implements Adjustable {

    public AbstractLayout() {
        super();
        setSize(new Dimension(0, 0));
    }

    protected void adjustShape(final Shape shape, final Graphics2D graphics) {
        try {
            ((Adjustable) shape).adjust(graphics);
        } catch (ClassCastException ex) { // NOPMD
            // Not adjustable
        }
    }

}
