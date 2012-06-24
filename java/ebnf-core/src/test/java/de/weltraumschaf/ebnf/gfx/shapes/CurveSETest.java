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
public class CurveSETest {

    @Test public void calcArcPosition() {
        final CurveSE curve = new CurveSE();
        assertEquals(new Point(15, 15), curve.calcArcPosition());
        curve.setPosition(new Point(100, 100));
        assertEquals(new Point(115, 115), curve.calcArcPosition());
        curve.setSize(new Dimension(50, 50));
        assertEquals(new Point(125, 125), curve.calcArcPosition());
    }

    @Test public void calcArcDimenson() {
        final CurveSE curve = new CurveSE();
        assertEquals(new Dimension(32, 32), curve.calcArcDimenson());
        curve.setSize(new Dimension(50, 50));
        assertEquals(new Dimension(51, 51), curve.calcArcDimenson());
    }

    @Test public void createArc() {
        final CurveSE curve = new CurveSE();
        final Arc2D arc = curve.createArc();
        assertEquals(90, (int) arc.getAngleStart());
        assertEquals(90, (int) arc.getAngleExtent());
    }
}
