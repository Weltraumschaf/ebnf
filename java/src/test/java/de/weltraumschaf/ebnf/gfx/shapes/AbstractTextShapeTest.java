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

import de.weltraumschaf.ebnf.gfx.StringPainter;
import java.awt.Font;
import java.awt.Graphics2D;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class AbstractTextShapeTest {

    class AbstractTextShapeStub extends AbstractTextShape {

        public AbstractTextShapeStub(final String text, final Font font) {
            super(text, font);
        }

        public AbstractTextShapeStub(final String text) {
            super(text);
        }

        @Override
        public void adjust(final Graphics2D graphic) {
            // stubbed method;
        }

    }

    @Test public void createObject() {
        AbstractTextShape sut = new AbstractTextShapeStub("foobar");
        assertSame(StringPainter.SANSERIF, sut.getFont());
        assertEquals("foobar", sut.getText());

        sut = new AbstractTextShapeStub("snafu", StringPainter.MONOSPACED);
        assertSame(StringPainter.MONOSPACED, sut.getFont());
        assertEquals("snafu", sut.getText());
    }

    @Test public void calculateWidth() {
        try {
            AbstractTextShape.calculateWidth(-1);
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException ex) {
            assertEquals("box width need to be greater or equal zero!", ex.getMessage());
        }

        assertEquals(1 * Shape.DEFAULT_WIDTH, AbstractTextShape.calculateWidth(0));
        assertEquals(1 * Shape.DEFAULT_WIDTH, AbstractTextShape.calculateWidth(1));
        assertEquals(1 * Shape.DEFAULT_WIDTH, AbstractTextShape.calculateWidth(15));
        assertEquals(1 * Shape.DEFAULT_WIDTH, AbstractTextShape.calculateWidth(Shape.DEFAULT_WIDTH - 11));
        assertEquals(2 * Shape.DEFAULT_WIDTH, AbstractTextShape.calculateWidth(Shape.DEFAULT_WIDTH));
        assertEquals(3 * Shape.DEFAULT_WIDTH, AbstractTextShape.calculateWidth(3 * Shape.DEFAULT_WIDTH - 11));
        assertEquals(4 * Shape.DEFAULT_WIDTH, AbstractTextShape.calculateWidth(3 * Shape.DEFAULT_WIDTH));
    }
}
