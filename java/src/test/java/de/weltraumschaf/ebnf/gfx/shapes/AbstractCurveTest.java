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
import java.awt.geom.Arc2D;
import static org.mockito.Mockito.*;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AbstractCurveTest {

    private static final Arc2D ARC = mock(Arc2D.class);

    private class CurveStub extends AbstractCurve {
        @Override
        protected Arc2D createArc() {
            return ARC;
        }

        @Override
        protected Point calcArcPosition() {
            return new Point();
        }

        @Override
        protected Dimension calcArcDimenson() {
            return new Dimension();
        }
    }

    @Test public void paint() {
        final AbstractCurve curve = new CurveStub();
        final Graphics2D graphics = mock(Graphics2D.class);
        curve.paint(graphics);
        verify(graphics, times(1)).setStroke(Strokes.createForRail());
        verify(graphics, times(1)).setColor(Color.BLACK);
        verify(graphics, times(1)).draw(ARC);
    }

}
