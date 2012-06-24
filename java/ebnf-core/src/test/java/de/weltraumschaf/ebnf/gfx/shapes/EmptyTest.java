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
import static de.weltraumschaf.ebnf.gfx.shapes.ShapeFactory.empty;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class EmptyTest {
    private final Graphics2D graphics = mock(Graphics2D.class);
    private final Empty empty         = empty();
    private final Point pos      = empty.getPosition();
    private final Dimension size = empty.getSize();

    @Test public void paintNotTransparent() {
        assertFalse(empty.isDebug());
        assertFalse(empty.isTransparent());
        empty.paint(graphics);
        verify(graphics, times(1)).setColor(Color.WHITE);
        verify(graphics, times(1)).fillRect(pos.x, pos.y, size.width, size.height);
    }

    @Test public void paintTransparent() {
        assertFalse(empty.isDebug());
        empty.setTransparent(true);
        assertTrue(empty.isTransparent());
        empty.paint(graphics);
        verify(graphics, never()).setColor(Color.WHITE);
        verify(graphics, never()).fillRect(pos.x, pos.y, size.width, size.height);
    }

    @Test public void paintDebug() {
        empty.setTransparent(true);
        assertTrue(empty.isTransparent());
        empty.setDebug(true);
        assertTrue(empty.isDebug());
        empty.paint(graphics);
        verify(graphics, times(1)).drawLine(0, 15, 30, 15);
        verify(graphics, times(1)).drawLine(15, 0, 15, 30);
        verify(graphics, times(1)).drawRect(0, 0, 30, 30);
    }
}
