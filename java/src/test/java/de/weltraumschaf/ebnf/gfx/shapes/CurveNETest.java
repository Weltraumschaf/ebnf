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
import java.awt.geom.Arc2D;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CurveNETest {

    @Test public void calcArcPosition() {
        final CurveNE curve = new CurveNE();
        assertEquals(new Point(15, -16), curve.calcArcPosition());
        curve.setPosition(new Point(100, 100));
        assertEquals(new Point(115, 84), curve.calcArcPosition());
        curve.setSize(new Dimension(50, 50));
        assertEquals(new Point(125, 74), curve.calcArcPosition());
    }

    @Test public void calcArcDimenson() {
        final CurveNE curve = new CurveNE();
        assertEquals(new Dimension(32, 31), curve.calcArcDimenson());
        curve.setSize(new Dimension(50, 50));
        assertEquals(new Dimension(51, 50), curve.calcArcDimenson());
    }

    @Test public void createArc() {
        final CurveNE curve = new CurveNE();
        final Arc2D arc = curve.createArc();
        assertEquals(180, (int) arc.getAngleStart());
        assertEquals(90, (int) arc.getAngleExtent());
    }
}
