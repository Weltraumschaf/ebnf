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
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AbstractForkTest {

    static class AbstractForkStub extends AbstractFork {
        public AbstractForkStub(final Empty straight, final Empty curve) {
            super(straight, curve);
        }
    }

    @Test public void paint() {
        final Point pos = new Point(1, 2);
        final Graphics2D graphics = mock(Graphics2D.class);

        final Empty straight   = mock(Empty.class);
        final Empty curve      = mock(Empty.class);

        final AbstractFork sut = new AbstractForkStub(straight, curve);
        sut.setPosition(pos);
        sut.paint(graphics);

        verify(straight, times(1)).setPosition(pos);
        verify(straight, times(1)).setTransparent(true);
        verify(straight, times(1)).paint(graphics);

        verify(curve, times(1)).setPosition(pos);
        verify(curve, times(1)).setTransparent(true);
        verify(curve, times(1)).paint(graphics);
    }
}
