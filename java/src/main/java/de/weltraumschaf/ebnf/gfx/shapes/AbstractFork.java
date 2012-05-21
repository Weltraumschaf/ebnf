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
import java.awt.Graphics2D;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public abstract class AbstractFork extends Empty {

    final Empty straight;
    final Empty curve;

    public AbstractFork(final Empty straight, final Empty curve) {
        super();
        this.straight = straight;
        this.curve    = curve;
    }

    @Override
    public void paint(final Graphics2D graphic) {
        super.paint(graphic);
        final Point pos = getPosition();

        straight.setPosition(pos);
        straight.setTransparent(true);
        straight.paint(graphic);

        curve.setPosition(pos);
        curve.setTransparent(true);
        curve.paint(graphic);
    }

}
