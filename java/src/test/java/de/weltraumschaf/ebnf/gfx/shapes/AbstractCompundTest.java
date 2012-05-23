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
import java.awt.Graphics2D;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AbstractCompundTest {

    static class AbstractCompundStub extends AbstractCompund {
        public AbstractCompundStub(final GridLayout grid) {
            super(grid);
        }
    }

    @Test public void delegatesToGridLayout() {
        final GridLayout grid = mock(GridLayout.class);
        final AbstractCompundStub sut = new AbstractCompundStub(grid);

        sut.getPosition();
        verify(grid, times(1)).getPosition();

        final Point pos = new Point();
        sut.setPosition(pos);
        verify(grid, times(1)).setPosition(pos);

        sut.isDebug();
        verify(grid, times(1)).isDebug();

        sut.setDebug(true);
        verify(grid, times(1)).setDebug(true);
        sut.setDebug(false);
        verify(grid, times(1)).setDebug(false);

        sut.getSize();
        verify(grid, times(1)).getSize();

        final Dimension size = new Dimension();
        sut.setSize(size);
        verify(grid, times(1)).setSize(size);

        final Graphics2D graphics = mock(Graphics2D.class);
        sut.paint(graphics);
        verify(grid, times(1)).paint(graphics);

        sut.adjust(graphics);
        verify(grid, times(1)).adjust(graphics);
    }

}
