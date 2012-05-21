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
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AbstractEndpointTest {

    class AbstractEndpointImpl extends AbstractEndpoint {

    }

    @Test public void paint() {
        final Graphics2D graphics = mock(Graphics2D.class);
        final AbstractEndpoint sut = new AbstractEndpointImpl();

        sut.setPosition(new Point(0, 0));
        sut.setSize(new Dimension(31, 31));
        sut.paint(graphics);

        verify(graphics, times(1)).setColor(Color.BLACK);
        verify(graphics, times(1)).setStroke(Strokes.createForRail(true));
        verify(graphics, times(1)).drawLine(15, 3, 15, 27);
    }
}
