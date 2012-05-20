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
public class VForkSE extends Empty {

    @Override
    public void paint(final Graphics2D graphic) {
        super.paint(graphic);
        final Point pos = getPosition();
        final StraightNS straight = new StraightNS();
        straight.setPosition(pos);
        straight.setTransparent(true);
        straight.paint(graphic);
        final CurveSE curve = new CurveSE();
        curve.setPosition(pos);
        curve.setTransparent(true);
        curve.paint(graphic);
    }

}
